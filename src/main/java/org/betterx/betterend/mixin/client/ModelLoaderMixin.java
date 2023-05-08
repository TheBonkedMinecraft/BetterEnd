package org.betterx.betterend.mixin.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
    @ModifyArg(method = "loadModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;getResourceStack(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;"))
    public ResourceLocation be_switchModelOnLoad(ResourceLocation loc) {
        return loc;
    }
    private boolean be_changeModel(ResourceLocation id) {
        return id.getNamespace().equals("minecraft")
                && id.getPath().startsWith("blockstates/")
                && id.getPath().contains("chorus")
                && !id.getPath().contains("custom_");
    }
}
