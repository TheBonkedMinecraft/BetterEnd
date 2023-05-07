package org.betterx.betterend.blocks;

import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class CrystalMossCoverBlock extends MultifaceBlock implements BonemealableBlock, SimpleWaterloggedBlock, RenderLayerProvider {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public CrystalMossCoverBlock(MaterialColor color) {
        super(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, color)
                                 .noCollission()
                                 .strength(0.2f)
                                 .sound(SoundType.GLOW_LICHEN)
                                 .lightLevel(GlowLichenBlock.emission(7)));
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(
            BlockState blockState,
            Direction direction,
            BlockState blockState2,
            LevelAccessor levelAccessor,
            BlockPos blockPos,
            BlockPos blockPos2
    ) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return !blockPlaceContext.getItemInHand().is(EndBlocks.CRYSTAL_MOSS_COVER.asItem()) || super.canBeReplaced(
                blockState,
                blockPlaceContext
        );
    }

    @Override
    public boolean isValidBonemealTarget(
            BlockGetter blockGetter,
            BlockPos blockPos,
            BlockState blockState,
            boolean bl
    ) {
        return Direction.stream()
                        .anyMatch(direction -> this.spreader.canSpreadInAnyDirection(
                                blockState,
                                blockGetter,
                                blockPos,
                                direction.getOpposite()
                        ));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(
            ServerLevel serverLevel,
            RandomSource randomSource,
            BlockPos blockPos,
            BlockState blockState
    ) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }
}
