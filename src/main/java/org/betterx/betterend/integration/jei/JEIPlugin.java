package org.betterx.betterend.integration.jei;

import org.betterx.bclib.recipes.AlloyingRecipe;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IIngredientManager;

public class JEIPlugin implements IModPlugin {
    public final static ResourceLocation PLUGIN_ID = BetterEnd.makeID("jei_plugin");


    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);

        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new JEIAlloyingCategory(guiHelper));
        registration.addRecipeCategories(new JEIAlloyingFuelCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        var recipeManager = world.getRecipeManager();
        IIngredientManager ingredientManager = registration.getIngredientManager();

        IModPlugin.super.registerRecipes(registration);
        registration.addRecipes(JEIAlloyingCategory.TYPE, recipeManager.getAllRecipesFor(AlloyingRecipe.TYPE));
        registration.addRecipes(
                JEIAlloyingFuelCategory.FUEL_TYPE,
                JEIAlloyingFuelCategory.getFuelRecipes(ingredientManager)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(
                new ItemStack(EndBlocks.END_STONE_SMELTER),
                JEIAlloyingCategory.TYPE,
                JEIAlloyingFuelCategory.FUEL_TYPE
        );
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IModPlugin.super.registerRecipeTransferHandlers(registration);

    }
}
