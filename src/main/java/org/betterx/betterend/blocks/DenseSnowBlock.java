package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class DenseSnowBlock extends BaseBlock {
    public DenseSnowBlock() {
        super(FabricBlockSettings.of(Material.SNOW).strength(0.2F).sound(SoundType.SNOW));
    }
}
