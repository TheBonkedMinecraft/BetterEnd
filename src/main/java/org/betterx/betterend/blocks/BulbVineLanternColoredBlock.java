package org.betterx.betterend.blocks;

import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.ui.ColorUtil;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class BulbVineLanternColoredBlock extends BulbVineLanternBlock implements CustomColorProvider {
    public BulbVineLanternColoredBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockColor getProvider() {
        return (state, world, pos, tintIndex) -> getColor();
    }

    @Override
    public ItemColor getItemProvider() {
        return (stack, tintIndex) -> getColor();
    }

    private int getColor() {
        int color = BlocksHelper.getBlockColor(this);
        int b = (color & 255);
        int g = ((color >> 8) & 255);
        int r = ((color >> 16) & 255);
        float[] hsv = ColorUtil.RGBtoHSB(r, g, b, new float[3]);
        return ColorUtil.HSBtoRGB(hsv[0], hsv[1], hsv[1] > 0.2 ? 1 : hsv[2]);
    }

    @Override
    protected String getGlowTexture() {
        return "bulb_vine_lantern_overlay";
    }
}
