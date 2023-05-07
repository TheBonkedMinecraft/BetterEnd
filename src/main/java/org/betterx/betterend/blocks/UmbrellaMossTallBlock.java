package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseDoublePlantBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class UmbrellaMossTallBlock extends BaseDoublePlantBlock {
    public UmbrellaMossTallBlock() {
        super(12);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        ItemEntity item = new ItemEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                new ItemStack(EndBlocks.UMBRELLA_MOSS)
        );
        world.addFreshEntity(item);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.END_MOSS) || state.is(EndBlocks.END_MYCELIUM) || state.is(EndBlocks.JUNGLE_MOSS);
    }
}
