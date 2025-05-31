package com.landscapesreimagined.createbiggerstoragetocreate6.mixin;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.impl.contraption.BlockMovementChecksImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import uwu.lopyluna.create_bs.content.vault.TieredVaultBlock;
import uwu.lopyluna.create_bs.registry.BSMovementChecks;

@Mixin(BSMovementChecks.class)
public class BSMovementChecksMixin {

    /**
     * @author gamma_02
     * @reason compat with Create 6
     */
    @Overwrite(remap = false)
    public static void register(){
        BlockMovementChecksImpl.registerAttachedCheck((state, world, pos, direction) -> state.getBlock() instanceof TieredVaultBlock && ConnectivityHandler.isConnected(world, pos, pos.relative(direction))
                ? BlockMovementChecks.CheckResult.SUCCESS
                : BlockMovementChecks.CheckResult.PASS);
    }
}
