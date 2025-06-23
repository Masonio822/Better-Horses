package com.betterhorses.networking.payload;

import com.betterhorses.networking.NetworkConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * Custom payload to change the perspective of the client when mounted/dismounted
 *
 * @param mounted whether the action is mount or dismount
 */
public record MountPayload(boolean mounted) implements CustomPayload {
    public static final CustomPayload.Id<MountPayload> ID = new CustomPayload.Id<>(NetworkConstants.MOUNT_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, MountPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, MountPayload::mounted, MountPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
