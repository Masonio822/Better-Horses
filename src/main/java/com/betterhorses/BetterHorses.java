package com.betterhorses;

import com.betterhorses.attributes.ModEntityAttributes;
import com.betterhorses.config.ModConfig;
import com.betterhorses.item.ModItems;
import com.betterhorses.json.HorseFood;
import com.betterhorses.json.JsonLoader;
import com.betterhorses.networking.payload.BreedingChartPayload;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.sound.ModSounds;
import com.betterhorses.util.ModDataComponents;
import com.betterhorses.util.loottable.LootTableModifer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BetterHorses implements ModInitializer {
    public static final String MOD_ID = "betterhorses";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Set<HorseFood> horseFoods;

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BreedingChartPayload.ID, BreedingChartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MountPayload.ID, MountPayload.CODEC);

        LootTableModifer.modifyLootTables();
        ModItems.registerItems();
        ModDataComponents.registerDataComponentTypes();
        ModSounds.registerSounds();
        JsonLoader.load();
        ModEntityAttributes.registerModEntityAttributes();
        ModConfig.getInstance().load();
    }

    public static void setHorseFoods(Set<HorseFood> horseFoods) {
        BetterHorses.horseFoods = horseFoods;
    }

    public static Set<HorseFood> getHorseFoods() {
        return horseFoods;
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}