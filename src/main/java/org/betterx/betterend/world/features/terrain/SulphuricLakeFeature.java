package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.EndBlockProperties;
import org.betterx.betterend.blocks.SulphurCrystalBlock;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.util.GlobalState;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

import com.google.common.collect.Sets;

import java.util.Set;

public class SulphuricLakeFeature extends DefaultFeature {
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        BlockPos blockPos = featureConfig.origin();
        final WorldGenLevel world = featureConfig.level();
        blockPos = getPosOnSurfaceWG(world, blockPos);

        if (blockPos.getY() < 57) {
            return false;
        }

        final RandomSource random = featureConfig.random();
        final MutableBlockPos POS = GlobalState.stateForThread().POS;
        double radius = MHelper.randRange(10.0, 20.0, random);
        int dist2 = MHelper.floor(radius * 1.5);

        int minX = blockPos.getX() - dist2;
        int maxX = blockPos.getX() + dist2;
        int minZ = blockPos.getZ() - dist2;
        int maxZ = blockPos.getZ() + dist2;

        Set<BlockPos> brimstone = Sets.newHashSet();
        for (int x = minX; x <= maxX; x++) {
            POS.setX(x);
            int x2 = x - blockPos.getX();
            x2 *= x2;
            for (int z = minZ; z <= maxZ; z++) {
                POS.setZ(z);
                int z2 = z - blockPos.getZ();
                z2 *= z2;
                double r = radius * (NOISE.eval(x * 0.2, z * 0.2) * 0.25 + 0.75);
                double r2 = r * 1.5;
                r *= r;
                r2 *= r2;
                int dist = x2 + z2;
                if (dist <= r) {
                    POS.setY(getYOnSurface(world, x, z) - 1);
                    if (world.getBlockState(POS).is(CommonBlockTags.GEN_END_STONES)) {
                        if (isBorder(world, POS)) {
                            if (random.nextInt(8) > 0) {
                                brimstone.add(POS.immutable());
                                if (random.nextBoolean()) {
                                    brimstone.add(POS.below());
                                    if (random.nextBoolean()) {
                                        brimstone.add(POS.below(2));
                                    }
                                }
                            } else {
                                if (!isAbsoluteBorder(world, POS)) {
                                    BlocksHelper.setWithoutUpdate(world, POS, Blocks.WATER);
                                    //world.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 2);
                                    world.scheduleTick(POS, Fluids.WATER, 0);
                                    brimstone.add(POS.below());
                                    if (random.nextBoolean()) {
                                        brimstone.add(POS.below(2));
                                        if (random.nextBoolean()) {
                                            brimstone.add(POS.below(3));
                                        }
                                    }
                                } else {
                                    brimstone.add(POS.immutable());
                                    if (random.nextBoolean()) {
                                        brimstone.add(POS.below());
                                    }
                                }
                            }
                        } else {
                            BlocksHelper.setWithoutUpdate(world, POS, Blocks.WATER);
                            brimstone.remove(POS);
                            for (Direction dir : BlocksHelper.HORIZONTAL) {
                                BlockPos offseted = POS.relative(dir);
                                if (world.getBlockState(offseted).is(CommonBlockTags.GEN_END_STONES)) {
                                    brimstone.add(offseted);
                                }
                            }
                            if (isDeepWater(world, POS)) {
                                BlocksHelper.setWithoutUpdate(world, POS.move(Direction.DOWN), Blocks.WATER);
                                brimstone.remove(POS);
                                for (Direction dir : BlocksHelper.HORIZONTAL) {
                                    BlockPos offseted = POS.relative(dir);
                                    if (world.getBlockState(offseted).is(CommonBlockTags.GEN_END_STONES)) {
                                        brimstone.add(offseted);
                                    }
                                }
                            }
                            brimstone.add(POS.below());
                            if (random.nextBoolean()) {
                                brimstone.add(POS.below(2));
                                if (random.nextBoolean()) {
                                    brimstone.add(POS.below(3));
                                }
                            }
                        }
                    }
                } else if (dist < r2) {
                    POS.setY(getYOnSurface(world, x, z) - 1);
                    if (world.getBlockState(POS).is(CommonBlockTags.GEN_END_STONES)) {
                        brimstone.add(POS.immutable());
                        if (random.nextBoolean()) {
                            brimstone.add(POS.below());
                            if (random.nextBoolean()) {
                                brimstone.add(POS.below(2));
                            }
                        }
                    }
                }
            }
        }

        brimstone.forEach((bpos) -> {
            placeBrimstone(world, bpos, random);
        });

        return true;
    }

    private boolean isBorder(WorldGenLevel world, BlockPos pos) {
        int y = pos.getY() + 1;
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            if (getYOnSurface(world, pos.getX() + dir.getStepX(), pos.getZ() + dir.getStepZ()) < y) {
                return true;
            }
        }
        return false;
    }

    private boolean isAbsoluteBorder(WorldGenLevel world, BlockPos pos) {
        int y = pos.getY() - 2;
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            if (getYOnSurface(world, pos.getX() + dir.getStepX() * 3, pos.getZ() + dir.getStepZ() * 3) < y) {
                return true;
            }
        }
        return false;
    }

    private boolean isDeepWater(WorldGenLevel world, BlockPos pos) {
        int y = pos.getY() + 1;
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            if (getYOnSurface(world, pos.getX() + dir.getStepX(), pos.getZ() + dir.getStepZ()) < y || getYOnSurface(
                    world,
                    pos.getX() + dir.getStepX() * 2,
                    pos.getZ() + dir.getStepZ() * 2
            ) < y || getYOnSurface(
                    world,
                    pos.getX() + dir.getStepX() * 3,
                    pos.getZ() + dir.getStepZ() * 3
            ) < y) {
                return false;
            }
        }
        return true;
    }

    private void placeBrimstone(WorldGenLevel world, BlockPos pos, RandomSource random) {
        BlockState state = getBrimstone(world, pos);
        BlocksHelper.setWithoutUpdate(world, pos, state);
        if (state.getValue(EndBlockProperties.ACTIVE)) {
            makeShards(world, pos, random);
        }
    }

    private BlockState getBrimstone(WorldGenLevel world, BlockPos pos) {
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            if (world.getBlockState(pos.relative(dir)).is(Blocks.WATER)) {
                return EndBlocks.BRIMSTONE.defaultBlockState().setValue(EndBlockProperties.ACTIVE, true);
            }
        }
        return EndBlocks.BRIMSTONE.defaultBlockState();
    }

    private void makeShards(WorldGenLevel world, BlockPos pos, RandomSource random) {
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            BlockPos side;
            if (random.nextInt(16) == 0 && world.getBlockState((side = pos.relative(dir))).is(Blocks.WATER)) {
                BlockState state = EndBlocks.SULPHUR_CRYSTAL.defaultBlockState()
                                                            .setValue(SulphurCrystalBlock.WATERLOGGED, true)
                                                            .setValue(SulphurCrystalBlock.FACING, dir)
                                                            .setValue(SulphurCrystalBlock.AGE, random.nextInt(3));
                BlocksHelper.setWithoutUpdate(world, side, state);
            }
        }
    }
}
