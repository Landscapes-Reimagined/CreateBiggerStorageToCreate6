package com.landscapesreimagined.createbiggerstoragetocreate6.MountedStorage;

import com.landscapesreimagined.createbiggerstoragetocreate6.CBStorageToCreate6;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.contraption.storage.item.WrapperMountedItemStorage;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultMountedStorage;
import com.simibubi.create.foundation.utility.CreateCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_bs.content.vault.TieredVaultBlockEntity;

public class TieredVaultMountedStorage extends WrapperMountedItemStorage<ItemStackHandler> {
    public static final Codec<TieredVaultMountedStorage> CODEC = CreateCodecs.ITEM_STACK_HANDLER.xmap(
            TieredVaultMountedStorage::new, storage -> storage.wrapped
    );

    protected TieredVaultMountedStorage(MountedItemStorageType<?> type, ItemStackHandler handler) {
        super(type, handler);
    }

    protected TieredVaultMountedStorage(ItemStackHandler handler) {
        this(CBStorageToCreate6.VAULT.get(), handler);
    }

    @Override
    public void unmount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof TieredVaultBlockEntity vault) {
            vault.applyInventoryToBlock(this.wrapped);
        }
    }

    @Override
    public boolean handleInteraction(ServerPlayer player, Contraption contraption, StructureTemplate.StructureBlockInfo info) {
        // vaults should never be opened.
        return false;
    }

    public static TieredVaultMountedStorage fromVault(TieredVaultBlockEntity vault) {
        // Vault inventories have a world-affecting onContentsChanged, copy to a safe one
        return new TieredVaultMountedStorage(copyToItemStackHandler(vault.getInventoryOfBlock()));
    }

    public static TieredVaultMountedStorage fromLegacy(CompoundTag nbt) {
        ItemStackHandler handler = new ItemStackHandler();
        handler.deserializeNBT(nbt);
        return new TieredVaultMountedStorage(handler);
    }


}
