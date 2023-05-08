package org.betterx.betterend.world.features.terrain.caves;


import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public abstract class EndCaveFeature extends DefaultFeature {
    protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
    protected static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
    protected static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final Vec3i[] SPHERE;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        return false;
    }

    protected abstract Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, RandomSource random);

    protected boolean isWaterNear(WorldGenLevel world, BlockPos pos) {
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            if (!world.getFluidState(pos.relative(dir, 5)).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean biomeMissingCaves(WorldGenLevel world, BlockPos pos) {
        return false;
    }

    static {
        List<Vec3i> prePos = Lists.newArrayList();
        int radius = 5;
        int r2 = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            int x2 = x * x;
            for (int y = -radius; y <= radius; y++) {
                int y2 = y * y;
                for (int z = -radius; z <= radius; z++) {
                    int z2 = z * z;
                    if (x2 + y2 + z2 < r2) {
                        prePos.add(new Vec3i(x, y, z));
                    }
                }
            }
        }
        SPHERE = prePos.toArray(new Vec3i[]{});
    }
}
