package com.betterhorses.horse;

import com.betterhorses.item.ModItems;
import com.betterhorses.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface Boxable {
    void copyDataToStack(ItemStack stack);

    void copyDataFromNbt(NbtCompound nbt);

    static <T extends LivingEntity & Boxable> ActionResult tryBox(PlayerEntity player, Hand hand, T entity) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == ModItems.HORSEBOX && entity.isAlive()) {
            entity.playSound(ModSounds.HORSEBOX_UNZIP, 1.0F, 1.0F);
            ItemStack boxItem = new ItemStack(ModItems.HORSEBOX);
            //Put the NBT of the entity into the item
            entity.copyDataToStack(boxItem);

            //Swap the empty horsebox with the new one
            ItemStack exchange = ItemUsage.exchangeStack(itemStack, player, boxItem, false);
            player.setStackInHand(hand, exchange);
            World world = entity.getWorld();

            //Delete the entity afterward
            entity.discard();
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }
}
