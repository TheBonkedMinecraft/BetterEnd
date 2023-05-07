package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.FeatureSaplingBlock;
import org.betterx.betterend.interfaces.PottablePlant;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class PottableFeatureSapling<F extends Feature<FC>, FC extends FeatureConfiguration> extends FeatureSaplingBlock<F, FC> implements PottablePlant {

    public PottableFeatureSapling(FeatureSupplier<F, FC> featureSupplier) {
        super(featureSupplier);
    }

    public PottableFeatureSapling(int light, FeatureSupplier<F, FC> featureSupplier) {
        super(light, featureSupplier);
    }
}
