package org.betterx.betterend.item.model;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import com.google.common.collect.ImmutableList;

public class ArmoredElytraModel<T extends LivingEntity> extends AgeableListModel<T> {
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(
                PartNames.LEFT_WING,
                CubeListBuilder.create().texOffs(22, 0).addBox(-10.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f),
                PartPose.ZERO
        );

        modelPartData.addOrReplaceChild(
                PartNames.RIGHT_WING,
                CubeListBuilder.create().mirror().texOffs(22, 0).addBox(0.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f),
                PartPose.ZERO
        );

        return LayerDefinition.create(modelData, 64, 32);
    }

    public ArmoredElytraModel(ModelPart modelPart) {
        leftWing = modelPart.getChild(PartNames.LEFT_WING);
        rightWing = modelPart.getChild(PartNames.RIGHT_WING);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(leftWing, rightWing);
    }

    public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
        float rotX = 0.2617994F;
        float rotZ = -0.2617994F;
        float rotY = 0.0F;
        float wingY = 0.0F;
        if (livingEntity.isFallFlying()) {
            float coef = 1.0F;
            Vec3 vec3 = livingEntity.getDeltaMovement();
            if (vec3.y < 0.0D) {
                Vec3 normalized = vec3.normalize();
                coef = 1.0F - (float) Math.pow(-normalized.y, 2.5D);
            }
            rotX = coef * 0.34906584F + (1.0F - coef) * rotX;
            rotZ = coef * -1.5707964F + (1.0F - coef) * rotZ;
        } else if (livingEntity.isCrouching()) {
            rotX = 0.6981317F;
            rotZ = -0.7853982F;
            rotY = 0.08726646F;
            wingY = 3.0F;
        }

        leftWing.x = 5.0F;
        leftWing.y = wingY;
        if (livingEntity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) livingEntity;
            abstractClientPlayer.elytraRotX = (float) ((double) abstractClientPlayer.elytraRotX + (double) (rotX - abstractClientPlayer.elytraRotX) * 0.1D);
            abstractClientPlayer.elytraRotY = (float) ((double) abstractClientPlayer.elytraRotY + (double) (rotY - abstractClientPlayer.elytraRotY) * 0.1D);
            abstractClientPlayer.elytraRotZ = (float) ((double) abstractClientPlayer.elytraRotZ + (double) (rotZ - abstractClientPlayer.elytraRotZ) * 0.1D);
            leftWing.xRot = abstractClientPlayer.elytraRotX;
            leftWing.yRot = abstractClientPlayer.elytraRotY;
            leftWing.zRot = abstractClientPlayer.elytraRotZ;
        } else {
            leftWing.xRot = rotX;
            leftWing.zRot = rotZ;
            leftWing.yRot = rotY;
        }

        rightWing.x = -leftWing.x;
        rightWing.yRot = -leftWing.yRot;
        rightWing.y = leftWing.y;
        rightWing.xRot = leftWing.xRot;
        rightWing.zRot = -leftWing.zRot;
    }
}
