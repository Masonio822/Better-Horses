package com.betterhorses.mixin;

import com.betterhorses.tag.ModTags;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilMixin extends ForgingScreenHandler {
    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private String newItemName;

    public AnvilMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    private void combineHorseArmor(CallbackInfo info) {
        ItemStack i1 = this.input.getStack(0);
        ItemStack i2 = this.input.getStack(1);
        ItemStack o = i1.copy();
        this.levelCost.set(1);
        int i = 0;
        long l = 0L;
        int j = 0;

        System.out.println(i1.isIn(ItemTags.BEDS));
        System.out.println("I1: " + i1.isIn(ModTags.HORSE_ARMOR));
        System.out.println("I2: " + i2.isIn(ModTags.HORSE_ARMOR) + " Empty: " + i2.isEmpty());
        if (i1.isIn(ModTags.HORSE_ARMOR) && i2.isIn(ModTags.HORSE_ARMOR) && !i2.isEmpty()) {
            System.out.println("Pass 1st con");
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(o));

            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(i2);
            boolean bl2 = false;
            boolean bl3 = false;

            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> registryEntry = entry.getKey();
                int q = builder.getLevel(registryEntry);
                int r = entry.getIntValue();
                r = q == r ? r + 1 : Math.max(r, q);
                Enchantment enchantment = registryEntry.value();
                boolean bl4 = enchantment.isAcceptableItem(i1);
                if (this.player.getAbilities().creativeMode) {
                    bl4 = true;
                }

                for (RegistryEntry<Enchantment> registryEntry2 : builder.getEnchantments()) {
                    if (!registryEntry2.equals(registryEntry) && !Enchantment.canBeCombined(registryEntry, registryEntry2)) {
                        bl4 = false;
                        i++;
                    }
                }

                if (!bl4) {
                    bl3 = true;
                } else {
                    bl2 = true;
                    if (r > enchantment.getMaxLevel()) {
                        r = enchantment.getMaxLevel();
                    }

                    builder.set(registryEntry, r);
                    int s = enchantment.getAnvilCost();

                    i += s * r;
                    if (i1.getCount() > 1) {
                        i = 40;
                    }
                }
            }

            if (bl3 && !bl2) {
                this.output.setStack(0, ItemStack.EMPTY);
                this.levelCost.set(0);
                return;
            }

            if (this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(i1.getName().getString())) {
                    j = 1;
                    i += j;
                    o.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
                }
            } else if (i1.contains(DataComponentTypes.CUSTOM_NAME)) {
                j = 1;
                i += j;
                o.remove(DataComponentTypes.CUSTOM_NAME);
            }

            int t = (int) MathHelper.clamp(l + (long) i, 0L, 2147483647L);
            this.levelCost.set(t);
            if (i <= 0) {
                o = ItemStack.EMPTY;
            }

            if (j == i && j > 0 && this.levelCost.get() >= 40) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
                o = ItemStack.EMPTY;
            }


            this.output.setStack(0, o);
            this.sendContentUpdates();
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        }
    }
}
