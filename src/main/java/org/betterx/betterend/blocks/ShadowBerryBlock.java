package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableCropBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShadowBerryBlock extends PottableCropBlock {
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 8, 15);

    public ShadowBerryBlock() {
        super(EndItems.SHADOW_BERRY_RAW, EndBlocks.SHADOW_GRASS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE;
    }
}
