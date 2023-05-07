package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MegalakeGroveBiome extends EndBiome.Config {
    public MegalakeGroveBiome() {
        super("megalake_grove");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .structure(EndStructures.MEGALAKE_SMALL)
                .plantsColor(73, 210, 209)
                .fogColor(178, 209, 248)
                .waterAndFogColor(96, 163, 255)
                .fogDensity(2.0F)
                .particles(EndParticles.GLOWING_SPHERE, 0.001F)
                .music(EndSounds.MUSIC_WATER)
                .loop(EndSounds.AMBIENT_MEGALAKE_GROVE)
                .terrainHeight(0F)
                .feature(EndFeatures.LACUGROVE)
                .feature(EndFeatures.END_LOTUS)
                .feature(EndFeatures.END_LOTUS_LEAF)
                .feature(EndFeatures.BUBBLE_CORAL_RARE)
                .feature(EndFeatures.END_LILY_RARE)
                .feature(EndFeatures.UMBRELLA_MOSS)
                //.feature(EndFeatures.PEARLBERRY)
                .feature(EndFeatures.CREEPING_MOSS)
                .feature(EndFeatures.CHARNIA_CYAN)
                .feature(EndFeatures.CHARNIA_LIGHT_BLUE)
                .feature(EndFeatures.CHARNIA_RED_RARE)
                .feature(EndFeatures.MENGER_SPONGE)
                .spawn(EndEntities.DRAGONFLY, 20, 1, 3)
                .spawn(EndEntities.END_FISH, 20, 3, 8)
                .spawn(EndEntities.CUBOZOA, 50, 3, 8)
                .spawn(EndEntities.END_SLIME, 5, 1, 2)
                .spawn(EntityType.ENDERMAN, 10, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.END_MOSS.defaultBlockState();
            }
        };
    }
}
