package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.rules.SwitchRuleSource;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.registry.EndSounds;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.surface.UmbraSurfaceNoiseCondition;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.List;

public class UmbraValleyBiome extends EndBiome.Config {
    private static final Block[] SURFACE_BLOCKS = new Block[]{
            EndBlocks.PALLIDIUM_FULL,
            EndBlocks.PALLIDIUM_HEAVY,
            EndBlocks.PALLIDIUM_THIN,
            EndBlocks.PALLIDIUM_TINY,
            EndBlocks.UMBRALITH.stone
    };

    public UmbraValleyBiome() {
        super("umbra_valley");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .fogColor(100, 100, 100)
                .plantsColor(172, 189, 190)
                .waterAndFogColor(69, 104, 134)
                .particles(EndParticles.AMBER_SPHERE, 0.0001F)
                .loop(EndSounds.UMBRA_VALLEY)
                .music(EndSounds.MUSIC_DARK)
                .feature(EndFeatures.UMBRALITH_ARCH)
                .feature(EndFeatures.THIN_UMBRALITH_ARCH)
                .feature(EndFeatures.INFLEXIA)
                .feature(EndFeatures.FLAMMALIX);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.UMBRALITH.stone.defaultBlockState();
            }

            @Override
            public BlockState getUnderMaterial() {
                return EndBlocks.UMBRALITH.stone.defaultBlockState();
            }

            @Override
            public BlockState getAltTopMaterial() {
                return EndBlocks.PALLIDIUM_FULL.defaultBlockState();
            }

            @Override
            public boolean generateFloorRule() {
                return false;
            }

            @Override
            public SurfaceRuleBuilder surface() {
                return super.surface()
                            .rule(2, SurfaceRules.ifTrue(
                                    SurfaceRules.ON_FLOOR,
                                    new SwitchRuleSource(
                                            new UmbraSurfaceNoiseCondition(),
                                            List.of(
                                                    SurfaceRules.state(surfaceMaterial().getAltTopMaterial()),
                                                    PALLIDIUM_HEAVY,
                                                    PALLIDIUM_THIN,
                                                    PALLIDIUM_TINY,
                                                    SurfaceRules.state(surfaceMaterial().getTopMaterial())
                                            )
                                    )
                            ));
            }
        };
    }

    public static Block getSurface(int x, int z) {
        return SURFACE_BLOCKS[UmbraSurfaceNoiseCondition.getDepth(x, z)];
    }
}
