package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;

import net.minecraft.world.level.block.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class MissingTileBlock extends BaseBlock {
    public MissingTileBlock() {
        super(FabricBlockSettings.copyOf(Blocks.END_STONE));
    }
}
