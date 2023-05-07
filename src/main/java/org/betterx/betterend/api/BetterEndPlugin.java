package org.betterx.betterend.api;

public interface BetterEndPlugin {
    /**
     * Alloying recipes registration.
     * See AlloyingRecipe.Builder for details.
     */
    default void registerAlloyingRecipes() {
    }

    /**
     * Smithing recipes registration.
     * See AnvilSmithingRecipe.Builder for details.
     */
    default void registerSmithingRecipes() {
    }

    /**
     * Additional biomes registration.
     * See BiomeRegistry.registerBiome for details.
     */
    default void registerEndBiomes() {
    }

    /**
     * Register other mod stuff, for example, EndITEM_HAMMERS.
     */
    default void registerOthers() {
    }


    static void register(BetterEndPlugin plugin) {
        plugin.registerAlloyingRecipes();
        plugin.registerSmithingRecipes();
        plugin.registerEndBiomes();
        plugin.registerOthers();
    }
}
