package com.betterhorses.mixin.horseshoe;

import com.betterhorses.BetterHorses;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Shadow
    @Final
    private AbstractHorseEntity entity;
    @Unique
    private static final Identifier HORSESHOE_SLOT_TEXTURE = BetterHorses.identifier("container/horse/horseshoe_slot");

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawSlot(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (entity.canUseSlot(EquipmentSlot.BETTER_HORSES_ANIMAL_FEET)) { //TODO donkey still has slot
            int midX = (this.width - this.backgroundWidth) / 2;
            int midY = (this.height - this.backgroundHeight) / 2;
            context.drawGuiTexture(HORSESHOE_SLOT_TEXTURE, midX + 7, midY + 35 + 18, 18, 18);
        }
    }
}
