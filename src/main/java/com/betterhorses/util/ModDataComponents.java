package com.betterhorses.util;

import com.betterhorses.BetterHorses;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

/**
 * Basic class to register custom data components
 *
 * @see net.minecraft.component.DataComponentTypes
 */
public class ModDataComponents {

    /**
     * Used to save the NBT data of entities in the {@link com.betterhorses.item.HorseboxItem}
     *
     * @see NbtComponent
     * @see com.betterhorses.item.HorseboxItem
     */
    public static final ComponentType<NbtComponent> BOX_ENTITY_DATA = register("box_entity_data", builder -> builder.codec(NbtComponent.CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(BetterHorses.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        BetterHorses.LOGGER.info("Registering Data Component Types for " + BetterHorses.MOD_ID);
    }
}
