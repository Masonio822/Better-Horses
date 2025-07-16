package com.betterhorses.item;

import com.betterhorses.config.CommonConfig;
import com.betterhorses.duck.Boxable;
import com.betterhorses.util.HorseHelper;
import com.betterhorses.util.ModDataComponents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

/**
 * Custom item class that creates a way to transport {@link Boxable} entities
 *
 * @see ModItems
 * @see net.minecraft.item.Item
 */

public class HorseboxItem extends Item {
    public HorseboxItem(Settings settings) {
        super(settings);
    }

    /**
     * Core method to put the entity in the Horsebox
     *
     * @param stack  the itemstack the player is using
     * @param user   the player using the item
     * @param entity the entity being boxed
     */
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (stack.getComponents().contains(ModDataComponents.BOX_ENTITY_DATA)) {
            return ActionResult.PASS;
        }
        if (entity instanceof HorseEntity horse && !user.getItemCooldownManager().isCoolingDown(this)) {
            user.getItemCooldownManager().set(this, CommonConfig.INSTANCE.horseboxCooldown);
            return Boxable.tryBox(user, hand, (LivingEntity & Boxable) horse);
        }
        return ActionResult.PASS;
    }

    /**
     * Core method to release the entity from the Horsebox
     *
     * @param world the world to spawn the entity
     * @param user  the user spawning the entity
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.getStackInHand(hand).getComponents().get(ModDataComponents.BOX_ENTITY_DATA) != null && world instanceof ServerWorld serverWorld) {
            ItemStack stack = user.getStackInHand(hand);
            if (EntityType.HORSE.spawnFromItemStack(
                    serverWorld,
                    user.getStackInHand(hand),
                    user,
                    user.getBlockPos(),
                    SpawnReason.BUCKET,
                    true,
                    false) instanceof Boxable boxable) {
                NbtComponent nbtComponent = stack.getOrDefault(ModDataComponents.BOX_ENTITY_DATA, NbtComponent.DEFAULT);
                AbstractHorseEntity horse = (AbstractHorseEntity) boxable;
                BlockPos summonPos = locateSummonPos(user.getBlockPos(), horse);

                NbtList posList = new NbtList();
                posList.add(NbtDouble.of(summonPos.getX()));
                posList.add(NbtDouble.of(summonPos.getY()));
                posList.add(NbtDouble.of(summonPos.getZ()));

                nbtComponent = nbtComponent.apply((compound) -> compound.put("Pos", posList));
                boxable.copyDataFromNbt(nbtComponent.copyNbt());

                if (user.isCreative()) {
                    user.setStackInHand(hand, ItemStack.EMPTY);
                }

                CalmTimer.INSTANCE.horse = horse;
                horse.setAngry(true);
                horse.playSound(SoundEvents.ENTITY_HORSE_ANGRY);
                CalmTimer.INSTANCE.setTimer(20);
            }
            ItemStack exchange = ItemUsage.exchangeStack(user.getStackInHand(hand), user, new ItemStack(ModItems.HORSEBOX), true);
            user.setStackInHand(hand, exchange);
            user.getItemCooldownManager().set(this, CommonConfig.INSTANCE.horseboxCooldown);

            world.emitGameEvent(user, GameEvent.ENTITY_PLACE, user.getBlockPos());
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    /**
     * Locates a safe space to load the entity. The entity should be placed in a 1 block radius of the player
     *
     * @param playerLoc the location of the player
     * @param horse     the entity to be loaded
     * @return A safe position for the entity to be spawned
     */
    //TODO make the method LOL
    private BlockPos locateSummonPos(BlockPos playerLoc, AbstractHorseEntity horse) {
        return playerLoc;
    }

    /**
     * Small class to stop the horse from bucking on spawn
     */
    public static class CalmTimer implements ServerTickEvents.EndTick {
        public static final CalmTimer INSTANCE = new CalmTimer();

        private long ticksRemaining;
        private AbstractHorseEntity horse;

        public void setTimer(long ticksRemaining) {
            this.ticksRemaining = ticksRemaining;
        }

        @Override
        public void onEndTick(MinecraftServer server) {
            if (--this.ticksRemaining == 0L) {
                horse.setAngry(false);
            }
        }

        static {
            ServerTickEvents.END_SERVER_TICK.register(INSTANCE);
        }
    }

    /**
     * Adds info about the current entity to the tooltip
     *
     * @param stack   the itemstack
     * @param tooltip the list of tooltips to show
     */

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.getOrDefault(ModDataComponents.BOX_ENTITY_DATA, NbtComponent.DEFAULT);
        if (nbtComponent.isEmpty()) {
            return;
        }

        int variant = nbtComponent.copyNbt().getInt("Variant");

        String colorStr = "color.minecraft.horse." + HorseHelper.getColor(variant);
        String markStr = "marking.minecraft.horse." + HorseHelper.getMarkings(variant);

        tooltip.add(
                Text.translatable("tooltip.betterhorses.color")
                        .append(Text.translatable(colorStr))
                        .formatted(Formatting.GRAY)
        );
        tooltip.add(
                Text.translatable("tooltip.betterhorses.marking")
                        .append(Text.translatable(markStr))
                        .formatted(Formatting.GRAY)
        );
    }
}



