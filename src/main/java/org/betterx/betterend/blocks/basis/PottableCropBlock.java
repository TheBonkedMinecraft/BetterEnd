package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BaseCropBlock;
import org.betterx.betterend.interfaces.PottablePlant;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PottableCropBlock extends BaseCropBlock implements PottablePlant {
    private final Block[] terrain;

    public PottableCropBlock(Item drop, Block... terrain) {
        super(drop, terrain);
        this.terrain = terrain;
    }

    @Override
    public boolean canPlantOn(Block block) {
        for (Block ter : terrain) {
            if (block == ter) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPottedState() {
        return "age=3";
    }
}
