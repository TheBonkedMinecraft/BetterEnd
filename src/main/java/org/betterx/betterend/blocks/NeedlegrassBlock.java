package org.betterx.betterend.blocks;

import org.betterx.bclib.items.tool.BaseShearsItem;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.basis.EndPlantBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import com.google.common.collect.Lists;

import java.util.List;

public class NeedlegrassBlock extends EndPlantBlock {
    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.hurt(DamageSource.CACTUS, 0.1F);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        if (tool != null && BaseShearsItem.isShear(tool) || EnchantmentHelper.getItemEnchantmentLevel(
                Enchantments.SILK_TOUCH,
                tool
        ) > 0) {
            return Lists.newArrayList(new ItemStack(this));
        } else {
            return Lists.newArrayList(new ItemStack(Items.STICK, MHelper.randRange(0, 2, MHelper.RANDOM_SOURCE)));
        }
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.SHADOW_GRASS);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
