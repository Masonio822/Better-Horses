package com.betterhorses.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
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
        if (baby instanceof AbstractHorseEntity && other instanceof AbstractHorseEntity && (AnimalEntity) (Object) this instanceof AbstractHorseEntity) {
            NbtCompound nbt = baby.writeNbt(new NbtCompound());

            NbtCompound thisNbt = this.writeNbt(new NbtCompound());
            NbtCompound addThis = new NbtCompound();

            NbtList thisAttributes = new NbtList();
            NbtCompound th = new NbtCompound();

            thisAttributes.add(new NbtCompound().putFloat());


            baby.readNbt(nbt);
        }
    }
}
