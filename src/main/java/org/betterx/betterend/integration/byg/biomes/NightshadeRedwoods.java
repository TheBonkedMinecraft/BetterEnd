package org.betterx.betterend.integration.byg.biomes;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.integration.byg.features.BYGFeatures;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.List;

public class NightshadeRedwoods extends EndBiome.Config {
    public NightshadeRedwoods() {
        super("nightshade_redwoods");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        Holder<Biome> biome = Integrations.BYG.getBiome("nightshade_forest");
        BiomeSpecialEffects effects = biome.value().getSpecialEffects();

        builder.fogColor(140, 108, 47)
               .fogDensity(1.5F)
               .waterAndFogColor(55, 70, 186)
               .foliageColor(122, 17, 155)
               .particles(
                       ParticleTypes.REVERSE_PORTAL,
                       0.002F
               )
               .grassColor(48, 13, 89)
               .plantsColor(200, 125, 9)
               .feature(EndFeatures.END_LAKE_RARE)
               .feature(BYGFeatures.NIGHTSHADE_REDWOOD_TREE)
               .feature(BYGFeatures.NIGHTSHADE_MOSS_WOOD)
               .feature(BYGFeatures.NIGHTSHADE_MOSS);

        if (BCLib.isClient()) {
            SoundEvent loop = effects.getAmbientLoopSoundEvent()
                                     .get();
            SoundEvent music = effects.getBackgroundMusic()
                                      .get()
                                      .getEvent();
            SoundEvent additions = effects.getAmbientAdditionsSettings()
                                          .get()
                                          .getSoundEvent();
            SoundEvent mood = effects.getAmbientMoodSettings()
                                     .get()
                                     .getSoundEvent();
            builder.loop(loop)
                   .music(music)
                   .additions(additions)
                   .mood(mood);
        }
        biome.value().getGenerationSettings()
             .features()
             .forEach((list) -> {
                 list.forEach((feature) -> {
                     builder.feature(Decoration.VEGETAL_DECORATION, feature);
                 });
             });

        for (MobCategory group : MobCategory.values()) {
            List<SpawnerData> list = biome.value()
                                          .getMobSettings()
                                          .getMobs(group)
                                          .unwrap();
            list.forEach((entry) -> {
                builder.spawn((EntityType<? extends Mob>) entry.type, 1, entry.minCount, entry.maxCount);
            });
        }
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return Integrations.BYG.getBlock("nightshade_phylium").defaultBlockState();
            }

            @Override
            public SurfaceRuleBuilder surface() {
                return SurfaceRuleBuilder
                        .start()
                        .rule(4, SurfaceRules.sequence(SurfaceRules.ifTrue(
                                                BYGBiomes.BYG_WATER_CHECK,
                                                SurfaceRules.ifTrue(
                                                        SurfaceRules.ON_FLOOR,
                                                        SurfaceRules.state(getTopMaterial())
                                                )
                                        )
                                )
                        );
            }
        };
    }
}
