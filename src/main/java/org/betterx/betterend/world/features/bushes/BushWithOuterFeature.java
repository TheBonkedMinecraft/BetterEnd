package org.betterx.betterend.world.features.bushes;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.sdf.SDF;
import org.betterx.bclib.sdf.operator.SDFDisplacement;
import org.betterx.bclib.sdf.operator.SDFScale3D;
import org.betterx.bclib.sdf.operator.SDFSubtraction;
import org.betterx.bclib.sdf.operator.SDFTranslate;
import org.betterx.bclib.sdf.primitive.SDFSphere;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;

public class BushWithOuterFeature extends Feature<BushWithOuterFeatureConfig> {
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Function<BlockState, Boolean> REPLACE;


    public BushWithOuterFeature() {
        super(BushWithOuterFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<BushWithOuterFeatureConfig> featureConfig) {
        final RandomSource random = featureConfig.random();
        final BlockPos pos = featureConfig.origin();
        BushWithOuterFeatureConfig cfg = featureConfig.config();
        BlockState outer_leaves = cfg.outer_leaves.getState(random, pos);
        Block leaves = cfg.leaves.getState(random, pos).getBlock();
        BlockState stem = cfg.stem.getState(random, pos);

        final WorldGenLevel world = featureConfig.level();
        if (!world.getBlockState(pos.below()).is(CommonBlockTags.END_STONES) && !world.getBlockState(pos.above())
                                                                                      .is(CommonBlockTags.END_STONES))
            return false;

        float radius = MHelper.randRange(1.8F, 3.5F, random);
        OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
        SDF sphere = new SDFSphere().setRadius(radius).setBlock(leaves);
        sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
        sphere = new SDFDisplacement().setFunction((vec) -> (float) noise.eval(
                vec.x() * 0.2,
                vec.y() * 0.2,
                vec.z() * 0.2
        ) * 3).setSource(sphere);
        sphere = new SDFDisplacement().setFunction((vec) -> MHelper.randRange(-2F, 2F, random)).setSource(sphere);
        sphere = new SDFSubtraction().setSourceA(sphere)
                                     .setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
        sphere.setReplaceFunction(REPLACE);
        sphere.addPostProcess((info) -> {
            if (info.getState().getBlock() instanceof LeavesBlock) {
                int distance = info.getPos().distManhattan(pos);
                if (distance < 7) {
                    return info.getState().setValue(LeavesBlock.DISTANCE, distance);
                } else {
                    return DefaultFeature.AIR;
                }
            }
            return info.getState();
        }).addPostProcess((info) -> {
            if (info.getState().getBlock() instanceof LeavesBlock) {
                MHelper.shuffle(DIRECTIONS, random);
                for (Direction dir : DIRECTIONS) {
                    if (info.getState(dir).isAir()) {
                        info.setBlockPos(
                                info.getPos().relative(dir),
                                outer_leaves.setValue(BlockStateProperties.FACING, dir)
                        );
                    }
                }
            }
            return info.getState();
        });
        sphere.fillRecursive(world, pos);
        BlocksHelper.setWithoutUpdate(world, pos, stem);
        for (Direction d : Direction.values()) {
            BlockPos p = pos.relative(d);
            if (world.isEmptyBlock(p)) {
                if (leaves instanceof LeavesBlock) {
                    BlocksHelper.setWithoutUpdate(
                            world,
                            p,
                            leaves.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1)
                    );
                } else {
                    BlocksHelper.setWithoutUpdate(world, p, leaves.defaultBlockState());
                }
            }
        }

        return true;
    }

    static {
        REPLACE = (state) -> {
            if (state.getMaterial().equals(Material.PLANT)) {
                return true;
            }
            return state.getMaterial().isReplaceable();
        };
    }
}
