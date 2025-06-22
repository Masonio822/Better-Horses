package com.betterhorses.sound;

import com.betterhorses.BetterHorses;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent HORSEBOX_UNZIP = register("horsebox_unzip");

    public static void registerSounds() {
        BetterHorses.LOGGER.info("Registering sounds for mod " + BetterHorses.MOD_ID);
    }

    public static SoundEvent register(String name) {
        Identifier id = Identifier.of(BetterHorses.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
