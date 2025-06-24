package com.betterhorses.mixin;

import com.betterhorses.horse.Boxable;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.util.ModDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for the {@link AbstractHorseEntity}.
 * Adds most of the custom behavior for the horses
 *
 * @see HorseEntity
 * @see com.betterhorses.horse.Boxable
 */

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseMixin extends AnimalEntity implements Boxable {

    protected AbstractHorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
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
}
