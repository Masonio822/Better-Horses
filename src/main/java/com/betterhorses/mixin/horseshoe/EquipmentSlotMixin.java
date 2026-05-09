package com.betterhorses.mixin.horseshoe;

import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EquipmentSlot.class)
enum EquipmentSlotMixin {
    BETTER_HORSES_ANIMAL_FEET(EquipmentSlot.Type.ANIMAL_ARMOR, 1, 1, 6, "animal_feet");

    @Shadow
    EquipmentSlotMixin(EquipmentSlot.Type type, int entityId, int maxCount, int armorStandId, String name) {}
}
