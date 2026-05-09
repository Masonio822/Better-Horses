package com.betterhorses.mixin.horseshoe;

import com.betterhorses.duck.HorseshoeEquipable;
import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MobEntity.class)
abstract class UseAnimalFeetSlotMixin extends LivingEntity {
    protected UseAnimalFeetSlotMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "getEquippedStack", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/MobEntity;bodyArmor:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.GETFIELD))
    private ItemStack redirectToUseSyncedAnimalStacks(MobEntity instance, EquipmentSlot slot) {
        if (instance instanceof AbstractHorseEntity horse && slot == EquipmentSlot.BETTER_HORSES_ANIMAL_FEET) {
            return ((HorseshoeEquipable) horse).better_Horses_1_21_1$getHorseshoe();
        } else {
            return instance.getBodyArmor();
        }
    }

    @Inject(method = "equipStack", at = @At("HEAD"), cancellable = true)
    private void overrideEquipStackBehavior(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        //Guard clause so that no vanilla behavior is affected
        if (slot != EquipmentSlot.BETTER_HORSES_ANIMAL_FEET) return;

        if (((MobEntity) (Object) this) instanceof AbstractHorseEntity horse) {
            processEquippedStack(stack);

            HorseshoeEquipable horseshoeEquipable = (HorseshoeEquipable) horse;
            ItemStack itemStack = horseshoeEquipable.better_Horses_1_21_1$getHorseshoe();
            horseshoeEquipable.better_Horses_1_21_1$setHorseshoe(stack);
            horse.onEquipStack(slot, itemStack, stack);

            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "getAllArmorItems", at = @At("RETURN"))
    private Iterable<ItemStack> factorInHorseshoeSlot(Iterable<ItemStack> original) {
        if (((MobEntity) (Object) this) instanceof AbstractHorseEntity horse && ((HorseshoeEquipable) horse).better_Horses_1_21_1$hasHorseshoe()) {
            Iterables.addAll(List.of(((HorseshoeEquipable) horse).better_Horses_1_21_1$getHorseshoe()), original);
        }
        return original;
    }
}
