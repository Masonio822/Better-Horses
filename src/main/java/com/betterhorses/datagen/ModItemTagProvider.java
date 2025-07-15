package com.betterhorses.datagen;

import com.betterhorses.BetterHorses;
import com.betterhorses.item.ModItems;
import com.betterhorses.json.HorseFood;
import com.betterhorses.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //Add to existing tags
        for (HorseFood horseFood : BetterHorses.getHorseFoods()) {
            getTagBuilder(ItemTags.HORSE_FOOD).add(horseFood.id());
            if (horseFood.breed()) {
                getTagBuilder(ItemTags.HORSE_TEMPT_ITEMS).add(horseFood.id());
            }
        }

        getTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE).addTag(ModTags.Items.HORSE_ARMOR.id());

        //Create new tags
        getOrCreateTagBuilder(ModTags.Items.HORSE_ARMOR)
                .add(Items.LEATHER_HORSE_ARMOR)
                .add(Items.GOLDEN_HORSE_ARMOR)
                .add(Items.IRON_HORSE_ARMOR)
                .add(Items.DIAMOND_HORSE_ARMOR)
                .add(ModItems.NETHERITE_HORSE_ARMOR);

        getOrCreateTagBuilder(ModTags.Items.STABILIZES_HORSE).add(ModItems.NETHERITE_HORSE_ARMOR);
    }
}
