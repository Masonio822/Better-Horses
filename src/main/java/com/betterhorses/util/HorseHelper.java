package com.betterhorses.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;

public class HorseHelper {
    private HorseHelper() {
    }

    public static double convertToBlocksPerSecond(double internal) {
        return roundToTwoDecimals(internal * 42.16);
    }

    public static double convertToJumpBlocks(double internal) {
        return Math.round(((3.33 * internal * internal) + (2.33 * internal) - 0.3767) * 2.0) / 2.0;
    }

    public static double roundToTwoDecimals(float f) {
        return Math.round(f * 100.0) / 100.0;
    }

    public static double roundToTwoDecimals(double d) {
        return Math.round(d * 100.0) / 100.0;
    }

    public static Formatting getFormatForStat(RegistryEntry<EntityAttribute> attribute, double value) {
        if (attribute.equals(EntityAttributes.GENERIC_MAX_HEALTH)) {
            return value > 25.0 ? Formatting.DARK_GREEN : value < 20.0 ? Formatting.DARK_RED : Formatting.DARK_GRAY;
        } else if (attribute.equals(EntityAttributes.GENERIC_MOVEMENT_SPEED)) {
            return value > 0.277 ? Formatting.DARK_GREEN : value < 0.162 ? Formatting.DARK_RED : Formatting.DARK_GRAY;
        } else if (attribute.equals(EntityAttributes.GENERIC_JUMP_STRENGTH)) {
            return value > 0.85 ? Formatting.DARK_GREEN : value < 0.5 ? Formatting.DARK_RED : Formatting.DARK_GRAY;
        } else {
            return Formatting.DARK_GRAY;
        }
    }

    public static int getColor(int variant) {
        return variant & 0xFF;
    }

    public static int getMarkings(int variant) {
        return (variant >> 8) & 0xFF;
    }
}
