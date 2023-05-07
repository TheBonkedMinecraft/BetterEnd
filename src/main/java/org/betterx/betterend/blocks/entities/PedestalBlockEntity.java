package org.betterx.betterend.blocks.entities;

import org.betterx.betterend.blocks.basis.PedestalBlock;
import org.betterx.betterend.registry.EndBlockEntities;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalBlockEntity extends BlockEntity implements Container {
    private ItemStack activeItem = ItemStack.EMPTY;

    public PedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(EndBlockEntities.PEDESTAL, blockPos, blockState);
    }

    public PedestalBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected void toTag(CompoundTag tag) {
        if (activeItem != ItemStack.EMPTY) {
            tag.put("active_item", activeItem.save(new CompoundTag()));
        }
    }

    protected void fromTag(CompoundTag tag) {
        if (tag.contains("active_item")) {
            CompoundTag itemTag = tag.getCompound("active_item");
            activeItem = ItemStack.of(itemTag);
        }
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return activeItem.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return activeItem;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return removeItemNoUpdate(slot);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return isEmpty();
    }

    @Override
    public void clearContent() {
        activeItem = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stored = activeItem;
        clearContent();
        return stored;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        activeItem = stack.split(1);
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null && !level.isClientSide()) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof PedestalBlock) {
                state = state.setValue(PedestalBlock.HAS_ITEM, !isEmpty());
                state = state.setValue(PedestalBlock.HAS_LIGHT, activeItem.getItem() == EndItems.ETERNAL_CRYSTAL);
            }
            level.setBlockAndUpdate(worldPosition, state);
            level.blockEntityChanged(worldPosition);
        }
        super.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        fromTag(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        toTag(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}
