package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.EndPlantBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.state.BlockState;

public class ChorusGrassBlock extends EndPlantBlock {
    public ChorusGrassBlock() {
        super(true);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.getBlock() == EndBlocks.CHORUS_NYLIUM;
    }
}
