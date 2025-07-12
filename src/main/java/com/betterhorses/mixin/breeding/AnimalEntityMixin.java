package com.betterhorses.mixin.breeding;

import com.betterhorses.duck.TrackedParents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings({"ConstantValue"})
    @Inject(at = @At("TAIL"), method = "breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V")
    private void saveParents(ServerWorld world, AnimalEntity other, PassiveEntity baby, CallbackInfo ci) {
        if (baby instanceof AbstractHorseEntity && other instanceof AbstractHorseEntity o && (AnimalEntity) (Object) this instanceof AbstractHorseEntity t) {
            TrackedParents tp = (TrackedParents) baby;
            tp.setParents(t, o);
        }
    }
}
