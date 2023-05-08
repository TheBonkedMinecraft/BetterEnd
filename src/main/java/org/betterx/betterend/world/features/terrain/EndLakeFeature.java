package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndLakeFeature extends DefaultFeature {

    public EndLakeFeature() {}

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
       return false;
    }
}
