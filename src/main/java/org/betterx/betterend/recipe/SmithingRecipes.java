package org.betterx.betterend.recipe;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.item.Items;

public class SmithingRecipes {

    public static void register() {
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_sword_handle"), EndItems.AETERNIUM_SWORD_HANDLE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.ingot)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_sword"), EndItems.AETERNIUM_SWORD)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_SWORD_BLADE)
                        .setAddition(EndItems.AETERNIUM_SWORD_HANDLE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_pickaxe"), EndItems.AETERNIUM_PICKAXE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_PICKAXE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_axe"), EndItems.AETERNIUM_AXE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_AXE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_shovel"), EndItems.AETERNIUM_SHOVEL)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_SHOVEL_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_hoe"), EndItems.AETERNIUM_HOE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_HOE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_hammer"), EndItems.AETERNIUM_HAMMER)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.AETERNIUM_HAMMER_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("netherite_hammer"), EndItems.NETHERITE_HAMMER)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndItems.DIAMOND_HAMMER)
                        .setAddition(Items.NETHERITE_INGOT)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_helmet"), EndItems.AETERNIUM_HELMET)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.helmet)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_chestplate"), EndItems.AETERNIUM_CHESTPLATE)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.chestplate)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_leggings"), EndItems.AETERNIUM_LEGGINGS)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.leggings)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_boots"), EndItems.AETERNIUM_BOOTS)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.boots)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("thallasium_anvil_updrade"), EndBlocks.TERMINITE.anvilBlock.asItem())
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.THALLASIUM.anvilBlock.asItem())
                        .setAddition(EndBlocks.TERMINITE.block)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("terminite_anvil_updrade"), EndBlocks.AETERNIUM_ANVIL.asItem())
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(EndBlocks.TERMINITE.anvilBlock.asItem())
                        .setAddition(EndItems.AETERNIUM_INGOT)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("armored_elytra"), EndItems.ARMORED_ELYTRA)
                        .checkConfig(Configs.RECIPE_CONFIG)
                        .setBase(Items.ELYTRA)
                        .setAddition(EndItems.AETERNIUM_INGOT)
                        .build();
    }
}
