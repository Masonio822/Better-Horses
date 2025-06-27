package com.betterhorses;

import com.betterhorses.networking.payload.BreedingChartPayload;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.screen.BreedingChartScreen;
import com.betterhorses.util.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;


public class BetterHorsesClient implements ClientModInitializer {
    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onInitializeClient() {
        //Breeding Chart Packet Register
        ClientPlayNetworking.registerGlobalReceiver(BreedingChartPayload.ID, (payload, context) -> context.client().execute(() -> {
            Entity entity = MinecraftClient.getInstance().player.getWorld().getEntityById(payload.entityId());
            context.client().setScreen(new BreedingChartScreen(entity.getName(), (AbstractHorseEntity) entity));
        }));
        //Mount Packet Register
        ClientPlayNetworking.registerGlobalReceiver(MountPayload.ID, (payload, context) -> context.client().execute(() -> {
            MinecraftClient.getInstance().options.setPerspective(payload.mounted() ? Perspective.THIRD_PERSON_BACK : Perspective.FIRST_PERSON);
        }));

        ModModelPredicates.registerModelPredicates();
    }
}
