package com.betterhorses.mixin.horseshoe;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AttributeModifierSlot.class)
enum AttributeModifierSlotMixin {
    BETTER_HORSES_ANIMAL_FEET(10,"animal_feet", EquipmentSlot.BETTER_HORSES_ANIMAL_FEET);

    @Shadow
    AttributeModifierSlotMixin(int id, String name, EquipmentSlot slot) {}
}
