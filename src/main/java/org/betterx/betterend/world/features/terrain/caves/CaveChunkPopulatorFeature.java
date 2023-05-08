package org.betterx.betterend.world.features.terrain.caves;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CaveChunkPopulatorFeature extends Feature<CaveChunkPopulatorFeatureConfig> {

    public CaveChunkPopulatorFeature() {
        super(CaveChunkPopulatorFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CaveChunkPopulatorFeatureConfig> featureConfig) {
        return false;
    }
}
