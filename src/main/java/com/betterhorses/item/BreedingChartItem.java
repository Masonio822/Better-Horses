package com.betterhorses.item;

import com.betterhorses.networking.payload.BreedingChartPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

/**
 * Simple item to show various stats of a {@link AbstractHorseEntity}
 */
public class BreedingChartItem extends Item {
    public BreedingChartItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient()) {
            return super.useOnEntity(stack, user, entity, hand);
        }

        if (entity instanceof AbstractHorseEntity) {
            //Send the packet to change the client's screen
            ServerPlayNetworking.send((ServerPlayerEntity) user, new BreedingChartPayload(entity.getId()));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
