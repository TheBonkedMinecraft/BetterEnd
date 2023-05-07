package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.betterend.client.models.Patterns;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class StoneLanternBlock extends EndLanternBlock implements CustomColorProvider {
    private static final VoxelShape SHAPE_CEIL = box(3, 1, 3, 13, 16, 13);
    private static final VoxelShape SHAPE_FLOOR = box(3, 0, 3, 13, 15, 13);

    public StoneLanternBlock(Block source) {
        super(FabricBlockSettings.copyOf(source).luminance(15));
    }

    @Override
    public BlockColor getProvider() {
        return ((CustomColorProvider) EndBlocks.AURORA_CRYSTAL).getProvider();
    }

    @Override
    public ItemColor getItemProvider() {
        return ((CustomColorProvider) EndBlocks.AURORA_CRYSTAL).getItemProvider();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return state.getValue(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
        String blockName = resourceLocation.getPath();
        Optional<String> pattern = blockState.getValue(IS_FLOOR)
                ? Patterns.createJson(
                Patterns.BLOCK_STONE_LANTERN_FLOOR,
                blockName,
                blockName
        )
                : Patterns.createJson(Patterns.BLOCK_STONE_LANTERN_CEIL, blockName, blockName);
        return ModelsHelper.fromPattern(pattern);
    }
}
