package com.betterhorses.config;

import com.betterhorses.BetterHorses;
import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

public class CommonConfig extends WrappedConfig {
    public static final CommonConfig INSTANCE = CommonConfig.createToml(
            FabricLoader.getInstance().getConfigDir(),
            BetterHorses.MOD_ID,
            "common",
            CommonConfig.class
    );

    public void load() {
    }

    @Comment("How long the horsebox will be placed on cooldown (ticks) after use")
    @Comment("Set to 0 to disable")
    @SerializedName("horsebox_cooldown")
    public int horseboxCooldown = 40;

    @Comment("Should horses be prevented from neighing after taking damage")
    @Comment("while wearing armor with the 'stabilizes_horse' tag")
    @SerializedName("immune_to_neighing")
    public boolean immuneToNeighing = true;

    @Comment("Should regular apples be able to make horses breed")
    @Comment("Horses will still be able to be tempted with apples")
    @SerializedName("allow_apple_breeding")
    public boolean allowAppleBreeding = true;

    @Comment("Should horse armor be enchantable")
    @SerializedName("horse_armor_enchantable")
    public boolean horseArmorEnchantable = true;

    @Comment("How much reach should be removed from the player while riding a horse")
    @Comment("Set to -1 to disable")
    @FloatRange(min = -1, max = 61)
    @SerializedName("horse_reach_penalty")
    public float horseReachPenalty = 1.0f;

    @Comment("Should horse armor be removed from loot tables")
    @SerializedName("remove_horse_armor")
    public boolean removeHorseArmor = true;

    @Comment("Should saddles be removed from loot tables and replaced with leather")
    @SerializedName("remove_saddles")
    public boolean removeSaddles = true;

    @Comment("Should horses be able to swim while ridden")
    @SerializedName("horses_can_swim")
    public boolean horsesCanSwim = true;

    @Comment("Should horses' base movement speed be increased slightly")
    @SerializedName("buffHorses")
    public boolean buffHorses = true;
}
