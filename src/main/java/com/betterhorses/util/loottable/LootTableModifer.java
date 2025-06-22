package com.betterhorses.util.loottable;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

public class LootTableModifer {

    public static void modifyLootTables() {
        RemoveSaddlesFunction.register();
        RemoveHorseArmorFunction.register();
        removeSaddles();
        removeHorseArmor();
    }

    private static void removeHorseArmor() {
        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            builder.apply(RemoveHorseArmorFunction.INSTANCE);
        });
    }

    private static void removeSaddles() {
        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            builder.apply(RemoveSaddlesFunction.INSTANCE);
        });
    }
}
