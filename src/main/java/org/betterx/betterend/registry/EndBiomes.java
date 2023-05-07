package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.LifeCycleAPI;
import org.betterx.bclib.api.v2.generator.BiomePicker;
import org.betterx.bclib.api.v2.generator.map.hex.HexBiomeMap;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.biome.air.BiomeIceStarfield;
import org.betterx.betterend.world.biome.cave.*;
import org.betterx.betterend.world.biome.land.*;
import org.betterx.betterend.world.generator.BiomeType;
import org.betterx.betterend.world.generator.GeneratorOptions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

import java.util.LinkedList;
import java.util.List;

public class EndBiomes {
    public static final BiomeAPI.BiomeType END_CAVE = new BiomeAPI.BiomeType("END_CAVE", BiomeAPI.BiomeType.END_IGNORE);
    public static final List<EndBiome> ALL_BE_BIOMES = new LinkedList<>();
    public static BiomePicker CAVE_BIOMES = null;
    private static HexBiomeMap caveBiomeMap;
    private static long lastSeed;

    // Better End Land
    public static final EndBiome FOGGY_MUSHROOMLAND = registerBiome(new FoggyMushroomlandBiome(), BiomeType.LAND);
    public static final EndBiome CHORUS_FOREST = registerBiome(new ChorusForestBiome(), BiomeType.LAND);
    public static final EndBiome DUST_WASTELANDS = registerBiome(new DustWastelandsBiome(), BiomeType.LAND);
    public static final EndBiome MEGALAKE = registerBiome(new MegalakeBiome(), BiomeType.LAND);
    public static final EndBiome MEGALAKE_GROVE = registerSubBiome(new MegalakeGroveBiome(), MEGALAKE);
    public static final EndBiome CRYSTAL_MOUNTAINS = registerBiome(new CrystalMountainsBiome(), BiomeType.LAND);
    public static final EndBiome PAINTED_MOUNTAINS = registerSubBiome(new PaintedMountainsBiome(), DUST_WASTELANDS);
    public static final EndBiome SHADOW_FOREST = registerBiome(new ShadowForestBiome(), BiomeType.LAND);
    public static final EndBiome AMBER_LAND = registerBiome(new AmberLandBiome(), BiomeType.LAND);
    public static final EndBiome BLOSSOMING_SPIRES = registerBiome(new BlossomingSpiresBiome(), BiomeType.LAND);
    public static final EndBiome SULPHUR_SPRINGS = registerBiome(new SulphurSpringsBiome(), BiomeType.LAND);
    public static final EndBiome UMBRELLA_JUNGLE = registerBiome(new UmbrellaJungleBiome(), BiomeType.LAND);
    public static final EndBiome GLOWING_GRASSLANDS = registerBiome(new GlowingGrasslandsBiome(), BiomeType.LAND);
    public static final EndBiome DRAGON_GRAVEYARDS = registerBiome(new DragonGraveyardsBiome(), BiomeType.LAND);
    public static final EndBiome DRY_SHRUBLAND = registerBiome(new DryShrublandBiome(), BiomeType.LAND);
    public static final EndBiome LANTERN_WOODS = registerBiome(new LanternWoodsBiome(), BiomeType.LAND);
    public static final EndBiome NEON_OASIS = registerSubBiome(new NeonOasisBiome(), DUST_WASTELANDS);
    public static final EndBiome UMBRA_VALLEY = registerBiome(new UmbraValleyBiome(), BiomeType.LAND);

    // Better End Void
    public static final EndBiome ICE_STARFIELD = registerBiome(new BiomeIceStarfield(), BiomeType.VOID);

    // Better End Caves
    public static final EndCaveBiome EMPTY_END_CAVE = registerCaveBiome(new EmptyEndCaveBiome());
    public static final EndCaveBiome EMPTY_SMARAGDANT_CAVE = registerCaveBiome(new EmptySmaragdantCaveBiome());
    public static final EndCaveBiome LUSH_SMARAGDANT_CAVE = registerCaveBiome(new LushSmaragdantCaveBiome());
    public static final EndCaveBiome EMPTY_AURORA_CAVE = registerCaveBiome(new EmptyAuroraCaveBiome());
    public static final EndCaveBiome LUSH_AURORA_CAVE = registerCaveBiome(new LushAuroraCaveBiome());
    public static final EndCaveBiome JADE_CAVE = registerCaveBiome(new JadeCaveBiome());

