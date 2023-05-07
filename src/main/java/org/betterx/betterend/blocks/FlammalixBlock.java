package org.betterx.betterend.blocks;

import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.betterend.blocks.basis.EndPlantBlock;
import org.betterx.betterend.registry.EndBlocks;

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

public class FlammalixBlock extends EndPlantBlock {
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 14, 14);

    public FlammalixBlock() {
        super(basePlantSettings(12).offsetType(OffsetType.NONE));
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.PALLIDIUM_FULL) ||
                state.is(EndBlocks.PALLIDIUM_HEAVY) ||
                state.is(EndBlocks.PALLIDIUM_THIN) ||
                state.is(EndBlocks.PALLIDIUM_TINY);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BlockModel getItemModel(ResourceLocation resourceLocation) {
        return ModelsHelper.createItemModel(resourceLocation);
    }
}
