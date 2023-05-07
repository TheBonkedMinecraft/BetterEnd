package org.betterx.betterend.integration.emi;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.recipe.builders.InfusionRecipe;
import org.betterx.ui.ColorUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.RecipeManager;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class EMIInfusionRecipe implements EmiRecipe {
    public final static EmiTexture BACKGROUND = new EmiTexture(
            BetterEnd.makeID("textures/gui/infusion.png"),
            0, 0,
            84, 84, 84, 84, 84, 84
    );

    public final Component[] ORIENTATIONS = {
            Component.translatable("betterend.infusion.north").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.north_east").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.east").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.south_east").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.south").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.south_west").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.west").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
            Component.translatable("betterend.infusion.north_west").setStyle(Style.EMPTY.withColor(ColorUtil.GRAY)),
    };
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIInfusionRecipe(InfusionRecipe recipe) {
        this.id = recipe.getId();
        this.input = recipe.getIngredients().stream().map(i -> EmiIngredient.of(i)).toList();
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }

    static void addAllRecipes(EmiRegistry emiRegistry, RecipeManager manager) {
        org.betterx.bclib.integration.emi.EMIPlugin.addAllRecipes(
                emiRegistry, manager, BetterEnd.LOGGER,
                InfusionRecipe.TYPE, EMIInfusionRecipe::new
        );
    }


    @Override
    public EmiRecipeCategory getCategory() {
        return EMIPlugin.INFUSION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 4 + 10 + 84 + 68;
    }

    @Override
    public int getDisplayHeight() {
        return 4 + 20 + 84;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        final int radius = 36;
        final int halfSize = 9;
        final int left = 10;
        final int top = 17;

        final int cx = left + 84 / 2;
        final int cy = top + 84 / 2;

        final int right = left + 84;
        final int bottom = top + 84;
        widgets.addTexture(BACKGROUND, left, top);
        // Add an arrow texture to indicate processing
        widgets.addTexture(EmiTexture.EMPTY_ARROW, right + 10, cy - 8);

        // Adds an input slot on the left
        widgets.add(new TransparentSlotWidget(input.get(0), cx - halfSize, cy - halfSize));

        FormattedCharSequence str = FormattedCharSequence.forward("N", Style.EMPTY);
        widgets.addText(str, cx - Minecraft.getInstance().font.width(str) / 2, 4, ColorUtil.WHITE, true);
        double a = Math.PI;
        for (int i = 1; i < input.size(); i++) {
            widgets.add(new TransparentSlotWidget(
                    input.get(i),
                    cx - halfSize + (int) (Math.sin(a) * radius),
                    cy - halfSize + (int) (Math.cos(a) * radius)
            )).appendTooltip(ORIENTATIONS[i - 1]);
            a -= Math.PI / 4;
        }

        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgets.addSlot(output.get(0), right + 40, cy - (halfSize + 4)).output(true).recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }
}
