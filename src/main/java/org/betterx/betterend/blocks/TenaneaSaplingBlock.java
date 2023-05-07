package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.TenaneaFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TenaneaSaplingBlock extends PottableFeatureSapling<TenaneaFeature, NoneFeatureConfiguration> {
    public TenaneaSaplingBlock() {
        super((state) -> EndFeatures.TENANEA.configuredFeature);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(EndBlocks.PINK_MOSS);
    }

    @Override
    public boolean canPlantOn(Block block) {
        return block == EndBlocks.PINK_MOSS;
    }
}
