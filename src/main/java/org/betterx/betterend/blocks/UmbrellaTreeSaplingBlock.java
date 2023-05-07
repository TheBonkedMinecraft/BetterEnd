package org.betterx.betterend.blocks;

import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.UmbrellaTreeFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class UmbrellaTreeSaplingBlock extends PottableFeatureSapling<UmbrellaTreeFeature, NoneFeatureConfiguration> {
    public UmbrellaTreeSaplingBlock() {
        super((state) -> EndFeatures.UMBRELLA_TREE.configuredFeature);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(EndBlocks.JUNGLE_MOSS);
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canPlantOn(Block block) {
        return block == EndBlocks.JUNGLE_MOSS;
    }
}
