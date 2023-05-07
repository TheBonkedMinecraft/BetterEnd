package org.betterx.betterend.item;

import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;

import java.util.UUID;

public class CrystaliteLeggings extends CrystaliteArmor {

    public CrystaliteLeggings() {
        super(EquipmentSlot.LEGS, EndItems.makeEndItemSettings().rarity(Rarity.RARE));
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[EquipmentSlot.LEGS.getIndex()];
        addAttributeModifier(
                Attributes.MAX_HEALTH,
                new AttributeModifier(uuid, "Armor health boost", 4.0, AttributeModifier.Operation.ADDITION)
        );
    }
}
