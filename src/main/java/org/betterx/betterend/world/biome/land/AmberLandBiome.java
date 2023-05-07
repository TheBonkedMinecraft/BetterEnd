package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AmberLandBiome extends EndBiome.Config {
    public AmberLandBiome() {
        super("amber_land");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .fogColor(255, 184, 71)
                .fogDensity(2.0F)
                .plantsColor(219, 115, 38)
                .waterAndFogColor(145, 108, 72)
                .music(EndSounds.MUSIC_FOREST)
                .loop(EndSounds.AMBIENT_AMBER_LAND)
                .particles(EndParticles.AMBER_SPHERE, 0.001F)
                .feature(EndFeatures.AMBER_ORE)
                .feature(EndFeatures.END_LAKE_RARE)
                .feature(EndFeatures.HELIX_TREE)
                .feature(EndFeatures.LANCELEAF)
                .feature(EndFeatures.GLOW_PILLAR)
                .feature(EndFeatures.AMBER_GRASS)
                .feature(EndFeatures.AMBER_ROOT)
                .feature(EndFeatures.BULB_MOSS)
                .feature(EndFeatures.BULB_MOSS_WOOD)
                .feature(EndFeatures.CHARNIA_ORANGE)
                .feature(EndFeatures.CHARNIA_RED)
                .structure(BiomeTags.HAS_END_CITY)
                .spawn(EntityType.ENDERMAN, 50, 1, 4)
                .spawn(EndEntities.END_SLIME, 30, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.AMBER_MOSS.defaultBlockState();
            }
        };
    }
}
