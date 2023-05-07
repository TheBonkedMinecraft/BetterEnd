package org.betterx.betterend.mixin.common;

import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.item.tool.EndHammerItem;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    public void be_getDroppedStacks(
            BlockState state,
            LootContext.Builder builder,
            CallbackInfoReturnable<List<ItemStack>> info
    ) {
        if (state.is(Blocks.GLOWSTONE)) {
            ItemStack tool = builder.getParameter(LootContextParams.TOOL);
            if (tool != null && tool.getItem() instanceof EndHammerItem) {
                int min = 3;
                int max = 4;
                int count = 0;
                int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
                if (fortune > 0) {
                    fortune /= Enchantments.BLOCK_FORTUNE.getMaxLevel();
                    min = Mth.clamp(min + fortune, min, max);
                    if (min == max) {
                        info.setReturnValue(Lists.newArrayList(new ItemStack(Items.GLOWSTONE_DUST, max)));
                    }
                }
                count = MHelper.randRange(min, max, MHelper.RANDOM_SOURCE);
                info.setReturnValue(Lists.newArrayList(new ItemStack(Items.GLOWSTONE_DUST, count)));
            }
        }
    }
}
