package com.betterhorses.duck;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public interface TrackedParents {

    Optional<NbtCompound> getParentsNbt();

    void setParentsNbt(NbtCompound parentsNbt);

    void setParents(AbstractHorseEntity horse1, AbstractHorseEntity horse2);
}
