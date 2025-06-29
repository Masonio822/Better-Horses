package com.betterhorses.networking.payload;

import com.betterhorses.networking.NetworkConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * Custom payload to open the {@link com.betterhorses.screen.BreedingChartScreen} for the client
 *
 * @param entityId the id of the entity to be viewed
 * @see com.betterhorses.horse.Chartable
 */
public record BreedingChartPayload(int entityId) implements CustomPayload {
    public static final CustomPayload.Id<BreedingChartPayload> ID = new CustomPayload.Id<>(NetworkConstants.BREEDING_CHART_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, BreedingChartPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, BreedingChartPayload::entityId, BreedingChartPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
