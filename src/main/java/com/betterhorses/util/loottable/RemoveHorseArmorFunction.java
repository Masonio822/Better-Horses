package com.betterhorses.util.loottable;

import com.betterhorses.BetterHorses;
import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Random;

/**
 * Singleton class extending {@link LootFunction} to define a specific change to loot tables.
 * This function removes all horse armor from loot tables and replaces it with various amounts of the material of the armor.
 *
 * @see net.minecraft.item.AnimalArmorItem
 * @see net.minecraft.loot.LootTable
 * @see LootTableModifier
 */
public class RemoveHorseArmorFunction implements LootFunction {
    public static final RemoveHorseArmorFunction INSTANCE = new RemoveHorseArmorFunction();
    private static final MapCodec<RemoveHorseArmorFunction> CODEC = MapCodec.unit(RemoveHorseArmorFunction.INSTANCE);

    private static LootFunctionType<RemoveHorseArmorFunction> TYPE;


    private RemoveHorseArmorFunction() {
    }

    @Override
    public LootFunctionType<? extends LootFunction> getType() {
        return TYPE;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        Random r = new Random();
        ItemStack sub;

        if (itemStack.isOf(Items.LEATHER_HORSE_ARMOR)) {
            sub = new ItemStack(Items.LEATHER);
            sub.setCount(r.nextInt(0, 4));
            return sub;
        } else if (itemStack.isOf(Items.IRON_HORSE_ARMOR)) {
            sub = new ItemStack(Items.IRON_INGOT);
            sub.setCount(r.nextInt(0, 4));
            return sub;
        } else if (itemStack.isOf(Items.GOLDEN_HORSE_ARMOR)) {
            sub = new ItemStack(Items.GOLD_INGOT);
            sub.setCount(r.nextInt(0, 4));
            return sub;
        } else if (itemStack.isOf(Items.DIAMOND_HORSE_ARMOR)) {
            sub = new ItemStack(Items.DIAMOND);
            sub.setCount(r.nextInt(0, 4));
            return sub;
        }

        return itemStack;
    }

    public static void register() {
        TYPE = Registry.register(
                Registries.LOOT_FUNCTION_TYPE,
                BetterHorses.identifier("remove_horse_armor"),
                new LootFunctionType<>(CODEC)
        );
    }
}
