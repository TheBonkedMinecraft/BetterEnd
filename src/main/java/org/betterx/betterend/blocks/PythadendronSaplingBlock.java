package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.PythadendronTreeFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PythadendronSaplingBlock extends PottableFeatureSapling<PythadendronTreeFeature, NoneFeatureConfiguration> {
    public PythadendronSaplingBlock() {
        super((state) -> EndFeatures.PYTHADENDRON_TREE.configuredFeature);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(EndBlocks.CHORUS_NYLIUM);
    }

    @Override
    public boolean canPlantOn(Block block) {
        return block == EndBlocks.CHORUS_NYLIUM;
    }
}
