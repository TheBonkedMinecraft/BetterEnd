package org.betterx.betterend.recipe;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class FurnaceRecipes {
    public static void register() {
        BCLRecipeBuilder.smelting(BetterEnd.makeID("end_lily_leaf_dried"), EndItems.END_LILY_LEAF_DRIED)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndItems.END_LILY_LEAF)
                        .build();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("end_glass"), Blocks.GLASS)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndBlocks.ENDSTONE_DUST)
                        .build();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("end_berry"), EndItems.SHADOW_BERRY_COOKED)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndItems.SHADOW_BERRY_RAW)
                        .buildFoodlike();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("end_fish"), EndItems.END_FISH_COOKED)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndItems.END_FISH_RAW)
                        .buildFoodlike();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("slime_ball"), Items.SLIME_BALL)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndBlocks.JELLYSHROOM_CAP_PURPLE)
                        .build();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("menger_sponge"), EndBlocks.MENGER_SPONGE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndBlocks.MENGER_SPONGE_WET)
                        .build();

        BCLRecipeBuilder.smelting(BetterEnd.makeID("chorus_mushroom"), EndItems.CHORUS_MUSHROOM_COOKED)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndItems.CHORUS_MUSHROOM_RAW)
                        .buildFoodlike();
        
        BCLRecipeBuilder.smelting(BetterEnd.makeID("bolux_mushroom"), EndItems.BOLUX_MUSHROOM_COOKED)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setInput(EndBlocks.BOLUX_MUSHROOM)
                        .buildFoodlike();
    }
}
