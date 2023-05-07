package org.betterx.betterend.blocks;

import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class EmeraldIceBlock extends HalfTransparentBlock implements RenderLayerProvider, BlockModelProvider {
    public EmeraldIceBlock() {
        super(FabricBlockSettings.copyOf(Blocks.ICE));
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.TRANSLUCENT;
    }

    @Override
    public void playerDestroy(
            Level world,
            Player player,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity,
            ItemStack stack
    ) {
        super.playerDestroy(world, player, pos, state, blockEntity, stack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            if (world.dimensionType().ultraWarm()) {
                world.removeBlock(pos, false);
                return;
            }

            Material material = world.getBlockState(pos.below()).getMaterial();
            if (material.blocksMotion() || material.isLiquid()) {
                world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(world, pos)) {
            this.melt(state, world, pos);
        }

    }

    protected void melt(BlockState state, Level world, BlockPos pos) {
        if (world.dimensionType().ultraWarm()) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            world.neighborChanged(pos, Blocks.WATER, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
        if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) != 0) {
            return Collections.singletonList(new ItemStack(this));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockModel getItemModel(ResourceLocation resourceLocation) {
        return getBlockModel(resourceLocation, defaultBlockState());
    }
}
