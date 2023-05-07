package org.betterx.betterend.config;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;
import org.betterx.bclib.config.PathConfig;
import org.betterx.betterend.BetterEnd;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Configs {
    public static final PathConfig ENTITY_CONFIG = new PathConfig(BetterEnd.MOD_ID, "entities");
    public static final PathConfig BLOCK_CONFIG = new PathConfig(BetterEnd.MOD_ID, "blocks");
    public static final PathConfig ITEM_CONFIG = new PathConfig(BetterEnd.MOD_ID, "items");
    public static final IdConfig BIOME_CONFIG = new EntryConfig(BetterEnd.MOD_ID, "biomes");
    public static final PathConfig GENERATOR_CONFIG = new PathConfig(BetterEnd.MOD_ID, "generator", false);
    public static final PathConfig RECIPE_CONFIG = new PathConfig(BetterEnd.MOD_ID, "recipes");
    public static final PathConfig ENCHANTMENT_CONFIG = new PathConfig(BetterEnd.MOD_ID, "enchantments");

    @Environment(value = EnvType.CLIENT)
    public static final PathConfig CLENT_CONFIG = new PathConfig(BetterEnd.MOD_ID, "client", false);

    public static void saveConfigs() {
        ENTITY_CONFIG.saveChanges();
        BLOCK_CONFIG.saveChanges();
        BIOME_CONFIG.saveChanges();
        ITEM_CONFIG.saveChanges();
        GENERATOR_CONFIG.saveChanges();
        RECIPE_CONFIG.saveChanges();
        ENCHANTMENT_CONFIG.saveChanges();

        if (BCLib.isClient()) {
            CLENT_CONFIG.saveChanges();
        }
    }
}
