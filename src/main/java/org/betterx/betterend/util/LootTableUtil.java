package org.betterx.betterend.util;

import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

public class LootTableUtil {
    private static final ResourceLocation COMMON = BetterEnd.makeID("chests/common");
    private static final ResourceLocation FOGGY_MUSHROOMLAND = BetterEnd.makeID("chests/foggy_mushroomland");
    private static final ResourceLocation CHORUS_FOREST = BetterEnd.makeID("chests/chorus_forest");
    private static final ResourceLocation SHADOW_FOREST = BetterEnd.makeID("chests/shadow_forest");
    private static final ResourceLocation LANTERN_WOODS = BetterEnd.makeID("chests/lantern_woods");
    private static final ResourceLocation UMBRELLA_JUNGLE = BetterEnd.makeID("chests/umbrella_jungle");
    private static final ResourceLocation FISHING_FISH = BetterEnd.makeID("gameplay/fishing/fish");
    private static final ResourceLocation FISHING_TREASURE = BetterEnd.makeID("gameplay/fishing/treasure");
    private static final ResourceLocation FISHING_JUNK = BetterEnd.makeID("gameplay/fishing/junk");

    private static final LootItemCondition.Builder IN_END
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_FOGGY_MUSHROOMLAND
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_CHORUS_FOREST
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_AMBER_LAND
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_GLOWING_GRASSLANDS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_LANTERN_WOODS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_MEGALAKE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_MEGALAKE_GROVE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_NEON_OASIS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_SHADOW_FOREST
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_SULPHUR_SPRINGS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    private static final LootItemCondition.Builder IN_UMBRELLA_JUNGLE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));

    public static void init() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {
            if (BuiltInLootTables.END_CITY_TREASURE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.setRolls(ConstantValue.exactly(1));
                builder.when(LootItemRandomChanceCondition.randomChance(0.2f));
                builder.add(LootItem.lootTableItem(Items.GHAST_TEAR));
                table.withPool(builder);

                builder = LootPool.lootPool();
                builder.setRolls(UniformGenerator.between(0, 3));
                builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN));
                builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS));
                builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER));
                builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA));
                table.withPool(builder);
            } else if (BuiltInLootTables.FISHING.equals(id)) {
                table.modifyPools((modifier) -> modifier.when(IN_END.invert()));
                table.withPool(LootPool.lootPool().when(IN_END).setRolls(ConstantValue.exactly(1.0F))
                        .add(LootTableReference.lootTableReference(FISHING_FISH).setWeight(85).setQuality(-1))
                        .add(LootTableReference.lootTableReference(FISHING_TREASURE).setWeight(5).setQuality(2))
                        .add(LootTableReference.lootTableReference(FISHING_JUNK).setWeight(10).setQuality(-2)));
            } else if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
                if (FISHING_FISH.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(EndItems.END_FISH_RAW));
                    table.withPool(builder);
                    return;
                } else if (FISHING_JUNK.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(EndItems.END_LILY_LEAF))
                            .add(LootItem.lootTableItem(Items.ENDER_PEARL))
                            .add(LootItem.lootTableItem(Items.CHORUS_FRUIT))
                            .add(LootItem.lootTableItem(EndItems.GELATINE))
                            .add(LootItem.lootTableItem(EndItems.CRYSTAL_SHARDS))
                            .add(LootItem.lootTableItem(EndItems.HYDRALUX_PETAL).when(IN_SULPHUR_SPRINGS));
                    addCharnia(builder);
                    table.withPool(builder);
                    return;
                } else if (FISHING_TREASURE.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(EndBlocks.TERMINITE.swordBlade))
                            .add(LootItem.lootTableItem(EndBlocks.TERMINITE.forgedPlate))
                            .add(LootItem.lootTableItem(EndBlocks.MENGER_SPONGE))
                            .add(LootItem.lootTableItem(Items.BOW)
                                    .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
                                    .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F)).allowTreasure()))
                            .add(LootItem.lootTableItem(Items.FISHING_ROD)
                                    .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
                                    .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F)).allowTreasure()))
                            .add(LootItem.lootTableItem(Items.BOOK)
                                    .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F)).allowTreasure()));
                    table.withPool(builder);
                    return;
                }
                addCommonItems(table);
                if (FOGGY_MUSHROOMLAND.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool();
                    builder.setRolls(UniformGenerator.between(4, 8));
                    builder.add(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)));
                    builder.add(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM_SAPLING));
                    builder.add(LootItem.lootTableItem(EndBlocks.BLUE_VINE_SEED));
                    table.withPool(builder);
                } else if (CHORUS_FOREST.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool();
                    builder.setRolls(UniformGenerator.between(4, 8));
                    builder.add(LootItem.lootTableItem(EndBlocks.PYTHADENDRON.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)));
                    builder.add(LootItem.lootTableItem(EndBlocks.PYTHADENDRON_SAPLING));
                    builder.add(LootItem.lootTableItem(EndBlocks.CHORUS_MUSHROOM));
                    table.withPool(builder);
                } else if (SHADOW_FOREST.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool();
                    builder.setRolls(UniformGenerator.between(4, 8));
                    builder.add(LootItem.lootTableItem(EndBlocks.DRAGON_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)));
                    builder.add(LootItem.lootTableItem(EndBlocks.DRAGON_TREE_SAPLING));
                    builder.add(LootItem.lootTableItem(EndBlocks.SHADOW_BERRY));
                    builder.add(LootItem.lootTableItem(EndItems.SHADOW_BERRY_RAW));
                    table.withPool(builder);
                } else if (LANTERN_WOODS.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool();
                    builder.setRolls(UniformGenerator.between(4, 8));
                    builder.add(LootItem.lootTableItem(EndBlocks.LUCERNIA.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)));
                    builder.add(LootItem.lootTableItem(EndBlocks.LUCERNIA_SAPLING));
                    builder.add(LootItem.lootTableItem(EndBlocks.BOLUX_MUSHROOM));
                    table.withPool(builder);
                } else if (UMBRELLA_JUNGLE.equals(id)) {
                    LootPool.Builder builder = LootPool.lootPool();
                    builder.setRolls(UniformGenerator.between(4, 8));
                    builder.add(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)));
                    builder.add(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE_SAPLING));
                    builder.add(LootItem.lootTableItem(EndBlocks.SMALL_JELLYSHROOM));
                    table.withPool(builder);
                }
            }
        });
    }

    public static ResourceLocation getTable(Holder<Biome> biome) {
        return COMMON;
    }

    private static void addCharnia(LootPool.Builder pool) {
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_CYAN)
                .when(IN_GLOWING_GRASSLANDS.or(IN_MEGALAKE).or(IN_MEGALAKE_GROVE).or(IN_NEON_OASIS)));
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_LIGHT_BLUE)
                .when(IN_FOGGY_MUSHROOMLAND.or(IN_GLOWING_GRASSLANDS).or(IN_MEGALAKE).or(IN_MEGALAKE_GROVE).or(IN_UMBRELLA_JUNGLE)));
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_GREEN)
                .when(IN_GLOWING_GRASSLANDS.or(IN_NEON_OASIS).or(IN_SULPHUR_SPRINGS).or(IN_UMBRELLA_JUNGLE)));
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_RED)
                .when(IN_AMBER_LAND.or(IN_LANTERN_WOODS).or(IN_NEON_OASIS)));
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_ORANGE)
                .when(IN_AMBER_LAND.or(IN_LANTERN_WOODS).or(IN_SULPHUR_SPRINGS)));
        pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_PURPLE)
                .when(IN_CHORUS_FOREST.or(IN_SHADOW_FOREST)));
    }

    private static void addCommonItems(LootTable.Builder table) {
        LootPool.Builder builder = LootPool.lootPool();
        builder.setRolls(UniformGenerator.between(0, 2));
        builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN));
        builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS));
        builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER));
        builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA));
        table.withPool(builder);

        builder = LootPool.lootPool();
        builder.setRolls(UniformGenerator.between(4, 8));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.ingot));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.rawOre));
        builder.add(LootItem.lootTableItem(Items.ENDER_PEARL));
        table.withPool(builder);

        builder = LootPool.lootPool();
        builder.setRolls(UniformGenerator.between(2, 4));
        builder.add(LootItem.lootTableItem(EndBlocks.TERMINITE.ingot));
        builder.add(LootItem.lootTableItem(EndItems.ENDER_SHARD));
        builder.add(LootItem.lootTableItem(EndBlocks.AURORA_CRYSTAL));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.axe));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.pickaxe));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.hoe));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.sword));
        builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.shovel));
        builder.add(LootItem.lootTableItem(Items.ENDER_EYE));
        builder.add(LootItem.lootTableItem(Blocks.OBSIDIAN));
        table.withPool(builder);

        builder = LootPool.lootPool();
        builder.setRolls(UniformGenerator.between(0, 4));
        builder.add(LootItem.lootTableItem(EndBlocks.FLAVOLITE_RUNED));
        builder.add(LootItem.lootTableItem(EndItems.AETERNIUM_INGOT));
        builder.add(LootItem.lootTableItem(EndItems.AMBER_GEM));
        builder.add(LootItem.lootTableItem(Items.END_CRYSTAL));
        builder.add(LootItem.lootTableItem(Items.GHAST_TEAR));
        table.withPool(builder);
    }
}
