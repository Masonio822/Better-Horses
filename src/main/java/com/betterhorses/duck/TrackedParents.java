package com.betterhorses.duck;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface TrackedParents {

    NbtElement getParentsNbt();

    void setParentsNbt(NbtCompound parentsNbt);

    void setParents(AbstractHorseEntity horse1, AbstractHorseEntity horse2);
}
