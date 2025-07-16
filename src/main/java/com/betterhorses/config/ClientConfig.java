package com.betterhorses.config;

import com.betterhorses.BetterHorses;
import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.SerializedName;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.Perspective;

public class ClientConfig extends ReflectiveConfig {
    public static final ClientConfig INSTANCE = ClientConfig.createToml(
            FabricLoader.getInstance().getConfigDir(),
            BetterHorses.MOD_ID,
            "client",
            ClientConfig.class
    );

    public void load() {
    }

    @Comment("What camera perspective should be switched to when mounting a horse")
    @SerializedName("mount_perspective")
    public final TrackedValue<Perspective> mountPerspective = this.value(Perspective.THIRD_PERSON_BACK);
}
