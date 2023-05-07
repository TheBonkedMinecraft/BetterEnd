package org.betterx.betterend.blocks;

import org.betterx.bclib.interfaces.CustomItemProvider;
import org.betterx.bclib.interfaces.tools.AddMineableShears;
import org.betterx.betterend.blocks.basis.EndPlantBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.google.common.collect.Lists;

import java.util.List;

public class FlamaeaBlock extends EndPlantBlock implements CustomItemProvider, AddMineableShears {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public FlamaeaBlock() {
        super(basePlantSettings()
                .sound(SoundType.WET_GRASS)
                .offsetType(BlockBehaviour.OffsetType.NONE)
        );
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(Blocks.WATER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Lists.newArrayList(new ItemStack(this));
    }

    @Override
    public boolean canBePotted() {
        return false;
    }

    @Override
    public BlockItem getCustomItem(ResourceLocation resourceLocation, Item.Properties itemSettings) {
        return new PlaceOnWaterBlockItem(this, itemSettings);
    }
}
