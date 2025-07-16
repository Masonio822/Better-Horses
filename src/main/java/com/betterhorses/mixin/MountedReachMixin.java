package com.betterhorses.mixin;

import com.betterhorses.attributes.ModEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MountedReachMixin extends LivingEntity {
    protected MountedReachMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void useMountedBlockRange(CallbackInfoReturnable<Double> cir) {
        if (this.getVehicle() instanceof AbstractHorseEntity && this.getStackInHand(this.getActiveHand()).isIn(ItemTags.SHARP_WEAPON_ENCHANTABLE)) {
            cir.setReturnValue(this.getAttributeValue(ModEntityAttributes.PLAYER_MOUNTED_BLOCK_REACH));
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void useMountedEntityRange(CallbackInfoReturnable<Double> cir) {
        if (this.getVehicle() instanceof AbstractHorseEntity && this.getStackInHand(this.getActiveHand()).isIn(ItemTags.SHARP_WEAPON_ENCHANTABLE)) {
            cir.setReturnValue(this.getAttributeValue(ModEntityAttributes.PLAYER_MOUNTED_ENTITY_REACH));
        }
    }

    @ModifyReturnValue(method = "createPlayerAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder initMountedRange(DefaultAttributeContainer.Builder original) {
        return original
                .add(ModEntityAttributes.PLAYER_MOUNTED_ENTITY_REACH, 2)
                .add(ModEntityAttributes.PLAYER_MOUNTED_BLOCK_REACH, 3.5);
    }
}
