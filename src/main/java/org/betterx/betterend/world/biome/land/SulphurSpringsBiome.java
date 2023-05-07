package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.rules.SwitchRuleSource;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.surface.SulphuricSurfaceNoiseCondition;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import java.util.List;

public class SulphurSpringsBiome extends EndBiome.Config {
    public SulphurSpringsBiome() {
        super("sulphur_springs");
    }

    @Override
    protected boolean hasCaves() {
        return false;
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .music(EndSounds.MUSIC_OPENSPACE)
                .loop(EndSounds.AMBIENT_SULPHUR_SPRINGS)
                .waterColor(25, 90, 157)
                .waterFogColor(30, 65, 61)
                .fogColor(207, 194, 62)
                .fogDensity(1.5F)
                .terrainHeight(0F)
                .particles(EndParticles.SULPHUR_PARTICLE, 0.001F)
                .feature(EndFeatures.GEYSER)
                .feature(EndFeatures.SURFACE_VENT)
                .feature(EndFeatures.SULPHURIC_LAKE)
                .feature(EndFeatures.SULPHURIC_CAVE)
                .feature(EndFeatures.HYDRALUX)
                .feature(EndFeatures.CHARNIA_GREEN)
                .feature(EndFeatures.CHARNIA_ORANGE)
                .feature(EndFeatures.CHARNIA_RED_RARE)
                .spawn(EndEntities.END_FISH, 50, 3, 8)
                .spawn(EndEntities.CUBOZOA, 50, 3, 8)
                .spawn(EntityType.ENDERMAN, 50, 1, 4);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.FLAVOLITE.stone.defaultBlockState();
            }

            @Override
            public BlockState getAltTopMaterial() {
                return Blocks.END_STONE.defaultBlockState();
            }

            @Override
            public boolean generateFloorRule() {
                return false;
            }

            @Override
            public SurfaceRuleBuilder surface() {
                RuleSource surfaceBlockRule = new SwitchRuleSource(
                        new SulphuricSurfaceNoiseCondition(),
                        List.of(
                                SurfaceRules.state(surfaceMaterial().getAltTopMaterial()),
                                SurfaceRules.state(surfaceMaterial().getTopMaterial()),
                                SULPHURIC_ROCK,
                                BRIMSTONE
                        )
                );
                return super
                        .surface()
                        .rule(2, SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, surfaceBlockRule))
                        .rule(
                                2,
                                SurfaceRules.ifTrue(
                                        SurfaceRules.stoneDepthCheck(5, false, CaveSurface.FLOOR),
                                        surfaceBlockRule
                                )
                        );
            }
        };
    }
}
