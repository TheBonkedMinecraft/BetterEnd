package org.betterx.betterend.integration.jei;

import org.betterx.bclib.recipes.AlloyingRecipe;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.ModIds;
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

public class JEIAlloyingCategory implements IRecipeCategory<AlloyingRecipe> {
    public static final RecipeType TYPE = RecipeType.create(
            BetterEnd.MOD_ID,
            AlloyingRecipe.GROUP,
            AlloyingRecipe.class
    );
    public static final String TEXTURE_GUI_PATH = "textures/gui/";
    public static final String TEXTURE_GUI_VANILLA = TEXTURE_GUI_PATH + "gui_vanilla.png";
    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, TEXTURE_GUI_VANILLA);
    public static final int width = 116;
    public static final int height = 54;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableStatic addonSlot;
    protected final IDrawableAnimated animatedFlame;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public JEIAlloyingCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        background = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        title = Component.translatable(EndBlocks.END_STONE_SMELTER.getDescriptionId());
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EndBlocks.END_STONE_SMELTER));
        this.cachedArrows = CacheBuilder.newBuilder()
                                        .maximumSize(25)
                                        .build(new CacheLoader<>() {
                                            @Override
                                            public IDrawableAnimated load(Integer cookTime) {
                                                return guiHelper.drawableBuilder(
                                                                        RECIPE_GUI_VANILLA,
                                                                        82,
                                                                        128,
                                                                        24,
                                                                        17
                                                                )
                                                                .buildAnimated(
                                                                        cookTime,
                                                                        IDrawableAnimated.StartDirection.LEFT,
                                                                        false
                                                                );
                                            }
                                        });

        addonSlot = guiHelper.getSlotDrawable();
    }

    protected IDrawableAnimated getArrow(AlloyingRecipe recipe) {
        int cookTime = recipe.getSmeltTime();
        if (cookTime <= 0) {
            cookTime = 0;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public RecipeType<AlloyingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
               .addIngredients(recipe.getIngredients().get(0));

        if (recipe.getIngredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 21, 1)
                   .addIngredients(recipe.getIngredients().get(1));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19)
               .addItemStack(recipe.getResultItem());
    }

    @Override
    public boolean isHandled(AlloyingRecipe recipe) {
        return !recipe.isSpecial();
    }

    @Override
    public void draw(
            AlloyingRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            PoseStack poseStack,
            double mouseX,
            double mouseY
    ) {
        animatedFlame.draw(poseStack, 1, 20);

        if (recipe.getIngredients().size() > 1) {
            addonSlot.draw(poseStack, 20, 0);
        }

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 24, 18);

        drawExperience(recipe, poseStack, 0);
        drawCookTime(recipe, poseStack, 45);

    }

    protected void drawExperience(AlloyingRecipe recipe, PoseStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(AlloyingRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getSmeltTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            net.minecraft.client.gui.Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }
}
