package org.betterx.betterend.integration;

import org.betterx.bclib.integration.ModIntegration;
import org.betterx.betterend.blocks.HydraluxPetalColoredBlock;
import org.betterx.betterend.complexmaterials.ColoredMaterial;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.ui.ColorUtil;

import net.minecraft.world.level.ItemLike;

import com.google.common.collect.Maps;

import java.awt.*;
import java.util.Map;

public class FlamboyantRefabricatedIntegration extends ModIntegration {
    public FlamboyantRefabricatedIntegration() {
        super("flamboyant");
    }

    @Override
    public void init() {
        Map<Integer, String> colors = Maps.newHashMap();
        Map<Integer, ItemLike> dyes = Maps.newHashMap();

        addColor("fead1d", "amber", colors, dyes);
        addColor("bd9a5f", "beige", colors, dyes);
        addColor("edeada", "cream", colors, dyes);
        addColor("33430e", "dark_green", colors, dyes);
        addColor("639920", "forest_green", colors, dyes);
        addColor("f0618c", "hot_pink", colors, dyes);
        addColor("491c7b", "indigo", colors, dyes);
        addColor("65291b", "maroon", colors, dyes);
        addColor("2c3969", "navy", colors, dyes);
        addColor("827c17", "olive", colors, dyes);
        addColor("7bc618", "pale_green", colors, dyes);
        addColor("f4a4bd", "pale_pink", colors, dyes);
        addColor("f8d45a", "pale_yellow", colors, dyes);
        addColor("6bb1cf", "sky_blue", colors, dyes);
        addColor("6e8c9c", "slate_gray", colors, dyes);
        addColor("b02454", "violet", colors, dyes);

        new ColoredMaterial(HydraluxPetalColoredBlock::new, EndBlocks.HYDRALUX_PETAL_BLOCK, colors, dyes, true);
    }

    private void addColor(String hex, String name, Map<Integer, String> colors, Map<Integer, ItemLike> dyes) {
        int color = ColorUtil.color(hex);
        colors.put(color, name);
        dyes.put(color, getItem(name + "_dye"));

        System.out.println(name + " " + color + " " + new Color(color));
    }
}
