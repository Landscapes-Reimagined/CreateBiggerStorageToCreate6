package com.landscapesreimagined.createbiggerstoragetocreate6.mixin;

import com.landscapesreimagined.createbiggerstoragetocreate6.CBStorageToCreate6;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import uwu.lopyluna.create_bs.registry.BSBlocks;

@Mixin(BSBlocks.class)
public class BSBlockMixin {

    @WrapOperation(
            method = "lambda$static$8",
            at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/builders/BlockBuilder;onRegister(Lcom/tterrag/registrate/util/nullness/NonNullConsumer;)Lcom/tterrag/registrate/builders/Builder;")
    )
    private static Builder wrapOnRegister(BlockBuilder instance, NonNullConsumer nonNullConsumer, Operation<Builder> original){
        return original.call(instance, nonNullConsumer)
                .transform(MountedItemStorageType.mountedItemStorage(CBStorageToCreate6.VAULT));
    }
}
