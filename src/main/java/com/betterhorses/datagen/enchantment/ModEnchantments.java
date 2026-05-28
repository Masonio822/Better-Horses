package com.betterhorses.datagen.enchantment;

import com.betterhorses.BetterHorses;
import com.betterhorses.attributes.ModEntityAttributes;
import com.betterhorses.util.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> JOUSTING =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(BetterHorses.MOD_ID, "jousting"));
    public static final Identifier JOUSTED_REACH_MODIFIER_ID = Identifier.of(BetterHorses.MOD_ID, "jousted_reach_modifier");

    public static final RegistryKey<Enchantment> SWIFT_HOOVES =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(BetterHorses.MOD_ID, "swift_hooves"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, JOUSTING, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        6,
                        AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES, new AttributeEnchantmentEffect(
                        Identifier.of(BetterHorses.MOD_ID, "enchantment.jousting"),
                        ModEntityAttributes.PLAYER_MOUNTED_ENTITY_REACH,
                        EnchantmentLevelBasedValue.linear(1.0f),
                        EntityAttributeModifier.Operation.ADD_VALUE
                ))
                .addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES, new AttributeEnchantmentEffect(
                        Identifier.of(BetterHorses.MOD_ID, "enchantment.jousting"),
                        ModEntityAttributes.PLAYER_MOUNTED_BLOCK_REACH,
                        EnchantmentLevelBasedValue.linear(1.0f),
                        EntityAttributeModifier.Operation.ADD_VALUE
                ))
        );

        register(registerable, SWIFT_HOOVES, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ModTags.Items.HORSESHOE),
                2,
                3,
                Enchantment.leveledCost(10, 10),
                Enchantment.leveledCost(25, 10),
                4,
                AttributeModifierSlot.BETTER_HORSES_ANIMAL_FEET
                )).addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES, new AttributeEnchantmentEffect(
                        Identifier.of(BetterHorses.MOD_ID, "enchantment.swift_hooves"),
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        EnchantmentLevelBasedValue.linear(0.1f),
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ))
        );
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
