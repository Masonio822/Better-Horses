package com.betterhorses.item;

import com.betterhorses.BetterHorses;
import com.betterhorses.sound.ModSounds;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

//This issue is handled internally by mojang; nothing needs to be done with it
public class HorseshoeItem extends AnimalArmorItem {
    public HorseshoeItem(RegistryEntry<ArmorMaterial> material, Type type, boolean hasOverlay, Settings settings) {
        super(material, type, hasOverlay, settings);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.BETTER_HORSES_ANIMAL_FEET;
    }

    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return ModSounds.EQUIP_HORSESHOE;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return AttributeModifiersComponent.builder().add(
                EntityAttributes.GENERIC_JUMP_STRENGTH,
                new EntityAttributeModifier(BetterHorses.identifier("horseshoe"), 0.25, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                AttributeModifierSlot.BETTER_HORSES_ANIMAL_FEET
        ).build();
    }
}
