package com.landscapesreimagined.createbiggerstoragetocreate6.mixin;

import com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.InstructionFixers;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    public static final boolean debug = true;

    private static final Logger log = LoggerFactory.getLogger(MixinConfigPlugin.class);


    @Override
    public void onLoad(String mixinPackage) {
        try {
            //load classes for use in preinit stuff
            this.getClass().getClassLoader().loadClass("com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.InstructionToString");
            this.getClass().getClassLoader().loadClass("com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.ClassConstants");
            this.getClass().getClassLoader().loadClass("com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.InstructionFixers");

            this.getClass().getClassLoader().loadClass("com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.MethodReplacers");
            this.getClass().getClassLoader().loadClass("com.landscapesreimagined.createbiggerstoragetocreate6.preinitutils.LookAroundMatchers");

        } catch (ClassNotFoundException e) {
            log.error("Could not find preinit util class!!! This should be fine, but here be dragons! If this is in the log, I CAN NOT HELP YOU!");
            log.error(e.getMessage());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        System.out.println("HELLO FROM CLASS TRANSFORMATION :DDDDDDDDD");

        final String targetClassJavaName = targetClassName.substring(targetClassName.lastIndexOf('.') + 1);

        final String mixinJavaName = mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1);


//        if(targetClassJavaName.equals("BSSpriteShifts")){
//            executeAllNormalInstructionFixers(targetClass);
//        }

        if(mixinJavaName.equals("GeneralFixerMultiTargetMixin")){
            executeAllNormalInstructionFixers(targetClass);
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }


    private static void executeAllNormalInstructionFixers(ClassNode targetClass) {
        InstructionFixers.applyStaticInterfaceMoves(targetClass);


        for(FieldNode f : targetClass.fields) {
            InstructionFixers.applyFieldClassMoves(f, targetClass);
            InstructionFixers.applyStaticFieldClassMoves(f, targetClass);
        }

        for(MethodNode m : targetClass.methods){
            InstructionFixers.applyStaticMethodClassMoves(m, targetClass);

            for(AbstractInsnNode insn : m.instructions){
                InstructionFixers.applyStaticInsnClassMoves(insn, m);
            }
        }
    }


    private static void executeAllNormalInstructionFixers(ClassNode targetClass, Function<AbstractInsnNode, AbstractInsnNode> executeOnAllInsnsAfterTransformation) {
        InstructionFixers.applyStaticInterfaceMoves(targetClass);


        for(FieldNode f : targetClass.fields) {
            InstructionFixers.applyFieldClassMoves(f, targetClass);
            InstructionFixers.applyStaticFieldClassMoves(f, targetClass);
        }

        for(MethodNode m : targetClass.methods){
            InstructionFixers.applyStaticMethodClassMoves(m, targetClass);

            ArrayDeque<AbstractInsnNode> toDelete = new ArrayDeque<>();

            for(AbstractInsnNode insn : m.instructions){
                InstructionFixers.applyStaticInsnClassMoves(insn, m);
                AbstractInsnNode beforeInsn = executeOnAllInsnsAfterTransformation.apply(insn);

                if(beforeInsn == null){
                    toDelete.push(insn);
                    continue;
                }

                if(insn != beforeInsn) {
                    m.instructions.insertBefore(insn, beforeInsn);
                    toDelete.push(insn);
                }
            }

            InstructionFixers.removeAllInstructions(targetClass, m, toDelete);
        }
    }


    private static void dumpClass(String targetClassName, ClassNode targetClass, boolean before) {
        if(!debug)
            return;

        ClassWriter writer = new ClassWriter(0);
        targetClass.accept(writer);

        File bytecodeDump = new File("C:\\Users\\gamma\\OneDrive\\Documents\\sources\\class-" + (before ? "before" : "after") + "-dump-" + targetClassName.substring(targetClassName.lastIndexOf('.') + 1) + ".class");

        try {
            Files.write(bytecodeDump.toPath(), writer.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeDumpFile(String targetClassName, String classInstructions) {
        if(!debug)
            return;

        File dumpFile = new File("C:\\Users\\gamma\\OneDrive\\Documents\\sources\\instruction dumps\\dump-" + targetClassName.substring(targetClassName.lastIndexOf(".") + 1) + ".txt");

        try {
            Files.writeString(dumpFile.toPath(), classInstructions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
