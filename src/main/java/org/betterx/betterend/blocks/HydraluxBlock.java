package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.UnderwaterPlantBlock;
import org.betterx.bclib.interfaces.tools.AddMineableShears;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.EndBlockProperties.HydraluxShape;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class HydraluxBlock extends UnderwaterPlantBlock implements AddMineableShears {

    public static final EnumProperty<HydraluxShape> SHAPE = EndBlockProperties.HYDRALUX_SHAPE;

    public HydraluxBlock() {
        super(baseUnderwaterPlantSettings()
                .lightLevel((state) -> state.getValue(SHAPE).hasGlow() ? 15 : 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState down = world.getBlockState(pos.below());
        HydraluxShape shape = state.getValue(SHAPE);
        if (shape == HydraluxShape.FLOWER_BIG_TOP || shape == HydraluxShape.FLOWER_SMALL_TOP) {
            return down.is(this);
        } else if (shape == HydraluxShape.ROOTS) {
            return down.is(EndBlocks.SULPHURIC_ROCK.stone) && world.getBlockState(pos.above()).is(this);
        } else {
            return down.is(this) && world.getBlockState(pos.above()).is(this);
        }
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(EndBlocks.HYDRALUX_SAPLING);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        HydraluxShape shape = state.getValue(SHAPE);
        if (shape == HydraluxShape.FLOWER_BIG_BOTTOM || shape == HydraluxShape.FLOWER_SMALL_BOTTOM) {
            return Lists.newArrayList(new ItemStack(
                    EndItems.HYDRALUX_PETAL,
                    MHelper.randRange(1, 4, MHelper.RANDOM_SOURCE)
            ));
        } else if (shape == HydraluxShape.ROOTS) {
            return Lists.newArrayList(new ItemStack(
                    EndBlocks.HYDRALUX_SAPLING,
                    MHelper.randRange(1, 2, MHelper.RANDOM_SOURCE)
            ));
        }
        return Collections.emptyList();
    }
}
