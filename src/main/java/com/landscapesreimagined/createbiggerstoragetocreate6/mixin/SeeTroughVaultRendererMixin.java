package com.landscapesreimagined.createbiggerstoragetocreate6.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import uwu.lopyluna.create_bs.content.vault.SeeThroughVaultRenderer;

@OnlyIn(Dist.CLIENT)
@Mixin(SeeThroughVaultRenderer.class)
public class SeeTroughVaultRendererMixin {

    /**
     * @author gamma_02
     * @reason Changes to TransformStack in Create 6.0 that I did not want to use bytecode transformation for
     */
    @Overwrite(remap = false)
    protected void renderItem(BlockPos pos, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack, int slot) {
        ms.pushPose();
        Minecraft mc = Minecraft.getInstance();
        RandomSource r = RandomSource.create(pos.hashCode() * (slot + 1L));
        Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 0.35F);
        float multiplier = r.nextIntBetweenInclusive(150, 500) / 100.0F;
        float time = AnimationTickHolder.getRenderTime() / 20.0F;
        float angle = time * 10.0F * multiplier % 360.0F * (r.nextBoolean() ? 1.0F : -1.0F);
        float timeOffset = slot * 57.2958F % 360.0F;
        float bob = (float)Math.sin((time + timeOffset) * Math.PI) * 0.05F;
        ms.translate(vec.x + 0.5, vec.y + 0.35 + bob, vec.z + 0.5);
        TransformStack.of(ms).rotateY(AngleHelper.rad(angle));
        TransformStack.of(ms).scale(0.75F);
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, mc.level, 0);
        ms.popPose();
    }
}
