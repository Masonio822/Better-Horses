package com.betterhorses.mixin.horseshoe;

import com.betterhorses.duck.HorseshoeEquipable;
import com.betterhorses.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {
    @Shadow
    @Final
    private AbstractHorseEntity entity;

    protected HorseScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addHorseShoeSlot(int syncId, PlayerInventory playerInventory, Inventory inventory, AbstractHorseEntity entity, int slotColumnCount, CallbackInfo ci) {
        this.addSlot(new Slot(((HorseshoeEquipable) entity).better_Horses_1_21_1$getHorseshoeInventory(), 38, 8, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.HORSESHOE) && !this.hasStack();
            }

            @Override
            public boolean isEnabled() {
                return entity.canUseSlot(EquipmentSlot.BETTER_HORSES_ANIMAL_FEET);
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                this.inventory.setStack(0, stack);
                this.markDirty();
            }
        });
    }

    @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
    private void quickMoveHorseshoe(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        Slot sourceSlot = this.slots.get(slot);
        if (!sourceSlot.hasStack()) return;

        ItemStack stack = sourceSlot.getStack();
        Slot horseshoeSlot = this.getSlot(38);

        if (horseshoeSlot.canInsert(stack)) {
            ItemStack copy = stack.copy();
            if (this.insertItem(stack, 38, 39, false)) {
                cir.setReturnValue(copy);
            } else {
                cir.setReturnValue(ItemStack.EMPTY);
            }

            if (stack.isEmpty()) {
                sourceSlot.setStack(ItemStack.EMPTY);
            } else {
                sourceSlot.markDirty();
            }
        }
    }
}
