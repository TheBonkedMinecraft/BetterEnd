package org.betterx.betterend.world.features.terrain;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FloatingSpireFeature extends SpireFeature {
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
       return false;
    }
}
