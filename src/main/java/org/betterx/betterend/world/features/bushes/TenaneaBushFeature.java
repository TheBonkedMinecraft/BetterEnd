package org.betterx.betterend.world.features.bushes;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.sdf.SDF;
import org.betterx.bclib.sdf.operator.SDFDisplacement;
import org.betterx.bclib.sdf.operator.SDFScale3D;
import org.betterx.bclib.sdf.operator.SDFSubtraction;
import org.betterx.bclib.sdf.operator.SDFTranslate;
import org.betterx.bclib.sdf.primitive.SDFSphere;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.basis.FurBlock;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

public class TenaneaBushFeature extends DefaultFeature {
    private static final Function<BlockState, Boolean> REPLACE;
    private static final Direction[] DIRECTIONS = Direction.values();

    public TenaneaBushFeature() {
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        final RandomSource random = featureConfig.random();
        final BlockPos pos = featureConfig.origin();
        final WorldGenLevel world = featureConfig.level();
        if (!world.getBlockState(pos.below()).is(CommonBlockTags.END_STONES)) return false;

        float radius = MHelper.randRange(1.8F, 3.5F, random);
        OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
        BlockState leaves = EndBlocks.TENANEA_LEAVES.defaultBlockState();
        SDF sphere = new SDFSphere().setRadius(radius).setBlock(leaves);
        sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
        sphere = new SDFDisplacement().setFunction((vec) -> (float) noise.eval(
                vec.x() * 0.2,
                vec.y() * 0.2,
                vec.z() * 0.2
        ) * 3).setSource(sphere);
        sphere = new SDFDisplacement().setFunction((vec) -> MHelper.randRange(-2F, 2F, random)).setSource(sphere);
        sphere = new SDFSubtraction().setSourceA(sphere)
                                     .setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
        sphere.setReplaceFunction(REPLACE);
        List<BlockPos> support = Lists.newArrayList();
        sphere.addPostProcess((info) -> {
            if (info.getState().getBlock() instanceof LeavesBlock) {
                int distance = info.getPos().distManhattan(pos);
                if (distance < 7) {
                    if (random.nextInt(4) == 0 && info.getStateDown().isAir()) {
                        BlockPos d = info.getPos().below();
                        support.add(d);
                    }

                    MHelper.shuffle(DIRECTIONS, random);
                    for (Direction d : DIRECTIONS) {
                        if (info.getState(d).isAir()) {
                            info.setBlockPos(
                                    info.getPos().relative(d),
                                    EndBlocks.TENANEA_OUTER_LEAVES.defaultBlockState().setValue(FurBlock.FACING, d)
                            );
                        }
                    }

                    return info.getState().setValue(LeavesBlock.DISTANCE, distance);
                } else {
                    return AIR;
                }
            }
            return info.getState();
        });
        sphere.fillRecursive(world, pos);
        BlockState stem = EndBlocks.TENANEA.getBark().defaultBlockState();
        BlocksHelper.setWithoutUpdate(world, pos, stem);
        for (Direction d : Direction.values()) {
            BlockPos p = pos.relative(d);
            if (world.isEmptyBlock(p)) {
                BlocksHelper.setWithoutUpdate(world, p, leaves.setValue(LeavesBlock.DISTANCE, 1));
            }
        }

        MutableBlockPos mut = new MutableBlockPos();
        BlockState top = EndBlocks.TENANEA_FLOWERS.defaultBlockState()
                                                  .setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP);
        BlockState middle = EndBlocks.TENANEA_FLOWERS.defaultBlockState()
                                                     .setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE);
        BlockState bottom = EndBlocks.TENANEA_FLOWERS.defaultBlockState()
                                                     .setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM);
        support.forEach((bpos) -> {
            BlockState state = world.getBlockState(bpos);
            if (state.isAir() || state.is(EndBlocks.TENANEA_OUTER_LEAVES)) {
                int count = MHelper.randRange(3, 8, random);
                mut.set(bpos);
                if (world.getBlockState(mut.above()).is(EndBlocks.TENANEA_LEAVES)) {
                    BlocksHelper.setWithoutUpdate(world, mut, top);
                    for (int i = 1; i < count; i++) {
                        mut.setY(mut.getY() - 1);
                        if (world.isEmptyBlock(mut.below())) {
                            BlocksHelper.setWithoutUpdate(world, mut, middle);
                        } else {
                            break;
                        }
                    }
                    BlocksHelper.setWithoutUpdate(world, mut, bottom);
                }
            }
        });

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
