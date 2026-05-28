package com.betterhorses.datagen;

import com.betterhorses.datagen.enchantment.ModEnchantments;
import com.betterhorses.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    protected ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translations) {
        translations.add(ModItems.HORSEBOX, "Horsebox");
        translations.add(ModItems.NETHERITE_HORSE_ARMOR, "Netherite Horse Armor");
        translations.add(ModItems.HORSESHOE, "Horseshoe");
        translations.add(ModItems.BREEDING_CHART, "Breeding Chart");

        translations.add("sounds.betterhorses.horsebox_unzip", "Horsebox Unzips");
        translations.add("sounds.betterhorses.clipboard_use", "Clipboard Opens");

        translations.add("color.minecraft.horse.0", "White");
        translations.add("color.minecraft.horse.1", "Creamy");
        translations.add("color.minecraft.horse.2", "Chestnut");
        translations.add("color.minecraft.horse.3", "Brown");
        translations.add("color.minecraft.horse.4", "Black");
        translations.add("color.minecraft.horse.5", "Gray");
        translations.add("color.minecraft.horse.6", "Dark Brown");

        translations.add("marking.minecraft.horse.0", "Clean");
        translations.add("marking.minecraft.horse.1", "White");
        translations.add("marking.minecraft.horse.2", "White Field");
        translations.add("marking.minecraft.horse.3", "White Dots");
        translations.add("marking.minecraft.horse.4", "Black Dots");

        translations.add("tooltip.betterhorses.color", "Color: ");
        translations.add("tooltip.betterhorses.marking", "Marking: ");

        translations.add("tag.item.betterhorses.horse_armor", "Horse Armors");
        translations.add("tag.item.betterhorses.stabilizes_horse", "Stabilizes Horse");
        translations.add("tag.item.betterhorses.horseshoe", "Horseshoe");
        translations.add("tag.item.betterhorses.foot_armor_and_horseshoe", "Animal Foot Enchantable");

        translations.add("text.betterhorses.info", "Info");
        translations.add("text.betterhorses.name", "Name: %s");
        translations.add("text.betterhorses.type", "Type: %s");
        translations.add("text.betterhorses.attribute", "Attributes");
        translations.add("text.betterhorses.health", "Health: %s h");
        translations.add("text.betterhorses.speed", "Speed: %s b/s");
        translations.add("text.betterhorses.jump", "Jump Height: %s b");
        translations.add("text.betterhorses.coloration", "Coloration");
        translations.add("text.betterhorses.wild", "Wild");
        translations.add("text.betterhorses.male", "Male");
        translations.add("text.betterhorses.female", "Female");
        translations.add("text.betterhorses.child", "Child");

        translations.addEnchantment(ModEnchantments.JOUSTING, "Jousting");
        translations.addEnchantment(ModEnchantments.SWIFT_HOOVES, "Swift Hooves");

        translations.add("attribute.name.player.mounted_entity_reach", "Mounted Entity Reach");
        translations.add("attribute.name.player.mounted_block_reach", "Mounted Block Reach");

        translations.add("item.modifiers.animal_feet", "When on Animal Feet:");
    }
}
