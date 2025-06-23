package com.betterhorses.mixin;

import com.betterhorses.util.ModTags;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
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

import static net.minecraft.screen.AnvilScreenHandler.getNextCost;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilMixin extends ForgingScreenHandler {
    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private String newItemName;

    @Shadow
    private int repairItemUsage;

    public AnvilMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean allowHorseArmorCombine(boolean original) {
        boolean custom = this.input.getStack(0).isIn(ModTags.Items.HORSE_ARMOR) && this.input.getStack(1).isIn(ModTags.Items.HORSE_ARMOR);
        return true;
    }

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"))
    private boolean allowNonDamageableCombine(boolean original) {
        return true;
    }

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    private void combineHorseArmor(CallbackInfo ci, @Cancellable CallbackInfo info) {
        ItemStack itemStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        long l = 0L;
        int j = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.canHaveEnchantments(itemStack)) {
            System.out.println("Passed con 1");
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = this.input.getStack(1);
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(itemStack2));
            l += (long) itemStack.getOrDefault(DataComponentTypes.REPAIR_COST, Integer.valueOf(0)).intValue()
                    + (long) itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, Integer.valueOf(0)).intValue();
            this.repairItemUsage = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.contains(DataComponentTypes.STORED_ENCHANTMENTS);
                if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                    int k = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    if (k <= 0) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }

                    int m;
                    for (m = 0; k > 0 && m < itemStack3.getCount(); m++) {
                        int n = itemStack2.getDamage() - k;
                        itemStack2.setDamage(n);
                        i++;
                        k = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    }

                    this.repairItemUsage = m;
                } else {
                    System.out.println("Failed con 2");
                    if (!bl && (!itemStack2.isOf(itemStack3.getItem()) || !itemStack2.isDamageable())) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        System.out.println("Passed con 3");
                        System.out.println("Should not be possible :(");
                        return;
                    }

                    if (itemStack2.isDamageable() && !bl) {
                        int kx = itemStack.getMaxDamage() - itemStack.getDamage();
                        int m = itemStack3.getMaxDamage() - itemStack3.getDamage();
                        int n = m + itemStack2.getMaxDamage() * 12 / 100;
                        int o = kx + n;
                        int p = itemStack2.getMaxDamage() - o;
                        if (p < 0) {
                            p = 0;
                        }

                        if (p < itemStack2.getDamage()) {
                            itemStack2.setDamage(p);
                            i += 2;
                        }
                    }

                    System.out.println("Starting item combine");
                    ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                        RegistryEntry<Enchantment> registryEntry = (RegistryEntry<Enchantment>) entry.getKey();
                        int q = builder.getLevel(registryEntry);
                        int r = entry.getIntValue();
                        r = q == r ? r + 1 : Math.max(r, q);
                        Enchantment enchantment = registryEntry.value();
                        boolean bl4 = enchantment.isAcceptableItem(itemStack);
                        System.out.println(bl4);
                        if (this.player.getAbilities().creativeMode || itemStack.isOf(Items.ENCHANTED_BOOK)) {
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
                            if (bl) {
                                s = Math.max(1, s / 2);
                            }

                            i += s * r;
                            if (itemStack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                }
            }

            if (this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(itemStack.getName().getString())) {
                    j = 1;
                    i += j;
                    itemStack2.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
                }
            } else if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                j = 1;
                i += j;
                itemStack2.remove(DataComponentTypes.CUSTOM_NAME);
            }

            int t = (int) MathHelper.clamp(l + (long) i, 0L, 2147483647L);
            this.levelCost.set(t);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (j == i && j > 0 && this.levelCost.get() >= 40) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int kxx = itemStack2.getOrDefault(DataComponentTypes.REPAIR_COST, Integer.valueOf(0));
                if (kxx < itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, Integer.valueOf(0))) {
                    kxx = itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, Integer.valueOf(0));
                }

                if (j != i || j == 0) {
                    kxx = getNextCost(kxx);
                }

                itemStack2.set(DataComponentTypes.REPAIR_COST, kxx);
                EnchantmentHelper.set(itemStack2, builder.build());
            }

            this.output.setStack(0, itemStack2);
            this.sendContentUpdates();
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        }
        info.cancel();
    }
}
