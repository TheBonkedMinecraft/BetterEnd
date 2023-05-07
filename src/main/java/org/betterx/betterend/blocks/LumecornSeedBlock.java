package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;

public class LumecornSeedBlock extends EndPlantWithAgeBlock {

    public LumecornSeedBlock() {
        super(basePlantSettings().offsetType(OffsetType.NONE));
    }

    @Override
    public void growAdult(WorldGenLevel world, RandomSource random, BlockPos pos) {
        ((Feature<NoneFeatureConfiguration>) (EndFeatures.LUMECORN.getFeature())).place(new FeaturePlaceContext<>(
                Optional.empty(),
                world,
                null,
                random,
                pos,
                null
        ));
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.END_MOSS);
    }
}
