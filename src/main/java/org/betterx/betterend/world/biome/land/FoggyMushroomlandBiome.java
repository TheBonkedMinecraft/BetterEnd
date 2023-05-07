package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FoggyMushroomlandBiome extends EndBiome.Config {
    public FoggyMushroomlandBiome() {
        super("foggy_mushroomland");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .structure(EndStructures.GIANT_MOSSY_GLOWSHROOM)
                .plantsColor(73, 210, 209)
                .fogColor(41, 122, 173)
                .fogDensity(3)
                .waterAndFogColor(119, 227, 250)
                .particles(EndParticles.GLOWING_SPHERE, 0.001F)
                .loop(EndSounds.AMBIENT_FOGGY_MUSHROOMLAND)
                .music(EndSounds.MUSIC_FOREST)
                .feature(EndFeatures.END_LAKE)
                .feature(EndFeatures.MOSSY_GLOWSHROOM)
                .feature(EndFeatures.BLUE_VINE)
                .feature(EndFeatures.UMBRELLA_MOSS)
                .feature(EndFeatures.CREEPING_MOSS)
                .feature(EndFeatures.DENSE_VINE)
                //.feature(EndFeatures.PEARLBERRY)
                .feature(EndFeatures.CYAN_MOSS)
                .feature(EndFeatures.CYAN_MOSS_WOOD)
                .feature(EndFeatures.END_LILY)
                .feature(EndFeatures.BUBBLE_CORAL)
                .feature(EndFeatures.CHARNIA_CYAN)
                .feature(EndFeatures.CHARNIA_LIGHT_BLUE)
                .feature(EndFeatures.CHARNIA_RED_RARE)
                .structure(BiomeTags.HAS_END_CITY)
                .spawn(EndEntities.DRAGONFLY, 80, 2, 5)
                .spawn(EndEntities.END_FISH, 20, 2, 5)
                .spawn(EndEntities.CUBOZOA, 10, 3, 8)
                .spawn(EndEntities.END_SLIME, 10, 1, 2)
                .spawn(EntityType.ENDERMAN, 10, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.END_MOSS.defaultBlockState();
            }

            @Override
            public BlockState getAltTopMaterial() {
                return EndBlocks.END_MYCELIUM.defaultBlockState();
            }
        };


    }
}
