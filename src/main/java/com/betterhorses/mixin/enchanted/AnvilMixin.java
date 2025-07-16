package com.betterhorses.mixin.enchanted;

import com.betterhorses.config.CommonConfig;
import com.betterhorses.util.ModTags;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Mixin that allows horse armor to be combined in an anvil. Works alongside the {@link AnimalArmorMixin}
 *
 * @see AnimalArmorMixin
 */
@Mixin(AnvilScreenHandler.class)
public abstract class AnvilMixin extends ForgingScreenHandler {

    public AnvilMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    /*
    For some reason the AnvilScreenHandler#updateResult checks if the item being combined has durability and if it doesn't it will not allow the item to be combined.
    To get around this I used this method which checks and allows *only horse armor* to bypass this rule
     */
    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"))
    private boolean allowNonDamageableCombine(boolean original) {
        if (CommonConfig.INSTANCE.horseArmorEnchantable) {
            return original || this.input.getStack(0).isIn(ModTags.Items.HORSE_ARMOR) && this.input.getStack(1).isIn(ModTags.Items.HORSE_ARMOR);
        }
        return original;
    }
}
