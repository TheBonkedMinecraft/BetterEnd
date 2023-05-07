package org.betterx.betterend.blocks;

import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.blocks.basis.EndPlantBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class SmallAmaranitaBlock extends EndPlantBlock {
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 10, 12);

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.SANGNUM) || state.is(EndBlocks.MOSSY_OBSIDIAN) || state.is(EndBlocks.MOSSY_DRAGON_BONE);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos bigPos = growBig(world, pos);
        if (bigPos != null) {
            if (EndFeatures.GIGANTIC_AMARANITA.getFeature()
                                              .place(new FeaturePlaceContext<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>(
                                                      Optional.empty(),
                                                      world,
                                                      null,
                                                      random,
                                                      bigPos,
                                                      null
                                              ))) {
                replaceMushroom(world, bigPos);
                replaceMushroom(world, bigPos.south());
                replaceMushroom(world, bigPos.east());
                replaceMushroom(world, bigPos.south().east());
            }
            return;
        }
        EndFeatures.LARGE_AMARANITA.getFeature()
                                   .place(new FeaturePlaceContext<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>(
                                           Optional.empty(),
                                           world,
                                           null,
                                           random,
                                           pos,
                                           null
                                   ));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        Vec3 vec3d = state.getOffset(view, pos);
        return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
    }

    private BlockPos growBig(ServerLevel world, BlockPos pos) {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockPos p = pos.offset(x, 0, z);
                if (checkFrame(world, p)) {
                    return p;
                }
            }
        }
        return null;
    }

    private boolean checkFrame(ServerLevel world, BlockPos pos) {
        return world.getBlockState(pos).is(this) && world.getBlockState(pos.south()).is(this) && world.getBlockState(pos
                                                                                                              .east())
                                                                                                      .is(this) && world.getBlockState(
                pos.south().east()).is(this);
    }

    private void replaceMushroom(ServerLevel world, BlockPos pos) {
        if (world.getBlockState(pos).is(this)) {
            BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
        }
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextInt(8) == 0;
    }
}
