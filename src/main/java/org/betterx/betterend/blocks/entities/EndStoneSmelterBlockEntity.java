package org.betterx.betterend.blocks.entities;

import org.betterx.bclib.recipes.AlloyingRecipe;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndStoneSmelter;
import org.betterx.betterend.client.gui.EndStoneSmelterMenu;
import org.betterx.betterend.registry.EndBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.registry.FuelRegistry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EndStoneSmelterBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    private static final int[] TOP_SLOTS = new int[]{
            EndStoneSmelterMenu.INGREDIENT_SLOT_A,
            EndStoneSmelterMenu.INGREDIENT_SLOT_B
    };
    private static final int[] JUST_A = new int[]{
            EndStoneSmelterMenu.INGREDIENT_SLOT_A,
    };
    private static final int[] BOTTOM_SLOTS = new int[]{EndStoneSmelterMenu.FUEL_SLOT, EndStoneSmelterMenu.RESULT_SLOT};
    private static final int[] SIDE_SLOTS = new int[]{
            EndStoneSmelterMenu.FUEL_SLOT
    };
    private static final Map<Item, Integer> AVAILABLE_FUELS = Maps.newHashMap();

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    protected NonNullList<ItemStack> inventory;
    protected final ContainerData propertyDelegate;
    private Recipe<?> lastRecipe;
    private int smeltTimeTotal;
    private int smeltTime;
    private int burnTime;
    private int fuelTime;

    public EndStoneSmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EndBlockEntities.END_STONE_SMELTER, blockPos, blockState);
        this.inventory = NonNullList.withSize(EndStoneSmelterMenu.SLOT_COUNT, ItemStack.EMPTY);
        this.recipesUsed = new Object2IntOpenHashMap<>();
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return EndStoneSmelterBlockEntity.this.burnTime;
                    case 1:
                        return EndStoneSmelterBlockEntity.this.fuelTime;
                    case 2:
                        return EndStoneSmelterBlockEntity.this.smeltTime;
                    case 3:
                        return EndStoneSmelterBlockEntity.this.smeltTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        EndStoneSmelterBlockEntity.this.burnTime = value;
                        break;
                    case 1:
                        EndStoneSmelterBlockEntity.this.fuelTime = value;
                        break;
                    case 2:
                        EndStoneSmelterBlockEntity.this.smeltTime = value;
                        break;
                    case 3:
                        EndStoneSmelterBlockEntity.this.smeltTimeTotal = value;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    private boolean isBurning() {
        return burnTime > 0;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> iterator = inventory.iterator();
        ItemStack itemStack;
        do {
            if (!iterator.hasNext()) {
                return true;
            }
            itemStack = iterator.next();
        } while (itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = inventory.get(slot);
        boolean stackValid = !stack.isEmpty() && stack.sameItem(itemStack) && ItemStack.tagMatches(stack, itemStack);
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if ((slot == EndStoneSmelterMenu.INGREDIENT_SLOT_A || slot == EndStoneSmelterMenu.INGREDIENT_SLOT_B) && !stackValid) {
            smeltTimeTotal = getSmeltTime();
            smeltTime = 0;
            setChanged();
        }
    }

    protected int getSmeltTime() {
        if (level == null) return 200;
        int smeltTime = level.getRecipeManager()
                             .getRecipeFor(AlloyingRecipe.TYPE, this, level)
                             .map(AlloyingRecipe::getSmeltTime)
                             .orElse(0);
        if (smeltTime == 0) {
            smeltTime = level.getRecipeManager()
                             .getRecipeFor(RecipeType.BLASTING, this, level)
                             .map(BlastingRecipe::getCookingTime)
                             .orElse(200);
            smeltTime /= 1.5;
        }
        return smeltTime;
    }

    public void dropExperience(Player player) {
        if (level == null) return;
        List<Recipe<?>> list = Lists.newArrayList();
        for (Entry<ResourceLocation> entry : recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                if (recipe instanceof AlloyingRecipe) {
                    AlloyingRecipe alloying = (AlloyingRecipe) recipe;
                    dropExperience(player.level, player.position(), entry.getIntValue(), alloying.getExperience());
                } else {
                    BlastingRecipe blasting = (BlastingRecipe) recipe;
                    dropExperience(player.level, player.position(), entry.getIntValue(), blasting.getExperience());
                }
            });
        }
        player.awardRecipes(list);
        recipesUsed.clear();
    }

    private void dropExperience(Level world, Vec3 vec3d, int count, float amount) {
        int expTotal = Mth.floor(count * amount);
        float g = Mth.frac(count * amount);
        if (g != 0.0F && Math.random() < g) {
            expTotal++;
        }

        while (expTotal > 0) {
            int expVal = ExperienceOrb.getExperienceValue(expTotal);
            expTotal -= expVal;
            world.addFreshEntity(new ExperienceOrb(world, vec3d.x, vec3d.y, vec3d.z, expVal));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (level != null && level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(String.format("block.%s.%s", BetterEnd.MOD_ID, EndStoneSmelter.ID));
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new EndStoneSmelterMenu(syncId, playerInventory, this, propertyDelegate);
    }

    public static void tick(
            Level tickLevel,
            BlockPos tickPos,
            BlockState tickState,
            EndStoneSmelterBlockEntity blockEntity
    ) {
        if (tickLevel == null) return;

        boolean initialBurning = blockEntity.isBurning();
        if (initialBurning) {
            blockEntity.burnTime--;
        }

        boolean burning = initialBurning;
        if (!tickLevel.isClientSide) {
            ItemStack fuel = blockEntity.inventory.get(EndStoneSmelterMenu.FUEL_SLOT);
            if (!burning && (fuel.isEmpty()
                    || blockEntity.inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).isEmpty()
                    && blockEntity.inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_B).isEmpty())
            ) {
                if (blockEntity.smeltTime > 0) {
                    blockEntity.smeltTime = Mth.clamp(blockEntity.smeltTime - 2, 0, blockEntity.smeltTimeTotal);
                }
            } else {
                Recipe<?> recipe = tickLevel.getRecipeManager()
                                            .getRecipeFor(AlloyingRecipe.TYPE, blockEntity, tickLevel)
                                            .orElse(null);
                if (recipe == null) {
                    recipe = tickLevel.getRecipeManager()
                                      .getRecipeFor(RecipeType.BLASTING, blockEntity, tickLevel)
                                      .orElse(null);
                }
                boolean accepted = blockEntity.canAcceptRecipeOutput(recipe);
                if (!burning && accepted) {
                    blockEntity.burnTime = EndStoneSmelterBlockEntity.getFuelTime(fuel);
                    blockEntity.fuelTime = blockEntity.burnTime;
                    burning = blockEntity.isBurning();
                    if (burning) {
                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);
                            if (fuel.isEmpty()) {
                                Item remainFuel = item.getCraftingRemainingItem();
                                blockEntity.inventory.set(
                                        EndStoneSmelterMenu.FUEL_SLOT,
                                        remainFuel == null ? ItemStack.EMPTY : new ItemStack(remainFuel)
                                );
                            }
                        }
                        blockEntity.setChanged();
                    }
                }

                if (burning && accepted) {
                    blockEntity.smeltTime++;
                    if (blockEntity.smeltTime == blockEntity.smeltTimeTotal) {
                        blockEntity.smeltTime = 0;
                        blockEntity.smeltTimeTotal = blockEntity.getSmeltTime();
                        blockEntity.craftRecipe(recipe);
                        blockEntity.setChanged();
                    }
                } else {
                    blockEntity.smeltTime = 0;
                }
            }
            burning = blockEntity.isBurning();
            if (initialBurning != burning) {
                tickLevel.setBlock(tickPos, tickState.setValue(EndStoneSmelter.LIT, burning), 3);
                blockEntity.setChanged();
            }
        }
    }

    protected boolean canAcceptRecipeOutput(Recipe<?> recipe) {
        if (recipe == null) return false;
        boolean validInput;
        if (recipe instanceof AlloyingRecipe) {
            validInput = !inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).isEmpty()
                    && !inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_B).isEmpty();
        } else {
            validInput = !inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).isEmpty()
                    || !inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_B).isEmpty();
        }
        if (validInput) {
            ItemStack result = recipe.getResultItem();
            if (result.isEmpty()) {
                return false;
            }
            ItemStack output = this.inventory.get(EndStoneSmelterMenu.RESULT_SLOT);
            int outCount = output.getCount();
            int total = outCount + result.getCount();
            if (output.isEmpty()) {
                return true;
            }
            if (!output.sameItem(result)) {
                return false;
            }
            if (outCount < getMaxStackSize() && outCount < output.getMaxStackSize()) {
                return getMaxStackSize() >= total;
            }
            return output.getCount() < result.getMaxStackSize();
        }
        return false;
    }

    private void craftRecipe(Recipe<?> recipe) {
        if (recipe == null || !canAcceptRecipeOutput(recipe)) return;

        ItemStack result = recipe.getResultItem();
        ItemStack output = inventory.get(EndStoneSmelterMenu.RESULT_SLOT);
        if (output.isEmpty()) {
            inventory.set(EndStoneSmelterMenu.RESULT_SLOT, result.copy());
        } else if (output.getItem() == result.getItem()) {
            output.grow(result.getCount());
        }

        assert this.level != null;
        if (!this.level.isClientSide) {
            setRecipeUsed(recipe);
        }

        if (recipe instanceof AlloyingRecipe) {
            inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).shrink(1);
            inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_B).shrink(1);
        } else {
            if (!inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).isEmpty()) {
                inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_A).shrink(1);
            } else {
                inventory.get(EndStoneSmelterMenu.INGREDIENT_SLOT_B).shrink(1);
            }
        }
    }

    @Override
    public void fillStackedContents(StackedContents finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.accountStack(itemStack);
        }
    }

    @Override
    public void setRecipeUsed(Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeId = recipe.getId();
            recipesUsed.addTo(recipeId, 1);
            lastRecipe = recipe;
        }
    }

    @Override
    public Recipe<?> getRecipeUsed() {
        return this.lastRecipe;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
//        var facing = getBlockState().getValue(EndStoneSmelter.FACING);
//        if (side == facing) return JUST_A;
        return switch (side) {
            case DOWN -> BOTTOM_SLOTS;
            case UP -> TOP_SLOTS;
            default -> SIDE_SLOTS;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == EndStoneSmelterMenu.FUEL_SLOT) {
            return stack.getItem() == Items.BUCKET;
        }
        return true;
    }

    public static int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        Item item = fuel.getItem();
        return AVAILABLE_FUELS.getOrDefault(item, getFabricFuel(fuel));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, inventory);
        burnTime = tag.getShort("BurnTime");
        fuelTime = tag.getShort("FuelTime");
        smeltTime = tag.getShort("SmeltTime");
        smeltTimeTotal = tag.getShort("SmeltTimeTotal");
        CompoundTag compoundTag = tag.getCompound("RecipesUsed");
        for (String id : compoundTag.getAllKeys()) {
            recipesUsed.put(new ResourceLocation(id), compoundTag.getInt(id));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putShort("BurnTime", (short) burnTime);
        tag.putShort("FuelTime", (short) fuelTime);
        tag.putShort("SmeltTime", (short) smeltTime);
        tag.putShort("SmeltTimeTotal", (short) smeltTimeTotal);
        ContainerHelper.saveAllItems(tag, inventory);
        CompoundTag usedRecipes = new CompoundTag();
        recipesUsed.forEach((identifier, integer) -> usedRecipes.putInt(identifier.toString(), integer));
        tag.put("RecipesUsed", usedRecipes);
    }

    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == EndStoneSmelterMenu.RESULT_SLOT) {
            return false;
        } else if (slot != EndStoneSmelterMenu.FUEL_SLOT) {
            return true;
        }
        ItemStack itemStack = this.inventory.get(EndStoneSmelterMenu.FUEL_SLOT);
        return canUseAsFuel(stack) || stack.getItem() == Items.BUCKET && itemStack.getItem() != Items.BUCKET;
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return AVAILABLE_FUELS.containsKey(stack.getItem()) || getFabricFuel(stack) > 2000;
    }

    public static void registerFuel(ItemLike fuel, int time) {
        AVAILABLE_FUELS.put(fuel.asItem(), time);
    }

    public static Map<Item, Integer> availableFuels() {
        return AVAILABLE_FUELS;
    }

    private static int getFabricFuel(ItemStack stack) {
        Integer ticks = FuelRegistry.INSTANCE.get(stack.getItem());
        return ticks == null ? 0 : ticks;
    }

    static {
        AbstractFurnaceBlockEntity.getFuel().forEach((item, time) -> {
            if (time >= 2000) {
                registerFuel(item, time);
            }
        });
    }
}
