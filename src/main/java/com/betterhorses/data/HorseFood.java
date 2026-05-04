package com.betterhorses.data;

import net.minecraft.util.Identifier;

public record HorseFood(Identifier id, float heal, int age, int temper, boolean breed, double mutationChance) {
}
