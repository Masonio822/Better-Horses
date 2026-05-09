package com.betterhorses.duck;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface HorseshoeEquipable {
    boolean better_Horses_1_21_1$hasHorseshoe();

    void better_Horses_1_21_1$setHorseshoe(ItemStack stack);
    ItemStack better_Horses_1_21_1$getHorseshoe();

    Inventory better_Horses_1_21_1$getHorseshoeInventory();
}
