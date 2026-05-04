package com.betterhorses.mixin.horseshoe;

import com.betterhorses.BetterHorses;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Unique
    private final Identifier HORSESHOE_SLOT_TEXTURE = BetterHorses.identifier("container/horse/horseshoe_slot.png");

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/HorseScreen;ARMOR_SLOT_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private void drawSlot(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int midX = (this.width - this.backgroundWidth) / 2;
        int midY = (this.height - this.backgroundHeight) / 2;
        context.drawGuiTexture(HORSESHOE_SLOT_TEXTURE, midX + 7, midY + 35 + 18, 18, 18);
    }
}
