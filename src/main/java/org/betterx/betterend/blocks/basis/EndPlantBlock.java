package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BasePlantBlock;
import org.betterx.betterend.interfaces.PottablePlant;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EndPlantBlock extends BasePlantBlock implements PottablePlant {
    protected EndPlantBlock() {
        super();
    }

    protected EndPlantBlock(boolean replaceable) {
        super(replaceable);
    }

    protected EndPlantBlock(int light) {
        super(light);
    }

    protected EndPlantBlock(Properties props) {
        super(props);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }

    @Override
    public boolean canPlantOn(Block block) {
        return isTerrain(block.defaultBlockState());
    }

    @Override
    public boolean canBePotted() {
        return getStateDefinition().getProperties().isEmpty();
    }
}
