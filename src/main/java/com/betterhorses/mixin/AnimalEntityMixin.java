package com.betterhorses.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
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
        if (baby instanceof AbstractHorseEntity && other instanceof AbstractHorseEntity && (AnimalEntity) (Object) this instanceof AbstractHorseEntity t) {
            NbtCompound nbt = baby.writeNbt(new NbtCompound());

            NbtCompound addThis = new NbtCompound();
            addThis.putInt("Variant", t.writeNbt(new NbtCompound()).getInt("Variant"));

            NbtCompound thisAttributes = new NbtCompound();
            thisAttributes.putDouble("Health", t.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
            thisAttributes.putDouble("Speed", t.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            thisAttributes.putDouble("Jump", t.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
            addThis.put("Attributes", thisAttributes);

            NbtCompound addOther = new NbtCompound();
            addOther.putInt("Variant", other.writeNbt(new NbtCompound()).getInt("Variant"));

            NbtCompound otherAttributes = new NbtCompound();
            otherAttributes.putDouble("Health", other.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
            otherAttributes.putDouble("Speed", other.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            otherAttributes.putDouble("Jump", other.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
            addOther.put("Attributes", otherAttributes);

            NbtList parents = new NbtList();
            parents.add(addThis);
            parents.add(addOther);
            nbt.put("Parents", parents);

            baby.readNbt(nbt);
            System.out.println(baby.writeNbt(new NbtCompound()).get("Parents"));
        }
    }
}
