package com.betterhorses.config;

import com.betterhorses.BetterHorses;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ModConfig {
    private final File runDirectory = MinecraftClient.getInstance().runDirectory;

    private boolean failed;
    private File commonConfig;
    private File clientConfig;

    private static final ModConfig instance = new ModConfig();

    private ModConfig() {
        try {
            createFiles();
            createCommonContent(new JsonWriter(Files.newBufferedWriter(commonConfig.toPath())));
            createClientContent(new JsonWriter(Files.newBufferedWriter(clientConfig.toPath())));
        } catch (IOException e) {
            BetterHorses.LOGGER.warn(
                    "Could not load config for mod" + BetterHorses.MOD_ID +
                            " By default, everything will be enabled!"
            );
            e.printStackTrace();
            failed = true;
        }
        failed = false;
    }

    private void createClientContent(JsonWriter jsonWriter) {

    }

    private void createCommonContent(JsonWriter jsonWriter) {
        
    }

    /**
     * Loads the configuration via a lazy init singleton design pattern
     *
     * @implNote Call this inside the {@code ModInitializer}
     */
    public final void load() {
        BetterHorses.LOGGER.info("Loading config files for mod" + BetterHorses.MOD_ID);
    }

    private void createFiles() throws IOException {
        commonConfig = new File(runDirectory.getPath() + "/config/" + BetterHorses.MOD_ID, "common.json");
        clientConfig = new File(runDirectory.getPath() + "/config/" + BetterHorses.MOD_ID, "client.json");

        if (!commonConfig.exists()) {
            BetterHorses.LOGGER.info("Generating common config file for mod" + BetterHorses.MOD_ID);
            commonConfig.getParentFile().mkdirs();
            commonConfig.createNewFile();
        }
        if (!clientConfig.exists()) {
            BetterHorses.LOGGER.info("Generating client config file for mod" + BetterHorses.MOD_ID);
            clientConfig.getParentFile().mkdirs();
            clientConfig.createNewFile();
        }
    }

    public boolean isLoaded() {
        return failed;
    }

    public static ModConfig getInstance() {
        return instance;
    }
}
