package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BasePlantWithAgeBlock;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.world.level.block.state.BlockState;

public abstract class EndPlantWithAgeBlock extends BasePlantWithAgeBlock {
    protected EndPlantWithAgeBlock() {
        super();
    }

    public EndPlantWithAgeBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }
}
