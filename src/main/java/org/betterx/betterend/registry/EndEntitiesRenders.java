package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.entity.model.*;
import org.betterx.betterend.entity.render.*;
import org.betterx.betterend.item.model.*;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.EntityType;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import java.util.function.Function;

public class EndEntitiesRenders {
    public static final ModelLayerLocation DRAGONFLY_MODEL = registerMain("dragonfly");
    public static final ModelLayerLocation END_SLIME_SHELL_MODEL = registerMain("endslime_shell");
    public static final ModelLayerLocation END_SLIME_MODEL = registerMain("endslime");
    public static final ModelLayerLocation END_FISH_MODEL = registerMain("endfish");
    public static final ModelLayerLocation CUBOZOA_MODEL = registerMain("cubozoa");
    public static final ModelLayerLocation SILK_MOTH_MODEL = registerMain("silkmoth");
    public static final ModelLayerLocation TEST_MODEL = registerMain("test");

    public static final ModelLayerLocation ARMORED_ELYTRA = registerMain("armored_elytra");
    public static final ModelLayerLocation CRYSTALITE_CHESTPLATE = registerMain("crystalite_chestplate");
    public static final ModelLayerLocation CRYSTALITE_CHESTPLATE_THIN = registerMain("crystalite_chestplate_thin");
    public static final ModelLayerLocation CRYSTALITE_HELMET = registerMain("crystalite_helmet");
    public static final ModelLayerLocation CRYSTALITE_LEGGINGS = registerMain("crystalite_leggings");
    public static final ModelLayerLocation CRYSTALITE_BOOTS = registerMain("crystalite_boots");

    public static void register() {
        register(EndEntities.DRAGONFLY.type(), RendererEntityDragonfly::new);
        register(EndEntities.END_SLIME.type(), RendererEntityEndSlime::new);
        register(EndEntities.END_FISH.type(), RendererEntityEndFish::new);
        register(EndEntities.SHADOW_WALKER.type(), RendererEntityShadowWalker::new);
        register(EndEntities.CUBOZOA.type(), RendererEntityCubozoa::new);
        register(EndEntities.SILK_MOTH.type(), SilkMothEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DRAGONFLY_MODEL, DragonflyEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(
                END_SLIME_SHELL_MODEL,
                EndSlimeEntityModel::getShellOnlyTexturedModelData
        );
        EntityModelLayerRegistry.registerModelLayer(END_SLIME_MODEL, EndSlimeEntityModel::getCompleteTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(END_FISH_MODEL, EndFishEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CUBOZOA_MODEL, CubozoaEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SILK_MOTH_MODEL, SilkMothEntityModel::getTexturedModelData);

        EntityModelLayerRegistry.registerModelLayer(ARMORED_ELYTRA, ArmoredElytraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(
                CRYSTALITE_CHESTPLATE,
                CrystaliteChestplateModel::getRegularTexturedModelData
        );
        EntityModelLayerRegistry.registerModelLayer(
                CRYSTALITE_CHESTPLATE_THIN,
                CrystaliteChestplateModel::getThinTexturedModelData
        );
        EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_HELMET, CrystaliteHelmetModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_LEGGINGS, CrystaliteLeggingsModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_BOOTS, CrystaliteBootsModel::getTexturedModelData);
    }

    private static void register(EntityType<?> type, Function<Context, MobRenderer> renderer) {
        EntityRendererRegistry.register(type, (context) -> renderer.apply(context));
    }

    private static ModelLayerLocation registerMain(String id) {
        return new ModelLayerLocation(BetterEnd.makeID(id), "main");
    }
}
