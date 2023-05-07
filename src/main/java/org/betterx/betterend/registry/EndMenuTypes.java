package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndStoneSmelter;
import org.betterx.betterend.client.gui.EndStoneSmelterMenu;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

public class EndMenuTypes {
    public final static MenuType<EndStoneSmelterMenu> END_STONE_SMELTER = registerSimple(
            BetterEnd.makeID(EndStoneSmelter.ID),
            EndStoneSmelterMenu::new
    );

    static <T extends AbstractContainerMenu> MenuType<T> registerSimple(
            ResourceLocation id,
            BiFunction<Integer, Inventory, T> factory
    ) {
        MenuType<T> type = new MenuType<>((syncId, inventory) -> factory.apply(syncId, inventory));
        return Registry.register(Registry.MENU, id, type);
    }

    public final static void ensureStaticallyLoaded() {
    }
}
