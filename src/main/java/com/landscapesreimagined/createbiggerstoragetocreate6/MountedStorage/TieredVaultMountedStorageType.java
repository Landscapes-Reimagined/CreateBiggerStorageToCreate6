package com.landscapesreimagined.createbiggerstoragetocreate6.MountedStorage;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultMountedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_bs.content.vault.TieredVaultBlockEntity;

public class TieredVaultMountedStorageType extends MountedItemStorageType<TieredVaultMountedStorage> {
    public TieredVaultMountedStorageType() {
        super(TieredVaultMountedStorage.CODEC);
    }

    @Override
    @Nullable
    public TieredVaultMountedStorage mount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        return be instanceof TieredVaultBlockEntity vault ? TieredVaultMountedStorage.fromVault(vault) : null;
    }
}
