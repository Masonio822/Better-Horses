package com.betterhorses.item;

import com.betterhorses.BetterHorses;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


/**
 * Standard class for registering mod items
 *
 * @see HorseboxItem
 * @see BreedingChartItem
 */
public class ModItems {

    public static final Item HORSEBOX = registerItem("horsebox", new HorseboxItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item BREEDING_CHART = registerItem("breeding_chart", new BreedingChartItem(new Item.Settings().maxCount(1)));

    public static void registerItems() {
        BetterHorses.LOGGER.info("Registering items for " + BetterHorses.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(HORSEBOX);
            entries.add(BREEDING_CHART);
        });
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BetterHorses.MOD_ID, name), item);
    }
}
