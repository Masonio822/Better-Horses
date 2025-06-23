package com.betterhorses.util;

import com.betterhorses.BetterHorses;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Defines various utility tags for the mod
 */
public class ModTags {
    public static class Items {
        public static final TagKey<Item> HORSE_ARMOR = createTag("horse_armor");

        //Use Itemstack.isIn() to check tag
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(BetterHorses.MOD_ID, name));
        }
    }
}
