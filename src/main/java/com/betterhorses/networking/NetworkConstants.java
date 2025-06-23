package com.betterhorses.networking;

import com.betterhorses.BetterHorses;
import net.minecraft.util.Identifier;

/**
 * Various constants for packets and other networking
 *
 * @see com.betterhorses.networking.payload.BreedingChartPayload
 * @see com.betterhorses.networking.payload.MountPayload
 */
public class NetworkConstants {
    public static final Identifier BREEDING_CHART_PACKET_ID = Identifier.of(BetterHorses.MOD_ID, "breeding_chart_packet");
    public static final Identifier MOUNT_PACKET_ID = Identifier.of(BetterHorses.MOD_ID, "mount_packet");
}
