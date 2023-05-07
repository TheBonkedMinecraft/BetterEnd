package org.betterx.betterend.client.gui.slot;

import org.betterx.betterend.blocks.entities.EndStoneSmelterBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SmelterOutputSlot extends Slot {

    private final Player player;
    private int amount;

    public SmelterOutputSlot(Player player, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amount += amount;
        this.checkTakeAchievements(stack);
    }

    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.amount);
        if (!this.player.level.isClientSide && this.container instanceof EndStoneSmelterBlockEntity) {
            ((EndStoneSmelterBlockEntity) this.container).dropExperience(player);
        }
        this.amount = 0;
    }
}
