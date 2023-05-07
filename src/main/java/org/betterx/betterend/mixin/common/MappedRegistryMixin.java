package org.betterx.betterend.mixin.common;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
    // TODO Make this a part of BCLib (implement froze/unfroze methods)
    @Inject(method = "validateWrite", at = @At("HEAD"), cancellable = true)
    private void be_validateWrite(ResourceKey<T> resourceKey, CallbackInfo info) {
        //info.cancel();
    }

//    @Inject(method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;Z)Lnet/minecraft/core/Holder;", at = @At("HEAD"))
//    private void be_debugDummy(
//            int i,
//            ResourceKey<T> resourceKey,
//            T object,
//            Lifecycle lifecycle,
//            boolean bl,
//            CallbackInfoReturnable<Holder<T>> cir
//    ) {
//        if (resourceKey.location().getPath().equals("dark_amaranth_forest")) {
//            System.out.println("Promenade registers: " + resourceKey);
//        }
//    }
//
//    @Inject(method = "getOrCreateHolderOrThrow", at = @At("HEAD"))
//    private void be_debugDummy2(
//            ResourceKey<T> resourceKey, CallbackInfoReturnable<Holder<T>> cir
//    ) {
//        if (resourceKey.location().getPath().equals("dark_amaranth_forest")) {
//            System.out.println("Promenade registers: " + resourceKey);
//        }
//    }
//
//    @Inject(method = "getOrCreateHolder", at = @At("HEAD"))
//    private void be_debugDummy3(
//            ResourceKey<T> resourceKey, CallbackInfoReturnable<Holder<T>> cir
//    ) {
//        if (resourceKey.location().getPath().equals("dark_amaranth_forest")) {
//            System.out.println("Promenade registers: " + resourceKey);
//        }
//    }
}
