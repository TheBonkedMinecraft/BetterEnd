package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.EndPlantBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainPlantBlock extends EndPlantBlock {
    private final Block[] ground;

    public TerrainPlantBlock(Block... ground) {
        super(true);
        this.ground = ground;
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        for (Block block : ground) {
            if (state.is(block)) {
                return true;
            }
        }
        return false;
    }
}
