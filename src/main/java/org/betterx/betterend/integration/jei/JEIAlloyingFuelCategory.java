package org.betterx.betterend.integration.jei;

import org.betterx.bclib.recipes.AlloyingRecipe;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.ui.layout.values.Rectangle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.Unmodifiable;


public class JEIAlloyingFuelCategory implements IRecipeCategory<IJeiFuelingRecipe> {
    public static final RecipeType FUEL_TYPE = RecipeType.create(
            BetterEnd.MOD_ID,
            AlloyingRecipe.GROUP + "_fuel",
            IJeiFuelingRecipe.class
    );

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedFlames;
    private final Rectangle textArea;

    public static List<IJeiFuelingRecipe> getFuelRecipes(IIngredientManager ingredientManager) {
        return ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK).stream()
                                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                                    if (EndStoneSmelterBlockEntity.canUseAsFuel(stack)) {
                                        final int time = EndStoneSmelterBlockEntity.getFuelTime(stack);
                                        if (time > 0) {
                                            final List<ItemStack> inputs = List.of(stack);
                                            consumer.accept(new IJeiFuelingRecipe() {
                                                @Override
                                                public @Unmodifiable List<ItemStack> getInputs() {
                                                    return inputs;
                                                }

                                                @Override
                                                public int getBurnTime() {
                                                    return time;
                                                }
                                            });
                                        }
                                    }

                                })
                                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                                .toList();
    }

    public JEIAlloyingFuelCategory(IGuiHelper guiHelper) {

        // width of the recipe depends on the text, which is different in each language
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        Component maxSmeltCountText = createSmeltCountText(10000000 * 200);
        int maxStringWidth = fontRenderer.width(maxSmeltCountText.getString());
        int backgroundHeight = 34;
        int textPadding = 20;

        background = guiHelper.drawableBuilder(JEIAlloyingCategory.RECIPE_GUI_VANILLA, 0, 134, 18, backgroundHeight)
                              .addPadding(0, 0, 0, textPadding + maxStringWidth)
                              .build();

        textArea = new Rectangle(20, 0, textPadding + maxStringWidth, backgroundHeight);

        icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(EndBlocks.END_STONE_SMELTER)
        );
        ;
        localizedName = Component.translatable("gui.jei.category.fuel");

        this.cachedFlames = CacheBuilder.newBuilder()
                                        .maximumSize(25)
                                        .build(new CacheLoader<>() {
                                            @Override
                                            public IDrawableAnimated load(Integer burnTime) {
                                                return guiHelper.drawableBuilder(
                                                                        JEIAlloyingCategory.RECIPE_GUI_VANILLA,
                                                                        82,
                                                                        114,
                                                                        14,
                                                                        14
                                                                )
                                                                .buildAnimated(
                                                                        burnTime,
                                                                        IDrawableAnimated.StartDirection.TOP,
                                                                        true
                                                                );
                                            }
                                        });
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
        return FUEL_TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17)
               .addItemStacks(recipe.getInputs());
    }

    @Override
    public void draw(
            IJeiFuelingRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            PoseStack poseStack,
            double mouseX,
            double mouseY
    ) {
        int burnTime = recipe.getBurnTime();
        IDrawableAnimated flame = cachedFlames.getUnchecked(burnTime);
        flame.draw(poseStack, 1, 0);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Component smeltCountText = createSmeltCountText(burnTime);
        int width = font.width(smeltCountText);
        int height = font.lineHeight;

        font.draw(
                poseStack,
                smeltCountText,
                this.textArea.left + (this.textArea.width - width) / 2,
                this.textArea.top + (this.textArea.height - height) / 2 + 1,
                0xFF808080
        );
    }

    private static Component createSmeltCountText(int burnTime) {
        if (burnTime == 200) {
            return Component.translatable("gui.jei.category.fuel.smeltCount.single");
        } else {
            NumberFormat numberInstance = NumberFormat.getNumberInstance();
            numberInstance.setMaximumFractionDigits(2);
            String smeltCount = numberInstance.format(burnTime / 200f);
            return Component.translatable("gui.jei.category.fuel.smeltCount", smeltCount);
        }
    }
}
