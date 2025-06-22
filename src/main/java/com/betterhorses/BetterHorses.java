package com.betterhorses;

import com.betterhorses.item.ModItems;
import com.betterhorses.networking.payload.BreedingChartPayload;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.sound.ModSounds;
import com.betterhorses.tag.ModTags;
import com.betterhorses.util.ModDataComponents;
import com.betterhorses.util.loottable.LootTableModifer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterHorses implements ModInitializer {
    public static final String MOD_ID = "better_horses";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BreedingChartPayload.ID, BreedingChartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MountPayload.ID, MountPayload.CODEC);

        LootTableModifer.modifyLootTables();
        ModItems.registerItems();
        ModDataComponents.registerDataComponentTypes();
        ModSounds.registerSounds();
        ModTags.registerModTags();
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}