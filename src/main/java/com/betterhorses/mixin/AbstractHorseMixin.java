package com.betterhorses.mixin;

import com.betterhorses.BetterHorses;
import com.betterhorses.duck.Boxable;
import com.betterhorses.duck.TrackedParents;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.util.ModDataComponents;
import com.betterhorses.util.ModTags;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for the general changes to {@link AbstractHorseEntity}.
 * Handles horsebox, perspective change, swimming, and stabilizing logic
 *
 * @see AbstractHorseEntity
 * @see com.betterhorses.item.ModItems
 */

@SuppressWarnings({"AddedMixinMembersNamePattern"})
@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseMixin extends AnimalEntity implements Boxable, TrackedParents {

    @Unique
    private static final Identifier MOUNTED_REACH_MODIFIER_ID = Identifier.of(BetterHorses.MOD_ID, "mounted_reach_modifier");

    protected AbstractHorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    @Unique
    public void copyDataToStack(ItemStack stack) {
        stack.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
        NbtComponent.set(ModDataComponents.BOX_ENTITY_DATA, stack, nbtCompound -> {
            NbtCompound nbt = this.writeNbt(nbtCompound);
            nbt.remove("Pos");
            nbt.remove("Motion");
            nbt.remove("Rotation");
        });
    }

    @Override
    @Unique
    public void copyDataFromNbt(NbtCompound nbt) {
        this.readNbt(nbt);
    }

    /**
     * Sends a payload to the client to tell it to change the perspective to the mounted perspective defined in the config
     *
     * @param player Player to change the perspective of
     */
    //TODO Make perspective configurable
    @Inject(at = @At("HEAD"), method = "putPlayerOnBack")
    private void changePerspectiveOnMount(PlayerEntity player, CallbackInfo info) {
        if (!player.getWorld().isClient()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new MountPayload(true));
        }
    }

    @Inject(at = @At("HEAD"), method = "updatePassengerForDismount")
    private void changePerspectiveOnDismount(LivingEntity passenger, CallbackInfoReturnable<Vec3d> cir) {
        if (passenger instanceof PlayerEntity player) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new MountPayload(false));
        }
    }

    @Inject(at = @At("TAIL"), method = "tickControlled")
    private void allowSwimmingWhileRidden(PlayerEntity controllingPlayer, Vec3d movementInput, CallbackInfo ci) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.travel(this.getVelocity().add(new Vec3d(0, 0.8, 0)));
        }
    }

    /*
    Credit for the stabilizing horse armor idea goes to this reddit post made by u/WorkingNo6161
    https://www.reddit.com/r/minecraftsuggestions/comments/vcnj1h/add_netherite_horse_armor/
     */
    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int preventHorseAngerOnDamage(int original) {
        if (this.getBodyArmor().isIn(ModTags.Items.STABILIZES_HORSE)) {
            return -1;
        }
        return original;
    }

    @Inject(method = "putPlayerOnBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;startRiding(Lnet/minecraft/entity/Entity;)Z"))
    private void changeReachOnMount(PlayerEntity player, CallbackInfo ci) {
        player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE).addTemporaryModifier(new EntityAttributeModifier(
                MOUNTED_REACH_MODIFIER_ID,
                -1.0d,
                EntityAttributeModifier.Operation.ADD_VALUE
        ));
        player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE).addTemporaryModifier(new EntityAttributeModifier(
                MOUNTED_REACH_MODIFIER_ID,
                -1.0d,
                EntityAttributeModifier.Operation.ADD_VALUE
        ));
    }

    @Inject(method = "updatePassengerForDismount", at = @At("RETURN"))
    private void changeReachOnDismount(LivingEntity passenger, CallbackInfoReturnable<Vec3d> cir) {
        if (passenger instanceof PlayerEntity player) {
            player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE).removeModifier(MOUNTED_REACH_MODIFIER_ID);
            player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE).removeModifier(MOUNTED_REACH_MODIFIER_ID);
        }
    }
}
