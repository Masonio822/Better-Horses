package com.betterhorses.mixin.horseshoe;

import com.betterhorses.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseInventoryMixin extends PassiveEntity {
    @Unique
    private final AbstractHorseEntity _this = (AbstractHorseEntity) (Object) this;

    protected AbstractHorseInventoryMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    @Final
    @Mutable
    private Inventory inventory = new SimpleInventory(2) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return switch (slot) {
                case 0 -> _this.isHorseArmor(stack);
                case 1 -> stack.getItem().equals(ModItems.HORSESHOE);
                default -> true;
            };
        }
    };

//    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
//    private void saveHorseshoeSlot(NbtCompound nbt, CallbackInfo ci) {
//        if (!this.inventory.getStack(1).isEmpty()) {
//            nbt.put("HorseshoeItem", horseshoe.encode(this.getRegistryManager()));
//        }
//    }
//
//    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
//    private void loadHorseshoeSlot(NbtCompound nbt, CallbackInfo ci) {
//        if (nbt.contains("HorseshoeItem", NbtElement.COMPOUND_TYPE)) {
//            ItemStack itemStack = ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound("HorseshoeItem")).orElse(ItemStack.EMPTY);
//            if (itemStack.isOf(ModItems.HORSESHOE)) {
//                this.inventory.setStack(1, itemStack);
//            }
//        }
//    }
}
