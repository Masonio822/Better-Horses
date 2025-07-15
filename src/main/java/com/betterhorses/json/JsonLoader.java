package com.betterhorses.json;

import com.betterhorses.BetterHorses;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class JsonLoader {
    public static void load() {
        BetterHorses.LOGGER.info("Loading json data for " + BetterHorses.MOD_ID);

        FabricLoader.getInstance().getModContainer(BetterHorses.MOD_ID).ifPresentOrElse(modContainer -> modContainer.findPath("data/betterhorses/food/horse.json").ifPresentOrElse(
                path -> {
                    try (
                            JsonReader jsonReader = new JsonReader(Files.newBufferedReader(path))
                    ) {
                        Set<HorseFood> horseFoods = new HashSet<>();
                        jsonReader.beginObject();
                        if (!jsonReader.nextName().equals("enabled")) {
                            throw new MalformedJsonException("First field in json file should be a boolean named: 'enabled'");
                        }
                        if (jsonReader.nextBoolean()) {
                            jsonReader.skipValue();
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                float heal = 0.0f;
                                int age = 0;
                                int temper = 0;
                                boolean breed = false;
                                double mutationChance = 0.0;
                                Identifier itemId = Identifier.of(jsonReader.nextName());
                                jsonReader.beginObject();

                                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                                    switch (jsonReader.nextName()) {
                                        case "heal" -> heal = (float) jsonReader.nextDouble();
                                        case "age" -> age = jsonReader.nextInt();
                                        case "temper" -> temper = jsonReader.nextInt();
                                        case "breed" -> breed = jsonReader.nextBoolean();
                                        case "mutationChance" -> mutationChance = jsonReader.nextDouble();
                                        default ->
                                                throw new MalformedJsonException("Could not read next name: '" + jsonReader.nextName() + "'!");
                                    }
                                }

                                horseFoods.add(new HorseFood(itemId, heal, age, temper, breed, mutationChance));
                                jsonReader.skipValue();
                            }
                            BetterHorses.setHorseFoods(horseFoods);
                        } else {
                            establishDefaultFood();
                        }
                    } catch (IllegalStateException | MalformedJsonException e) {
                        BetterHorses.LOGGER.error("Malformed json data in {} \nUsing mod defaults... \nCaused by: {}", path, e.toString());
                        establishDefaultFood();
                    } catch (IOException e) {
                        establishDefaultFood("Could not load json data for mod" + BetterHorses.MOD_ID + "\nUsing mod defaults...");
                    }
                },
                () -> establishDefaultFood("Could not find json data for mod " + BetterHorses.MOD_ID + "\nUsing mod defaults...")
        ), () -> establishDefaultFood("Could not find mod container for mod " + BetterHorses.MOD_ID + "\nUsing mod defaults..."));
    }

    private static void establishDefaultFood(String errorMessage) {
        BetterHorses.LOGGER.warn(errorMessage);
        establishDefaultFood();
    }

    private static void establishDefaultFood() {
        BetterHorses.setHorseFoods(Set.of(
                new HorseFood(Identifier.of("minecraft:wheat"), 2.0f, 20, 3, false, 0.0),
                new HorseFood(Identifier.of("minecraft:sugar"), 1.0f, 30, 3, false, 0.0),
                new HorseFood(Identifier.of("minecraft:hay_block"), 20.0f, 180, 0, false, 0.0),
                new HorseFood(Identifier.of("minecraft:apple"), 3.0f, 60, 3, true, 0.0),
                new HorseFood(Identifier.of("minecraft:golden_carrot"), 4.0f, 60, 5, true, 15.0),
                new HorseFood(Identifier.of("minecraft:golden_apple"), 10.0f, 240, 10, true, 50.0),
                new HorseFood(Identifier.of("minecraft:enchanted_golden_apple"), 10.0f, 240, 10, true, 100.0)
        ));
    }
}
