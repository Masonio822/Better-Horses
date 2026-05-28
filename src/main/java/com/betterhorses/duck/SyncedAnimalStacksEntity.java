package com.betterhorses.duck;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface SyncedAnimalStacksEntity {
    ItemStack better_Horses$getSyncedAnimalArmorStack(EquipmentSlot slot);

    DefaultedList<ItemStack> better_Horses$getSyncedAnimalStacks();

    ItemStack better_Horses$setSyncedAnimalStack(EquipmentSlot slot, ItemStack stack);
}
