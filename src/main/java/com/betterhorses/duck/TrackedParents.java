package com.betterhorses.duck;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public interface TrackedParents {

    Optional<NbtCompound> better_Horses_1_21_1$getParentsNbt();

    void better_Horses_1_21_1$setParentsNbt(NbtCompound parentsNbt);

    void better_Horses_1_21_1$setParents(AbstractHorseEntity horse1, AbstractHorseEntity horse2);
}
