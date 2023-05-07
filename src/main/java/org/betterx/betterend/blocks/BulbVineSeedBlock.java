package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class BulbVineSeedBlock extends EndPlantWithAgeBlock {

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState up = world.getBlockState(pos.above());
        return up.is(CommonBlockTags.GEN_END_STONES) || up.is(BlockTags.LOGS) || up.is(BlockTags.LEAVES);
    }

    @Override
    public void growAdult(WorldGenLevel world, RandomSource random, BlockPos pos) {
        int h = BlocksHelper.downRay(world, pos, random.nextInt(24)) - 1;
        if (h > 2) {
            BlocksHelper.setWithoutUpdate(
                    world,
                    pos,
                    EndBlocks.BULB_VINE.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP)
            );
            for (int i = 1; i < h; i++) {
                BlocksHelper.setWithoutUpdate(
                        world,
                        pos.below(i),
                        EndBlocks.BULB_VINE.defaultBlockState()
                                           .setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE)
                );
            }
            BlocksHelper.setWithoutUpdate(
                    world,
                    pos.below(h),
                    EndBlocks.BULB_VINE.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM)
            );
        }
    }
}
