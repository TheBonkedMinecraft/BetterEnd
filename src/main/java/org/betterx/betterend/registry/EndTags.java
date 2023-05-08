package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.ComposterAPI;
import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.SimpleLeavesBlock;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.basis.EndTerrainBlock;
import org.betterx.betterend.blocks.basis.PedestalBlock;
import org.betterx.betterend.item.tool.EndHammerItem;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;
import org.betterx.worlds.together.tag.v3.CommonItemTags;
import org.betterx.worlds.together.tag.v3.MineableTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;

import com.google.common.collect.Lists;

import java.util.List;

public class EndTags {
    // Table with common (c) tags:
    // https://fabricmc.net/wiki/tutorial:tags

    // Block Tags
    public static final TagKey<Block> PEDESTALS = TagManager.BLOCKS.makeTag(BetterEnd.MOD_ID, "pedestal");

    // Item Tags
    public static final TagKey<Item> ALLOYING_IRON = TagManager.ITEMS.makeTag(BetterEnd.MOD_ID, "alloying_iron");
    public static final TagKey<Item> ALLOYING_GOLD = TagManager.ITEMS.makeTag(BetterEnd.MOD_ID, "alloying_gold");
    public static final TagKey<Item> ALLOYING_COPPER = TagManager.ITEMS.makeTag(BetterEnd.MOD_ID, "alloying_copper");

    public static final TagKey<Block> BONEMEAL_SOURCE_DRAGON_BONE = TagManager.BLOCKS.makeTogetherTag(
            "bonemeal/source/dragon_bone"
    );
    public static final TagKey<Block> BONEMEAL_TARGET_DRAGON_BONE = TagManager.BLOCKS.makeTogetherTag(
            "bonemeal/target/dragon_bone"
    );

    public static void register() {
        addEndGround(EndBlocks.THALLASIUM.ore);
        addEndGround(EndBlocks.ENDSTONE_DUST);
        addEndGround(EndBlocks.AMBER_ORE);

        EndBlocks.getModBlocks().forEach(block -> {
            Properties properties = ((AbstractBlockAccessor) block).getSettings();
            Material material = ((AbstractBlockSettingsAccessor) properties).getMaterial();
            final Item item = block.asItem();

            if (material.equals(Material.STONE) || material.equals(Material.METAL) || material.equals(Material.HEAVY_METAL)) {
                TagManager.BLOCKS.add(MineableTags.PICKAXE, block);
            } else if (material.equals(Material.WOOD)) {
                TagManager.BLOCKS.add(MineableTags.AXE, block);
            } else if (material.equals(Material.LEAVES) || material.equals(Material.PLANT) || material.equals(Material.WATER_PLANT) || material.equals(
                    Material.SPONGE)) {
                TagManager.BLOCKS.add(MineableTags.HOE, block);
            } else if (material.equals(Material.SAND)) {
                TagManager.BLOCKS.add(MineableTags.SHOVEL, block);
            }

            if (block instanceof EndTerrainBlock) {
                addEndGround(block);
            } else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
                TagManager.BLOCKS.add(BlockTags.LEAVES, block);
                ComposterAPI.allowCompost(0.3f, item);
            } else if (block instanceof BaseVineBlock) {
                TagManager.BLOCKS.add(BlockTags.CLIMBABLE, block);
            } else if (block instanceof PedestalBlock) {
                TagManager.BLOCKS.add(PEDESTALS, block);
            }

            Material mat = block.defaultBlockState().getMaterial();
            if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
                ComposterAPI.allowCompost(0.1F, item);
            }
        });
        addEndGround(EndBlocks.CAVE_MOSS);

        List<Item> ITEM_HAMMERS = Lists.newArrayList();
        EndItems.getModItems().forEach(item -> {
            if (item.isEdible()) {
                FoodProperties food = item.getFoodProperties();
                if (food != null) {
                    float compost = food.getNutrition() * food.getSaturationModifier() * 0.18F;
                    ComposterAPI.allowCompost(compost, item);
                }
            }
            if (item instanceof EndHammerItem) {
                ITEM_HAMMERS.add(item);
            }
        });

        TagManager.BLOCKS.add(
                CommonBlockTags.END_STONES,
                EndBlocks.ENDER_ORE,
                EndBlocks.BRIMSTONE
        );
        TagManager.BLOCKS.add(CommonBlockTags.END_STONES, EndBlocks.BRIMSTONE);
        TagManager.BLOCKS.add(BlockTags.ANVIL, EndBlocks.AETERNIUM_ANVIL);
        TagManager.BLOCKS.add(BlockTags.BEACON_BASE_BLOCKS, EndBlocks.AETERNIUM_BLOCK);
        TagManager.ITEMS.add(ItemTags.BEACON_PAYMENT_ITEMS, EndItems.AETERNIUM_INGOT);
        TagManager.BLOCKS.add(
                BlockTags.DRAGON_IMMUNE,
                EndBlocks.ENDER_ORE,
                EndBlocks.ETERNAL_PEDESTAL,
                EndBlocks.FLAVOLITE_RUNED_ETERNAL,
                EndBlocks.FLAVOLITE_RUNED
        );
        TagManager.ITEMS.add(CommonItemTags.IRON_INGOTS, EndBlocks.THALLASIUM.ingot);

        TagManager.ITEMS.add(ALLOYING_IRON, Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
        TagManager.ITEMS.add(ALLOYING_GOLD, Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.RAW_GOLD);
        TagManager.ITEMS.add(ALLOYING_COPPER, Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);

        TagManager.ITEMS.add(ItemTags.FISHES, EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED);
    }

    public static void addEndGround(Block bl) {
        TagManager.BLOCKS.add(CommonBlockTags.END_STONES, bl);
    }

}
