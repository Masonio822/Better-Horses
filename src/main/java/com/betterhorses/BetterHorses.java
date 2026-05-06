package com.betterhorses;

import com.betterhorses.attributes.ModEntityAttributes;
import com.betterhorses.config.ClientConfig;
import com.betterhorses.config.CommonConfig;
import com.betterhorses.data.HorseFood;
import com.betterhorses.item.ModItems;
import com.betterhorses.networking.payload.BreedingChartPayload;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.sound.ModSounds;
import com.betterhorses.util.ModDataComponents;
import com.betterhorses.util.loottable.LootTableModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BetterHorses implements ModInitializer {
    public static final String MOD_ID = "betterhorses";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Set<HorseFood> horseFoods = Set.of(
            new HorseFood(Identifier.of("minecraft:wheat"), 2.0f, 20, 3, false, 0.0),
            new HorseFood(Identifier.of("minecraft:sugar"), 1.0f, 30, 3, false, 0.0),
            new HorseFood(Identifier.of("minecraft:hay_block"), 20.0f, 180, 0, false, 0.0),
            new HorseFood(Identifier.of("minecraft:apple"), 3.0f, 60, 3, true, 0.0),
            new HorseFood(Identifier.of("minecraft:golden_carrot"), 4.0f, 60, 5, true, 15.0),
            new HorseFood(Identifier.of("minecraft:golden_apple"), 10.0f, 240, 10, true, 50.0),
            new HorseFood(Identifier.of("minecraft:enchanted_golden_apple"), 10.0f, 240, 10, true, 100.0)
    );

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BreedingChartPayload.ID, BreedingChartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MountPayload.ID, MountPayload.CODEC);

        LootTableModifier.modifyLootTables();
        ModItems.registerItems();
        ModDataComponents.registerDataComponentTypes();
        ModSounds.registerSounds();
        ModEntityAttributes.registerModEntityAttributes();

        //Load both configs
        CommonConfig.INSTANCE.load();
        ClientConfig.INSTANCE.load();
    }

    public static Set<HorseFood> getHorseFoods() {
        return horseFoods;
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}