package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MaterialColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

public class ColoredMaterial {
    private static final Map<Integer, ItemLike> DYES = Maps.newHashMap();
    private static final Map<Integer, String> COLORS = Maps.newHashMap();
    private final Map<Integer, Block> colors = Maps.newHashMap();

    public ColoredMaterial(Function<FabricBlockSettings, Block> constructor, Block source, boolean craftEight) {
        this(constructor, source, COLORS, DYES, craftEight);
    }

    public ColoredMaterial(
            Function<FabricBlockSettings, Block> constructor,
            Block source,
            Map<Integer, String> colors,
            Map<Integer, ItemLike> dyes,
            boolean craftEight
    ) {
        String id = Registry.BLOCK.getKey(source).getPath();
        colors.forEach((color, name) -> {
            String blockName = id + "_" + name;
            Block block = constructor.apply(FabricBlockSettings.copyOf(source).mapColor(MaterialColor.COLOR_BLACK));
            EndBlocks.registerBlock(blockName, block);
            if (craftEight) {
                BCLRecipeBuilder.crafting(BetterEnd.makeID(blockName), block)
                                .checkConfig(Configs.RECIPE_CONFIG)
                                .setOutputCount(8)
                                .setShape("###", "#D#", "###")
                                .addMaterial('#', source)
                                .addMaterial('D', dyes.get(color))
                                .build();
            } else {
                BCLRecipeBuilder.crafting(BetterEnd.makeID(blockName), block)
                                .checkConfig(Configs.RECIPE_CONFIG)
                                .setList("#D")
                                .addMaterial('#', source)
                                .addMaterial('D', dyes.get(color))
                                .build();
            }
            this.colors.put(color, block);
            BlocksHelper.addBlockColor(block, color);
        });
    }

    public Block getByColor(DyeColor color) {
        return colors.get(color.getMaterialColor().col);
    }

    public Block getByColor(int color) {
        return colors.get(color);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            int colorRGB = color.getMaterialColor().col;
            COLORS.put(colorRGB, color.getName());
            DYES.put(colorRGB, DyeItem.byColor(color));
        }
    }
}
