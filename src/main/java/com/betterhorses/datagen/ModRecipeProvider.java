package com.betterhorses.datagen;

import com.betterhorses.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        //Saddle
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, Items.SADDLE)
                .pattern(" # ")
                .pattern("#-#")
                .input('#', Items.LEATHER)
                .input('-', Items.IRON_INGOT)
                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER),
                        FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                        FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
        //Horsebox
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.HORSEBOX)
                .pattern("===")
                .pattern("#0#")
                .pattern("===")
                .input('=', Items.IRON_INGOT)
                .input('#', Items.IRON_BARS)
                .input('0', Items.BARREL)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                        FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.BARREL),
                        FabricRecipeProvider.conditionsFromItem(Items.BARREL))
                .offerTo(exporter);
        //Breeding Chart
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BREEDING_CHART)
                .input(ItemTags.PLANKS)
                .input(Items.IRON_INGOT)
                .input(Items.PAPER)
                .criterion(FabricRecipeProvider.hasItem(Items.PAPER),
                        FabricRecipeProvider.conditionsFromItem(Items.PAPER))
                .offerTo(exporter);
        //Horse Armors
        var armorMap = Map.of(
                Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND,
                Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT,
                Items.IRON_HORSE_ARMOR, Items.IRON_INGOT
        );
    for (Map.Entry<Item, Item> entry : armorMap.entrySet()) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, entry.getKey())
                    .pattern("# #")
                    .pattern("###")
                    .pattern("# #")
                    .input('#', entry.getValue())
                    .criterion(FabricRecipeProvider.hasItem(entry.getValue()),
                            FabricRecipeProvider.conditionsFromItem(entry.getValue()))
                    .offerTo(exporter);
        }
    }
}
