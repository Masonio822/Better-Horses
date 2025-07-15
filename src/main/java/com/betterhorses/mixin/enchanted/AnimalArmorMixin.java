package com.betterhorses.mixin.enchanted;

import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that allows horse armor to be enchanted.
 * Works alongside the {@link AnvilMixin}
 *
 * @see AnvilMixin
 */
@Mixin(AnimalArmorItem.class)
public abstract class AnimalArmorMixin extends ArmorItem {

    @Shadow
    @Final
    private AnimalArmorItem.Type type;

    public AnimalArmorMixin(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Inject(at = @At("HEAD"), method = "isEnchantable", cancellable = true)
    private void makeEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.type == AnimalArmorItem.Type.EQUESTRIAN);
    }
}
