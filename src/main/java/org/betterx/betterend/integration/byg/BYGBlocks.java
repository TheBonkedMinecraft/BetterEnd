package org.betterx.betterend.integration.byg;

import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.betterend.blocks.basis.EndWallPlantBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.Block;

public class BYGBlocks {
    public static final Block IVIS_MOSS = EndBlocks.registerBlock("ivis_moss", new EndWallPlantBlock());
    public static final Block NIGHTSHADE_MOSS = EndBlocks.registerBlock("nightshade_moss", new EndWallPlantBlock());

    public static final Block IVIS_VINE = EndBlocks.registerBlock("ivis_vine", new BaseVineBlock());

    public static void register() {
    }
}
