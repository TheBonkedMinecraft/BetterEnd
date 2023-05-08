package org.betterx.betterend.util;

import org.betterx.bclib.api.v2.BonemealAPI;
import org.betterx.betterend.blocks.basis.EndTerrainBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.google.common.collect.Lists;

import java.util.List;

public class BonemealPlants {
    public static void init() {
        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.END_MOSS,
                EndFeatures.BONEMEAL_END_MOSS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.RUTISCUS,
                EndFeatures.BONEMEAL_RUTISCUS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.END_MYCELIUM,
                EndFeatures.BONEMEAL_END_MYCELIUM
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.JUNGLE_MOSS,
                EndFeatures.BONEMEAL_JUNGLE_MOSS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.SANGNUM,
                EndFeatures.BONEMEAL_SANGNUM
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.MOSSY_OBSIDIAN,
                EndFeatures.BONEMEAL_MOSSY_OBSIDIAN
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.MOSSY_DRAGON_BONE,
                EndFeatures.BONEMEAL_MOSSY_DRAGON_BONE
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CAVE_MOSS,
                EndFeatures.BONEMEAL_CAVE_MOSS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CHORUS_NYLIUM,
                EndFeatures.BONEMEAL_CHORUS_NYLIUM
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CRYSTAL_MOSS,
                EndFeatures.BONEMEAL_CRYSTAL_MOSS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.SHADOW_GRASS,
                EndFeatures.BONEMEAL_SHADOW_GRASS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.PINK_MOSS,
                EndFeatures.BONEMEAL_PINK_MOSS
        );

        org.betterx.bclib.api.v3.bonemeal.BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.AMBER_MOSS,
                EndFeatures.BONEMEAL_AMBER_MOSS
        );
        Block[] charnias = new Block[]{
                EndBlocks.CHARNIA_CYAN,
                EndBlocks.CHARNIA_GREEN,
                EndBlocks.CHARNIA_ORANGE,
                EndBlocks.CHARNIA_LIGHT_BLUE,
                EndBlocks.CHARNIA_PURPLE,
                EndBlocks.CHARNIA_RED
        };
        List<Block> terrain = Lists.newArrayList();
        EndBlocks.getModBlocks().forEach(block -> {
            if (block instanceof EndTerrainBlock) {
                terrain.add(block);
            }
        });
        terrain.add(Blocks.END_STONE);
        terrain.add(EndBlocks.ENDSTONE_DUST);
        terrain.add(EndBlocks.CAVE_MOSS);
        terrain.add(EndBlocks.SULPHURIC_ROCK.stone);
        terrain.add(EndBlocks.VIOLECITE.stone);
        terrain.add(EndBlocks.FLAVOLITE.stone);
        terrain.add(EndBlocks.AZURE_JADESTONE.stone);
        terrain.add(EndBlocks.VIRID_JADESTONE.stone);
        terrain.add(EndBlocks.SANDY_JADESTONE.stone);
        terrain.add(EndBlocks.BRIMSTONE);
        Block[] terrainBlocks = terrain.toArray(new Block[terrain.size()]);
        for (Block charnia : charnias) {
            BonemealAPI.addWaterGrass(charnia, terrainBlocks);
        }
    }
}
