package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BaseWallPlantBlock;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.world.level.block.state.BlockState;

public class EndWallPlantBlock extends BaseWallPlantBlock {
    public EndWallPlantBlock() {
    }

    public EndWallPlantBlock(int light) {
        super(light);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }
}
