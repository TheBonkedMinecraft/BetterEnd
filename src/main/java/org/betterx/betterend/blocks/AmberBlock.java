package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class AmberBlock extends BaseBlock {
    public AmberBlock() {
        super(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).mapColor(MaterialColor.COLOR_YELLOW));
    }
}
