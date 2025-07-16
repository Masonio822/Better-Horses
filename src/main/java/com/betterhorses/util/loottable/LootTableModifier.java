package com.betterhorses.util.loottable;

import com.betterhorses.config.CommonConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

/**
 * Executive class for modifying loot tables for the mod. Uses {@link net.minecraft.loot.function.LootFunction} to change various loot tables
 *
 * @see net.minecraft.loot.LootTable
 * @see RemoveHorseArmorFunction
 * @see RemoveSaddlesFunction
 */
public class LootTableModifier {

    public static void modifyLootTables() {
        RemoveSaddlesFunction.register();
        RemoveHorseArmorFunction.register();
        if (CommonConfig.INSTANCE.removeSaddles) {
            LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) ->
                    builder.apply(RemoveSaddlesFunction.INSTANCE));
        }
        if (CommonConfig.INSTANCE.removeHorseArmor) {
            LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) ->
                    builder.apply(RemoveHorseArmorFunction.INSTANCE));
        }
    }
}
