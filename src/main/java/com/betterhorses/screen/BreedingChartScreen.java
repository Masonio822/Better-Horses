package com.betterhorses.screen;

import com.betterhorses.BetterHorses;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class BreedingChartScreen extends Screen {

    private static final Identifier CLIPBOARD_TEXTURE = Identifier.of(BetterHorses.MOD_ID, "textures/gui/clipboard.png");

    private final LivingEntity entity;

    public BreedingChartScreen(Text title, LivingEntity entity) {
        super(title);
        this.entity = entity;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int midX = this.width / 2;
        int midY = this.height / 2;

        InventoryScreen.drawEntity(context, midX + 20, midY - 50, (midX + 20) + 30, (midY - 50) + 30, 10, 0.25f, (float) mouseX, (float) mouseX, this.entity);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        context.drawTexture(CLIPBOARD_TEXTURE, (this.width - 164) / 2, 5, 0, 0, 164, 215, 164, 215);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
