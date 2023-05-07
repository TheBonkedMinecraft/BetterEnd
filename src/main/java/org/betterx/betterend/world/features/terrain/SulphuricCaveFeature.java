package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.EndBlockProperties;
import org.betterx.betterend.blocks.SulphurCrystalBlock;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.util.BlockFixer;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import com.google.common.collect.Sets;

import java.util.Set;

public class SulphuricCaveFeature extends DefaultFeature {
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
    private static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final Direction[] HORIZONTAL = BlocksHelper.makeHorizontal();

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        final RandomSource random = featureConfig.random();
        BlockPos pos = featureConfig.origin();
        final WorldGenLevel world = featureConfig.level();
        int radius = MHelper.randRange(10, 30, random);

        int top = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        MutableBlockPos bpos = new MutableBlockPos();
        bpos.setX(pos.getX());
        bpos.setZ(pos.getZ());
        bpos.setY(top - 1);

        BlockState state = world.getBlockState(bpos);
        while (!state.is(CommonBlockTags.GEN_END_STONES) && bpos.getY() > 5) {
            bpos.setY(bpos.getY() - 1);
            state = world.getBlockState(bpos);
        }
        if (bpos.getY() < 10) {
            return false;
        }
        top = (int) (bpos.getY() - (radius * 1.3F + 5));

        while (state.is(CommonBlockTags.GEN_END_STONES) || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
            bpos.setY(bpos.getY() - 1);
            state = world.getBlockState(bpos);
        }
        int bottom = (int) (bpos.getY() + radius * 1.3F + 5);

        if (top <= bottom) {
            return false;
        }

        MutableBlockPos mut = new MutableBlockPos();
        pos = new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());

        OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, pos.getX(), pos.getZ()));

        int x1 = pos.getX() - radius - 5;
        int z1 = pos.getZ() - radius - 5;
        int x2 = pos.getX() + radius + 5;
        int z2 = pos.getZ() + radius + 5;
        int y1 = MHelper.floor(pos.getY() - (radius + 5) / 1.6);
        int y2 = MHelper.floor(pos.getY() + (radius + 5) / 1.6);

        double hr = radius * 0.75;
        double nr = radius * 0.25;

        Set<BlockPos> brimstone = Sets.newHashSet();
        BlockState rock = EndBlocks.SULPHURIC_ROCK.stone.defaultBlockState();
        int waterLevel = pos.getY() + MHelper.randRange(MHelper.floor(radius * 0.8), radius, random);
        for (int x = x1; x <= x2; x++) {
            int xsq = x - pos.getX();
            xsq *= xsq;
            mut.setX(x);
            for (int z = z1; z <= z2; z++) {
                int zsq = z - pos.getZ();
                zsq *= zsq;
                mut.setZ(z);
                for (int y = y1; y <= y2; y++) {
                    int ysq = y - pos.getY();
                    ysq *= 1.6;
                    ysq *= ysq;
                    mut.setY(y);
                    double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
                    double r2 = r + 5;
                    double dist = xsq + ysq + zsq;
                    if (dist < r * r) {
                        state = world.getBlockState(mut);
                        if (isReplaceable(state)) {
                            BlocksHelper.setWithoutUpdate(world, mut, y < waterLevel ? WATER : CAVE_AIR);
                        }
                    } else if (dist < r2 * r2) {
                        state = world.getBlockState(mut);
                        if (state.is(CommonBlockTags.GEN_END_STONES) || state.is(Blocks.AIR)) {
                            double v = noise.eval(x * 0.1, y * 0.1, z * 0.1) + noise.eval(
                                    x * 0.03,
                                    y * 0.03,
                                    z * 0.03
                            ) * 0.5;
                            if (v > 0.4) {
                                brimstone.add(mut.immutable());
                            } else {
                                BlocksHelper.setWithoutUpdate(world, mut, rock);
                            }
                        }
                    }
                }
            }
        }
        brimstone.forEach((blockPos) -> {
            placeBrimstone(world, blockPos, random);
        });

        if (random.nextInt(4) == 0) {
            int count = MHelper.randRange(5, 20, random);
            for (int i = 0; i < count; i++) {
                mut.set(pos)
                   .move(
                           MHelper.floor(random.nextGaussian() * 2 + 0.5),
                           0,
                           MHelper.floor(random.nextGaussian() * 2 + 0.5)
                   );
                int dist = MHelper.floor(3 - MHelper.length(
                        mut.getX() - pos.getX(),
                        mut.getZ() - pos.getZ()
                )) + random.nextInt(2);
                if (dist > 0) {
                    state = world.getBlockState(mut);
                    while (!state.getFluidState().isEmpty() || state.getMaterial().equals(Material.WATER_PLANT)) {
                        mut.setY(mut.getY() - 1);
                        state = world.getBlockState(mut);
                    }
                    if (state.is(CommonBlockTags.GEN_END_STONES) && !world.getBlockState(mut.above())
                                                                          .is(EndBlocks.HYDROTHERMAL_VENT)) {
                        for (int j = 0; j <= dist; j++) {
                            BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.SULPHURIC_ROCK.stone);
                            MHelper.shuffle(HORIZONTAL, random);
                            for (Direction dir : HORIZONTAL) {
                                BlockPos p = mut.relative(dir);
                                if (random.nextBoolean() && world.getBlockState(p).is(Blocks.WATER)) {
                                    BlocksHelper.setWithoutUpdate(
                                            world,
                                            p,
                                            EndBlocks.TUBE_WORM.defaultBlockState()
                                                               .setValue(HorizontalDirectionalBlock.FACING, dir)
                                    );
                                }
                            }
                            mut.setY(mut.getY() + 1);
                        }
                        BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.HYDROTHERMAL_VENT);
                        mut.setY(mut.getY() + 1);
                        state = world.getBlockState(mut);
                        while (state.is(Blocks.WATER)) {
                            BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.VENT_BUBBLE_COLUMN.defaultBlockState());
                            world.scheduleTick(
                                    mut.immutable(),
                                    EndBlocks.VENT_BUBBLE_COLUMN,
                                    MHelper.randRange(8, 32, random)
                            );
                            mut.setY(mut.getY() + 1);
                            state = world.getBlockState(mut);
                        }
                    }
                }
            }
        }

        BlockFixer.fixBlocks(world, new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));

        return true;
    }

    private boolean isReplaceable(BlockState state) {
        return state.is(CommonBlockTags.GEN_END_STONES) || state.is(EndBlocks.HYDROTHERMAL_VENT) || state.is(EndBlocks.VENT_BUBBLE_COLUMN) || state
                .is(EndBlocks.SULPHUR_CRYSTAL) || state.getMaterial().isReplaceable() || state.getMaterial()
                                                                                              .equals(Material.PLANT) || state
                .getMaterial()
                .equals(Material.WATER_PLANT) || state.getMaterial().equals(Material.LEAVES);
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
