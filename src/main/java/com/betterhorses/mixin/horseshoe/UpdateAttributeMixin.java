package com.betterhorses.mixin.horseshoe;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
abstract class UpdateAttributeMixin {
    @Unique
    private final DefaultedList<ItemStack> syncedAnimalStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);

    @Redirect(method = "getEquipmentChanges", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;syncedBodyArmorStack:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.GETFIELD))
    private ItemStack useSlotSupportedMethod(LivingEntity instance, @Local EquipmentSlot slot) {
        return getSyncedAnimalArmorStack(slot);
    }

    @Unique
    private ItemStack getSyncedAnimalArmorStack(EquipmentSlot slot) {
        return syncedAnimalStacks.get(slot.getEntitySlotId());
    }

    @Redirect(method = "method_30120(Ljava/util/List;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;syncedBodyArmorStack:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.PUTFIELD))
    private void redirectToUseSyncedAnimalStacks(LivingEntity instance, ItemStack value, @Local(argsOnly = true) EquipmentSlot slot) {
        syncedAnimalStacks.set(slot.getEntitySlotId(), value);
    }
}
