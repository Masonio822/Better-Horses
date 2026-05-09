package com.betterhorses.mixin.horseshoe;

import com.betterhorses.duck.HorseshoeEquipable;
import com.betterhorses.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
abstract class AbstractHorseHorseshoeMixin extends PassiveEntity implements HorseshoeEquipable {
    @Shadow
    protected float jumpStrength;

    @Unique
    private ItemStack horseshoeItem = ItemStack.EMPTY;
    @Unique
    private final Inventory horseshoeInventory = new SingleStackInventory() {
        @Override
        public ItemStack getStack() {
            return AbstractHorseHorseshoeMixin.this.better_Horses_1_21_1$getHorseshoe();
        }

        @Override
        public void setStack(ItemStack stack) {
            AbstractHorseHorseshoeMixin.this.better_Horses_1_21_1$setHorseshoe(stack);
        }

        @Override
        public void markDirty() {
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            AbstractHorseEntity _this = (AbstractHorseEntity) (Object) AbstractHorseHorseshoeMixin.this;
            return player.getVehicle() == _this || player.canInteractWithEntity(_this, 4.0);
        }
    };

    protected AbstractHorseHorseshoeMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean better_Horses_1_21_1$hasHorseshoe() {
        return this.better_Horses_1_21_1$getHorseshoe() != ItemStack.EMPTY;
    }

    @Override
    public void better_Horses_1_21_1$setHorseshoe(ItemStack stack) {
        if (stack.isOf(ModItems.HORSESHOE)) {
            horseshoeItem = stack;
        }
    }

    @Override
    public ItemStack better_Horses_1_21_1$getHorseshoe() {
        return horseshoeItem;
    }

    @Override
    public Inventory better_Horses_1_21_1$getHorseshoeInventory() {
        return this.horseshoeInventory;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void saveHorseshoe(NbtCompound nbt, CallbackInfo ci) {
        if (!this.better_Horses_1_21_1$getHorseshoeInventory().isEmpty()) {
            nbt.put("HorseshoeItem", this.better_Horses_1_21_1$getHorseshoe().encode(this.getRegistryManager()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void loadHorseshoe(NbtCompound nbt, CallbackInfo ci) {
        this.better_Horses_1_21_1$setHorseshoe(ItemStack.fromNbtOrEmpty(this.getRegistryManager(), nbt.getCompound("HorseshoeItem")));
    }
}
