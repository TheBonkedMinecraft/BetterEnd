package org.betterx.betterend.blocks;

import org.betterx.bclib.interfaces.TagProvider;
import org.betterx.bclib.interfaces.tools.AddMineableShovel;
import org.betterx.ui.ColorUtil;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import java.util.Collections;
import java.util.List;

public class EndstoneDustBlock extends FallingBlock implements TagProvider, AddMineableShovel {
    @Environment(EnvType.CLIENT)
    private static final int COLOR = ColorUtil.color(226, 239, 168);

    public EndstoneDustBlock() {
        super(FabricBlockSettings
                .copyOf(Blocks.SAND)
                .mapColor(Blocks.END_STONE.defaultMaterialColor())
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(this));
    }

    @Environment(EnvType.CLIENT)
    public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
        return COLOR;
    }

    @Override
    public void addTags(List<TagKey<Block>> blockTags, List<TagKey<Item>> itemTags) {
        blockTags.add(CommonBlockTags.END_STONES);
    }
}
