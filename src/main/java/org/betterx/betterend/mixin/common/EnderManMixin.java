package org.betterx.betterend.mixin.common;

import org.betterx.betterend.effects.EndStatusEffects;
import org.betterx.betterend.registry.EndEnchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderManMixin {
    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    private void be_isLookingAtMe(Player player, CallbackInfoReturnable<Boolean> info) {
        if (player.isCreative() || player.hasEffect(EndStatusEffects.END_VEIL) || EnchantmentHelper.getItemEnchantmentLevel(
                EndEnchantments.END_VEIL,
                player.getItemBySlot(EquipmentSlot.HEAD)
        ) > 0) {
            info.setReturnValue(false);
        }
    }
}
