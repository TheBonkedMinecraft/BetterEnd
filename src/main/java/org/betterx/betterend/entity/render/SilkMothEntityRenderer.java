package org.betterx.betterend.entity.render;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.entity.SilkMothEntity;
import org.betterx.betterend.entity.model.SilkMothEntityModel;
import org.betterx.betterend.registry.EndEntitiesRenders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SilkMothEntityRenderer extends MobRenderer<SilkMothEntity, SilkMothEntityModel> {
    private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/silk_moth.png");

    public SilkMothEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SilkMothEntityModel(ctx.bakeLayer(EndEntitiesRenders.SILK_MOTH_MODEL)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SilkMothEntity entity) {
        return TEXTURE;
    }
}