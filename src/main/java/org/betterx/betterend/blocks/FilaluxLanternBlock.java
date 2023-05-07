package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.interfaces.tools.AddMineableAxe;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class FilaluxLanternBlock extends BaseBlock implements AddMineableAxe {
    public FilaluxLanternBlock() {
        super(FabricBlockSettings.of(Material.WOOD)
                                 .luminance(15)
                                 .sound(SoundType.WOOD));
    }
}
