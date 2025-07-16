package com.betterhorses.mixin.breeding;

import com.betterhorses.BetterHorses;
import com.betterhorses.config.CommonConfig;
import com.betterhorses.duck.Mutable;
import com.betterhorses.json.HorseFood;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(AbstractHorseEntity.class)
public abstract class BreedingMixin extends AnimalEntity implements Mutable {

    private double mutationChance;

    protected BreedingMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readMutationChance(NbtCompound nbt, CallbackInfo ci) {
        mutationChance = nbt.getDouble("MutationChance");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeMutationChance(NbtCompound nbt, CallbackInfo ci) {
        if (mutationChance > 0) {
            nbt.putDouble("MutationChance", mutationChance);
        }
    }

    @Override
    public double getMutationChance() {
        return mutationChance;
    }

    @Override
    public void setMutationChance(double value) {
        mutationChance = value;
    }

    @ModifyExpressionValue(method = "receiveFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean cancelFoodValueAssignment(boolean original) {
        return false;
    }

    @ModifyVariable(method = "receiveFood", at = @At(value = "STORE", ordinal = 0), name = "bl")
    private boolean handleLogic(boolean value, PlayerEntity player, ItemStack item) {
        AbstractHorseEntity _this = (AbstractHorseEntity) (Object) this;
        HorseFood horseFood = searchIdentifier(Identifier.of(item.getItem().toString()));
        if (!_this.getWorld().isClient && _this.isTame() && _this.getBreedingAge() == 0 && !_this.isInLove()
                && horseFood.breed()) {
            if (horseFood.id().equals(Identifier.of("minecraft:apple")) && !CommonConfig.INSTANCE.allowAppleBreeding) {
                return value;
            }
            _this.lovePlayer(player);
            setMutationChance(horseFood.mutationChance());
            return true;
        }
        return value;
    }

    @ModifyVariable(method = "receiveFood", at = @At(value = "STORE", ordinal = 0), name = "f")
    private float setHeal(float value, PlayerEntity player, ItemStack item) {
        return searchIdentifier(Identifier.of(item.getItem().toString())).heal();
    }

    @ModifyVariable(method = "receiveFood", at = @At(value = "STORE", ordinal = 0), name = "i")
    private int setAge(int value, PlayerEntity player, ItemStack item) {
        return searchIdentifier(Identifier.of(item.getItem().toString())).age();
    }

    @ModifyVariable(method = "receiveFood", at = @At(value = "STORE", ordinal = 0), name = "j")
    private int setTemper(int value, PlayerEntity player, ItemStack item) {
        return searchIdentifier(Identifier.of(item.getItem().toString())).temper();
    }

    @Unique
    private HorseFood searchIdentifier(Identifier id) {
        return BetterHorses.getHorseFoods()
                .stream()
                .filter(horseFood -> horseFood.id().equals(id))
                .findFirst()
                .orElse(new HorseFood(null, 0.0f, 0, 0, false, 0.0));
    }
}
