package org.betterx.betterend;

import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.betterend.advancements.BECriteria;
import org.betterx.betterend.api.BetterEndPlugin;
import org.betterx.betterend.commands.CommandRegistry;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.effects.EndPotions;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.recipe.*;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.util.BonemealPlants;
import org.betterx.betterend.util.LootTableUtil;
import org.betterx.worlds.together.util.Logger;
import org.betterx.worlds.together.world.WorldConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BetterEnd implements ModInitializer {
    public static final String MOD_ID = "betterend";
    public static final Logger LOGGER = new Logger(MOD_ID);
    public static final boolean RUNS_TRINKETS = FabricLoader.getInstance()
                                                            .getModContainer("trinkets")
                                                            .isPresent();

    @Override
    public void onInitialize() {
        WorldConfig.registerModCache(MOD_ID);
        EndPortals.loadPortals();
        EndSounds.register();
        EndMenuTypes.ensureStaticallyLoaded();
        EndBlockEntities.register();
        EndPoiTypes.register();
        EndFeatures.register();
        EndEntities.register();
        EndTags.register();
        EndBlocks.ensureStaticallyLoaded();
        EndItems.ensureStaticallyLoaded();
        EndEnchantments.register();
        EndPotions.register();
        CraftingRecipes.register();
        FurnaceRecipes.register();
        AlloyingRecipes.register();
        AnvilRecipes.register();
        SmithingRecipes.register();
        InfusionRecipes.register();
        EndStructures.register();
        BonemealPlants.init();
        LootTableUtil.init();
        CommandRegistry.register();
        BECriteria.register();
        EndAdvancements.register();
        FabricLoader.getInstance()
                    .getEntrypoints("betterend", BetterEndPlugin.class)
                    .forEach(BetterEndPlugin::register);
        Integrations.init();
        Configs.saveConfigs();


        BiomeAPI.registerEndBiomeModification((biomeID, biome) -> {
            if (!biomeID.equals(Biomes.THE_VOID.location())) {
                EndFeatures.addBiomeFeatures(biomeID, biome);
            }
        });

        BiomeAPI.onFinishingEndBiomeTags((biomeID, biome) -> {
            if (!biomeID.equals(Biomes.THE_VOID.location())) {
                EndStructures.addBiomeStructures(biomeID, biome);
            }
        });
        if (RUNS_TRINKETS) {
            org.betterx.betterend.integration.trinkets.Elytra.register();
        }
    }

    public static ResourceLocation makeID(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
