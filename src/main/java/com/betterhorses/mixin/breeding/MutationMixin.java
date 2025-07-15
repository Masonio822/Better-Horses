package com.betterhorses.mixin.breeding;

import com.betterhorses.duck.Mutable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractHorseEntity.class)
public abstract class MutationMixin extends AnimalEntity {
    protected MutationMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private RegistryEntry<EntityAttribute> attribute;

    @WrapOperation(method = "setChildAttribute", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;calculateAttributeBaseValue(DDDDLnet/minecraft/util/math/random/Random;)D"))
    private double mutate(double parentBase, double otherParentBase, double min, double max, Random random, Operation<Double> original, PassiveEntity other, AbstractHorseEntity child, RegistryEntry<EntityAttribute> attribute) {
        if (other instanceof AbstractHorseEntity) {
            if (this.attribute.equals(attribute)) {
                return original.call(parentBase + (parentBase * 0.1), otherParentBase + (otherParentBase * 0.1), min, max, random);
            }
        }
        return original.call(parentBase, otherParentBase, min, max, random);
    }

    @Inject(method = "setChildAttributes", at = @At("HEAD"))
    private void runMutationChance(PassiveEntity other, AbstractHorseEntity child, CallbackInfo ci) {
        if (other instanceof AbstractHorseEntity) {
            double mutationChance = (((Mutable) this).getMutationChance() + ((Mutable) other).getMutationChance()) / 2;

            if (mutationChance / 100 > Math.random()) {
                attribute = Util.getRandom(
                        List.of(EntityAttributes.GENERIC_MAX_HEALTH, EntityAttributes.GENERIC_JUMP_STRENGTH, EntityAttributes.GENERIC_MOVEMENT_SPEED),
                        this.random
                );
            }
        }
    }
}
