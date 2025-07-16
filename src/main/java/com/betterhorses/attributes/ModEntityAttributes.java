package com.betterhorses.attributes;

import com.betterhorses.BetterHorses;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEntityAttributes {

    public static final RegistryEntry<EntityAttribute> PLAYER_MOUNTED_ENTITY_REACH =
            register("player.mounted_entity_reach", new ClampedEntityAttribute(
                    "attribute.name.player.mounted_entity_reach",
                    2,
                    -64,
                    64
            ).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_MOUNTED_BLOCK_REACH =
            register("player.mounted_block_reach", new ClampedEntityAttribute(
                    "attribute.name.player.mounted_block_reach",
                    3.5,
                    -64,
                    64
            ).setTracked(true));

    public static void registerModEntityAttributes() {
        BetterHorses.LOGGER.info("Registering Mod Entity Attributes for " + BetterHorses.MOD_ID);
    }

    private static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(BetterHorses.MOD_ID, name), attribute);
    }
}
