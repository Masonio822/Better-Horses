package com.betterhorses.item;

import com.betterhorses.BetterHorses;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
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
    public static final Item NETHERITE_HORSE_ARMOR = registerItem("netherite_horse_armor",
            new AnimalArmorItem(ArmorMaterials.NETHERITE, AnimalArmorItem.Type.EQUESTRIAN, false, new Item.Settings().maxCount(1).fireproof()));

    public static void registerItems() {
        BetterHorses.LOGGER.info("Registering items for " + BetterHorses.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.SADDLE, HORSEBOX);
            entries.addAfter(HORSEBOX, BREEDING_CHART);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.DIAMOND_HORSE_ARMOR, NETHERITE_HORSE_ARMOR);
        });
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BetterHorses.MOD_ID, name), item);
    }
}
