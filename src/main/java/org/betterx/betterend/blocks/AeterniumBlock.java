package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.interfaces.CustomItemProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class AeterniumBlock extends BaseBlock implements CustomItemProvider {
    public AeterniumBlock() {
        super(FabricBlockSettings
                .of(Material.METAL, MaterialColor.COLOR_GRAY)
                .hardness(65F)
                .resistance(1200F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.NETHERITE_BLOCK)
        );
    }

    @Override
    public BlockItem getCustomItem(ResourceLocation blockID, Item.Properties settings) {
        return new BlockItem(this, settings.fireResistant());
    }
}
