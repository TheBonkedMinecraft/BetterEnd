package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import com.google.common.collect.Lists;

import java.util.Queue;

@SuppressWarnings("deprecation")
public class MengerSpongeBlock extends BaseBlockNotFull implements RenderLayerProvider {
    private static final VoxelShape SHAPE;

    public MengerSpongeBlock() {
        super(FabricBlockSettings.copyOf(Blocks.SPONGE).noOcclusion());
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (absorbWater(world, pos)) {
            world.setBlockAndUpdate(pos, EndBlocks.MENGER_SPONGE_WET.defaultBlockState());
        }
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
        if (absorbWater(world, pos)) {
            return EndBlocks.MENGER_SPONGE_WET.defaultBlockState();
        }
        return state;
    }

    private boolean absorbWater(LevelAccessor world, BlockPos pos) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));
        int i = 0;

        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> pair = queue.poll();
            BlockPos blockPos = pair.getA();
            int j = pair.getB();

            for (Direction direction : Direction.values()) {
                BlockPos blockPos2 = blockPos.relative(direction);
                BlockState blockState = world.getBlockState(blockPos2);
                FluidState fluidState = world.getFluidState(blockPos2);
                Material material = blockState.getMaterial();
                if (fluidState.is(FluidTags.WATER)) {
                    if (blockState.getBlock() instanceof BucketPickup && !((BucketPickup) blockState.getBlock()).pickupBlock(
                                                                                                                        world,
                                                                                                                        blockPos2,
                                                                                                                        blockState
                                                                                                                )
                                                                                                                .isEmpty()) {
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockPos2, j + 1));
                        }
                    } else if (blockState.getBlock() instanceof LiquidBlock) {
                        world.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockPos2, j + 1));
                        }
                    } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                        BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(blockPos2) : null;
                        dropResources(blockState, world, blockPos2, blockEntity);
                        world.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockPos2, j + 1));
                        }
                    }
                }
            }

            if (i > 64) {
                break;
            }
        }

        return i > 0;
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape(
            BlockState blockState,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CollisionContext collisionContext
    ) {
        return SHAPE;
    }

    static {
        SHAPE = Shapes.or(
                Shapes.or(box(0, 0, 0, 16, 6, 6), box(0, 0, 10, 16, 6, 16),
                        Shapes.or(box(0, 10, 0, 16, 16, 6), box(0, 10, 10, 16, 16, 16)),

                        Shapes.or(box(0, 0, 0, 6, 6, 16), box(10, 0, 0, 16, 6, 16)),
                        Shapes.or(box(0, 10, 0, 6, 16, 16), box(10, 10, 0, 16, 16, 16)),

                        Shapes.or(box(0, 0, 0, 6, 16, 6), box(10, 0, 0, 16, 16, 6)),
                        Shapes.or(box(0, 0, 10, 6, 16, 16), box(10, 0, 10, 16, 16, 16))
                ));
    }
}
