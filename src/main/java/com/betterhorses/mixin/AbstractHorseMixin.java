package com.betterhorses.mixin;

import com.betterhorses.duck.TrackedParents;
import com.betterhorses.horse.Boxable;
import com.betterhorses.networking.payload.MountPayload;
import com.betterhorses.util.ModDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseMixin extends AnimalEntity implements Boxable, TrackedParents {

    private static final TrackedData<String> PARENTS = DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.STRING);

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

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    private void readParents(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(PARENTS, nbt.contains("Parents") ? nbt.get("Parents").asString() : "");
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    private void writeParents(NbtCompound nbt, CallbackInfo ci) {
        if (this.dataTracker.get(PARENTS) != null || this.dataTracker.get(PARENTS).isEmpty()) {
            nbt.putString("Parents", this.dataTracker.get(PARENTS));
        }
    }

    @Override
    @Nullable
    public NbtElement getParentsNbt() {
        return NbtString.of(this.dataTracker.get(PARENTS));
    }

    @Override
    public void setParentsNbt(NbtCompound parentsNbt) {
        this.dataTracker.set(PARENTS, parentsNbt.asString());
    }

    @Override
    public void setParents(AbstractHorseEntity horse1, AbstractHorseEntity horse2) {
        NbtCompound nbt = this.writeNbt(new NbtCompound());

        NbtCompound addThis = new NbtCompound();
        addThis.putInt("Variant", horse1.writeNbt(new NbtCompound()).getInt("Variant"));

        NbtCompound thisAttributes = new NbtCompound();
        thisAttributes.putDouble("Health", horse1.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
        thisAttributes.putDouble("Speed", horse1.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
        thisAttributes.putDouble("Jump", horse1.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
        addThis.put("Attributes", thisAttributes);

        NbtCompound addOther = new NbtCompound();
        addOther.putInt("Variant", horse2.writeNbt(new NbtCompound()).getInt("Variant"));

        NbtCompound otherAttributes = new NbtCompound();
        otherAttributes.putDouble("Health", horse2.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
        otherAttributes.putDouble("Speed", horse2.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
        otherAttributes.putDouble("Jump", horse2.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
        addOther.put("Attributes", otherAttributes);

        NbtList parents = new NbtList();
        parents.add(addThis);
        parents.add(addOther);
        nbt.put("Parents", parents);

        NbtComponent.of(nbt).applyToEntity(this);
        this.setParentsNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    private void addParentTracker(DataTracker.Builder builder, CallbackInfo callbackInfo) {
        builder.add(PARENTS, "");
    }
}
