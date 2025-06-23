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

/**
 * Singleton class extending {@link LootFunction} to define a specific change to loot tables.
 * This function removes all saddles and replaces it with various amounts of leather
 *
 * @see net.minecraft.item.SaddleItem
 * @see net.minecraft.loot.LootTable
 * @see LootTableModifer
 */
public class RemoveSaddlesFunction implements LootFunction {
    public static final RemoveSaddlesFunction INSTANCE = new RemoveSaddlesFunction();
    private static final MapCodec<RemoveSaddlesFunction> CODEC = MapCodec.unit(RemoveSaddlesFunction.INSTANCE);

    private static LootFunctionType<RemoveSaddlesFunction> TYPE;


    private RemoveSaddlesFunction() {
    }

    @Override
    public LootFunctionType<? extends LootFunction> getType() {
        return TYPE;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.isOf(Items.SADDLE)) {
            ItemStack sub = new ItemStack(Items.LEATHER);
            //Set leather count to anywhere from 1-3, inclusive
            sub.setCount((int) Math.floor((Math.random() * 3) + 1));
            return sub;
        }

        return itemStack;
    }

    public static void register() {
        TYPE = Registry.register(
                Registries.LOOT_FUNCTION_TYPE,
                BetterHorses.identifier("remove_saddles"),
                new LootFunctionType<>(CODEC)
        );
    }
}
