package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.interfaces.tools.AddMineableShears;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.basis.EndUnderwaterPlantBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class EndLilyBlock extends EndUnderwaterPlantBlock implements AddMineableShears {
    public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
    private static final VoxelShape SHAPE_BOTTOM = Block.box(4, 0, 4, 12, 16, 12);
    private static final VoxelShape SHAPE_TOP = Block.box(2, 0, 2, 14, 6, 14);

    public EndLilyBlock() {
        super(baseUnderwaterPlantSettings()
                .lightLevel((state) -> state.getValue(SHAPE) == TripleShape.TOP ? 13 : 0)
        );
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction facing,
            BlockState neighborState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (!canSurvive(state, world, pos)) {
            return state.getValue(SHAPE) == TripleShape.TOP
                    ? Blocks.AIR.defaultBlockState()
                    : Blocks.WATER.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        Vec3 vec3d = state.getOffset(view, pos);
        VoxelShape shape = state.getValue(SHAPE) == TripleShape.TOP ? SHAPE_TOP : SHAPE_BOTTOM;
        return shape.move(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(SHAPE) == TripleShape.TOP ? Fluids.EMPTY.defaultFluidState() : Fluids.WATER.getSource(
                false);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(SHAPE) == TripleShape.TOP) {
            return world.getBlockState(pos.below()).getBlock() == this;
        } else if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
            return isTerrain(world.getBlockState(pos.below()));
        } else {
            BlockState up = world.getBlockState(pos.above());
            BlockState down = world.getBlockState(pos.below());
            return up.getBlock() == this && down.getBlock() == this;
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(SHAPE) == TripleShape.TOP) {
            return Lists.newArrayList(
                    new ItemStack(EndItems.END_LILY_LEAF, MHelper.randRange(1, 2, MHelper.RANDOM_SOURCE)),
                    new ItemStack(EndBlocks.END_LILY_SEED, MHelper.randRange(1, 2, MHelper.RANDOM_SOURCE))
            );
        }
        return Collections.emptyList();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(EndBlocks.END_LILY_SEED);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }
}
