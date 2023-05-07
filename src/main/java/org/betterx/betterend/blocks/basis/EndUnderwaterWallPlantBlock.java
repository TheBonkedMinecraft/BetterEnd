package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BaseUnderwaterWallPlantBlock;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.world.level.block.state.BlockState;

public class EndUnderwaterWallPlantBlock extends BaseUnderwaterWallPlantBlock {

    public EndUnderwaterWallPlantBlock() {
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }
}
