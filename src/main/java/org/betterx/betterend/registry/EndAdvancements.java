package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.advancement.AdvancementManager;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.advancements.BECriteria;

import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EndAdvancements {
    public static void register() {
        ResourceLocation root = AdvancementManager.Builder
                .create(BetterEnd.makeID("root"))
                .startDisplay(EndBlocks.CAVE_MOSS)
                .frame(FrameType.TASK)
                .hideToast()
                .hideFromChat()
                .background(new ResourceLocation("textures/gui/advancements/backgrounds/end.png"))
                .endDisplay()
                .addCriterion(
                        "entered_end",
                        ChangeDimensionTrigger
                                .TriggerInstance
                                .changedDimensionTo(Level.END)
                )
                .requirements(RequirementsStrategy.OR)
                .buildAndRegister();

        ResourceLocation allElytras = AdvancementManager.Builder
                .create(BetterEnd.makeID("all_elytras"))
                .parent(root)
                .startDisplay(EndItems.CRYSTALITE_ELYTRA)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addInventoryChangedCriterion("vanilla", Items.ELYTRA)
                .addInventoryChangedCriterion("crystalite", EndItems.CRYSTALITE_ELYTRA)
                .addInventoryChangedCriterion("armored", EndItems.ARMORED_ELYTRA)
                .requirements(RequirementsStrategy.AND)
                .buildAndRegister();

        ResourceLocation infusion = AdvancementManager.Builder
                .create(BetterEnd.makeID("infusion"))
                .parent(root)
                .startDisplay(EndBlocks.INFUSION_PEDESTAL)
                .endDisplay()
                .addInventoryChangedCriterion("infusion_pedestal", EndBlocks.INFUSION_PEDESTAL)
                .requirements(RequirementsStrategy.OR)
                .buildAndRegister();

        ResourceLocation infusionFinished = AdvancementManager.Builder
                .create(BetterEnd.makeID("infusion_finished"))
                .parent(infusion)
                .startDisplay(Items.ENDER_EYE)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addCriterion("finished", BECriteria.INFUSION_FINISHED_TRIGGER)
                .requirements(RequirementsStrategy.OR)
                .buildAndRegister();
    }
}