    public static void register() {
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("biome"), EndBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("cave_biome"), EndCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("empty_aurora_cave_biome"),
                EmptyAuroraCaveBiome.KEY_CODEC
        );
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("empty_end_cave_biome"), EmptyEndCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("empty_smaragdant_cave_biome"),
                EmptySmaragdantCaveBiome.KEY_CODEC
        );
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("jade_cave_biome"), JadeCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("lush_aurora_cave_biome"), LushAuroraCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("lush_smaragdant_cave_biome"),
                LushSmaragdantCaveBiome.KEY_CODEC
        );
        LifeCycleAPI.onLevelLoad(EndBiomes::onWorldLoad);
    }

    private static void onWorldLoad(ServerLevel level, long seed, Registry<Biome> registry) {
        if (CAVE_BIOMES == null || CAVE_BIOMES.biomeRegistry != registry) {
            CAVE_BIOMES = new BiomePicker(registry);
            registry.stream()
                    .filter(biome -> registry.getResourceKey(biome).isPresent())
                    .map(biome -> registry.getOrCreateHolderOrThrow(registry.getResourceKey(biome).get()))
                    .map(biome -> biome.unwrapKey().orElseThrow().location())
                    .filter(id -> BiomeAPI.wasRegisteredAs(id, END_CAVE))
                    .map(id -> BiomeAPI.getBiome(id))
                    .filter(bcl -> bcl != null)
                    .forEach(bcl -> CAVE_BIOMES.addBiome(bcl));

            CAVE_BIOMES.rebuild();
            caveBiomeMap = null;
        }

        if (caveBiomeMap == null || lastSeed != seed) {
            caveBiomeMap = new HexBiomeMap(seed, GeneratorOptions.getBiomeSizeCaves(), CAVE_BIOMES);
            lastSeed = seed;
        }
    }

    /**
     * Put existing {@link EndBiome} as a sub-biome into selected parent.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @param parent      - {@link EndBiome} to be linked with
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerSubBiome(EndBiome.Config biomeConfig, EndBiome parent) {
        final EndBiome biome = EndBiome.create(biomeConfig);

        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            BiomeAPI.registerSubBiome(parent, biome);
        }
        return biome;
    }

    /**
     * Registers {@link EndBiome} and adds it into worldgen.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @param type        - {@link BiomeType}
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerBiome(EndBiome.Config biomeConfig, BiomeType type) {
        final EndBiome biome = EndBiome.create(biomeConfig);
        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            if (type == BiomeType.LAND) {
                BiomeAPI.registerEndLandBiome(biome);
            } else {
                BiomeAPI.registerEndVoidBiome(biome);
            }
            ALL_BE_BIOMES.add(biome);
        }
        return biome;
    }

    /**
     * Put integration sub-biome {@link EndBiome} into subbiomes list and registers it.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerSubBiomeIntegration(EndBiome.Config biomeConfig) {
        EndBiome biome = EndBiome.create(biomeConfig);
        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            BiomeAPI.registerBuiltinBiomeAndOverrideIntendedDimension(biome, BiomeAPI.BiomeType.END);
        }
        return biome;
    }

    /**
     * Link integration sub-biome with parent.
     *
     * @param biome  - {@link EndBiome} instance
     * @param parent - {@link ResourceLocation} parent id
     */
    public static void addSubBiomeIntegration(EndBiome biome, ResourceLocation parent) {
        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            BCLBiome parentBiome = BiomeAPI.getBiome(parent);
            if (parentBiome != null && !parentBiome.containsSubBiome(biome)) {
                parentBiome.addSubBiome(biome);
            }
        }
    }

    public static EndCaveBiome registerCaveBiome(EndCaveBiome.Config biomeConfig) {
        final EndCaveBiome biome = EndCaveBiome.create(biomeConfig);
        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            BiomeAPI.registerBuiltinBiomeAndOverrideIntendedDimension(biome, END_CAVE);
            //ALL_BE_BIOMES.add(biome);
        }
        return biome;
    }

    public static BiomePicker.ActualBiome getCaveBiome(int x, int z) {
        return caveBiomeMap.getBiome(x, 5, z);
    }

}
