package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.interfaces.tools.AddMineableShears;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class GlowingPillarSeedBlock extends EndPlantWithAgeBlock implements AddMineableShears {

    public GlowingPillarSeedBlock() {
        super(FabricBlockSettings.of(Material.PLANT)
                                 .sound(SoundType.GRASS)
                                 .lightLevel(state -> state.getValue(AGE) * 3 + 3)
                                 .randomTicks()
                                 .noCollission()
                                 .offsetType(OffsetType.NONE));
    }

    @Override
    public void growAdult(WorldGenLevel world, RandomSource random, BlockPos pos) {
        int height = MHelper.randRange(1, 2, random);
        int h = BlocksHelper.upRay(world, pos, height + 2);
        if (h < height) {
            return;
        }

        MutableBlockPos mut = new MutableBlockPos().set(pos);
        BlockState roots = EndBlocks.GLOWING_PILLAR_ROOTS.defaultBlockState();
        if (height < 2) {
            BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
        } else {
            BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
            mut.move(Direction.UP);
            BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
        }
        mut.move(Direction.UP);
        BlocksHelper.setWithUpdate(
                world,
                mut,
                EndBlocks.GLOWING_PILLAR_LUMINOPHOR.defaultBlockState().setValue(BlueVineLanternBlock.NATURAL, true)
        );
        for (Direction dir : BlocksHelper.DIRECTIONS) {
            pos = mut.relative(dir);
            if (world.isEmptyBlock(pos)) {
                BlocksHelper.setWithUpdate(
                        world,
                        pos,
                        EndBlocks.GLOWING_PILLAR_LEAVES.defaultBlockState().setValue(BlockStateProperties.FACING, dir)
                );
            }
        }
        mut.move(Direction.UP);
        if (world.isEmptyBlock(mut)) {
            BlocksHelper.setWithUpdate(
                    world,
                    mut,
                    EndBlocks.GLOWING_PILLAR_LEAVES.defaultBlockState()
                                                   .setValue(BlockStateProperties.FACING, Direction.UP)
            );
        }
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.AMBER_MOSS);
    }
}
