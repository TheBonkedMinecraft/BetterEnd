package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class EndWoodenComplexMaterial extends WoodenComplexMaterial {
    private Block bark;
    private Block log;

    public EndWoodenComplexMaterial(String name, MaterialColor woodColor, MaterialColor planksColor) {
        super(BetterEnd.MOD_ID, name, name, woodColor, planksColor);
    }

    public EndWoodenComplexMaterial init() {
        return (EndWoodenComplexMaterial) super.init(
                EndBlocks.getBlockRegistry(),
                EndItems.getItemRegistry(),
                Configs.RECIPE_CONFIG
        );
    }

    public boolean isTreeLog(Block block) {
        return block == getLog() || block == getBark();
    }

    public boolean isTreeLog(BlockState state) {
        return isTreeLog(state.getBlock());
    }

    public Block getLog() {
        if (log == null) {
            log = getBlock("log");
        }
        return log;
    }

    public Block getBark() {
        if (bark == null) {
            bark = getBlock("bark");
        }
        return bark;
    }
}