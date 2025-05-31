package com.landscapesreimagined.createbiggerstoragetocreate6.mixin;

import org.spongepowered.asm.mixin.Mixin;
import uwu.lopyluna.create_bs.CreateBS;
import uwu.lopyluna.create_bs.content.vault.TieredVaultCTBehaviour;
import uwu.lopyluna.create_bs.content.vault.TieredVaultItem;
import uwu.lopyluna.create_bs.registry.BSSpriteShifts;

@Mixin(
        value = {
                CreateBS.class,
                BSSpriteShifts.class,
                TieredVaultItem.class,
                TieredVaultCTBehaviour.class,
        }
)
public class GeneralFixerMultiTargetMixin {
}
