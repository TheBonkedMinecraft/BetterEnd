package org.betterx.betterend.blocks;

import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;
import org.betterx.betterend.blocks.basis.LitPillarBlock;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class SmaragdantCrystalBlock extends LitPillarBlock implements AddMineablePickaxe {
    public SmaragdantCrystalBlock() {
        super(FabricBlockSettings.of(Material.GLASS)
                                 .luminance(15)
                                 .hardness(1F)
                                 .resistance(1F)
                                 .noOcclusion()
                                 .sound(SoundType.AMETHYST));
    }
}
