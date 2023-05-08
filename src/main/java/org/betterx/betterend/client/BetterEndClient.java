package org.betterx.betterend.client;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.util.TranslationHelper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.client.render.BetterEndSkyRenderer;
import org.betterx.betterend.events.ItemTooltipCallback;
import org.betterx.betterend.interfaces.MultiModelItem;
import org.betterx.betterend.item.CrystaliteArmor;
import org.betterx.betterend.registry.*;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;

public class BetterEndClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EndBlockEntityRenders.register();
        EndScreens.register();
        EndParticles.register();
        EndEntitiesRenders.register();
        EndModelProviders.register();
        MultiModelItem.register();
        ClientOptions.init();
        registerTooltips();

        if (BCLib.isDevEnvironment()) {
            TranslationHelper.printMissingEnNames(BetterEnd.MOD_ID);
            TranslationHelper.printMissingNames(BetterEnd.MOD_ID, "ru_ru");
        }

        if (ClientOptions.isCustomSky()) {
            DimensionRenderingRegistry.registerSkyRenderer(Level.END, new BetterEndSkyRenderer());
        }

        if (BetterEnd.RUNS_TRINKETS) {
            org.betterx.betterend.integration.trinkets.ElytraClient.register();
        }
    }

    public static void registerTooltips() {
        ItemTooltipCallback.EVENT.register((player, stack, context, lines) -> {
            if (stack.getItem() instanceof CrystaliteArmor) {
                boolean hasSet = false;
                if (player != null) {
                    hasSet = CrystaliteArmor.hasFullSet(player);
                }
                MutableComponent setDesc = Component.translatable("tooltip.armor.crystalite_set");

                setDesc.setStyle(Style.EMPTY.applyFormats(
                        hasSet ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY,
                        ChatFormatting.ITALIC
                ));
                lines.add(Component.empty());
                lines.add(setDesc);
            }
        });
    }
}
