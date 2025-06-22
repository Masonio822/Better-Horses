package com.betterhorses.util;

import com.betterhorses.BetterHorses;
import com.betterhorses.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.HORSEBOX, Identifier.of(BetterHorses.MOD_ID, "closed"),
                (stack, world, entity, seed) -> stack.getComponents().get(ModDataComponents.BOX_ENTITY_DATA) != null ? 1f : 0f);
    }
}
