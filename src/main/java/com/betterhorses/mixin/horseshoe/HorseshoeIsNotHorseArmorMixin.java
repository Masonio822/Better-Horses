package com.betterhorses.mixin.horseshoe;

import com.betterhorses.util.ModTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HorseEntity.class)
class HorseshoeIsNotHorseArmorMixin {
    @ModifyReturnValue(method = "isHorseArmor", at = @At("RETURN"))
    private boolean horseshoeIsNotHorseArmor(boolean original, ItemStack stack) {
        return original && !stack.isIn(ModTags.Items.HORSESHOE);
    }
}
