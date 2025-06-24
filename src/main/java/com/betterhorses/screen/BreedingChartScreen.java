package com.betterhorses.screen;

import com.betterhorses.BetterHorses;
import com.betterhorses.horse.HorseHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * Screen to view the stats of {@link com.betterhorses.horse.Chartable} entities.
 *
 * @see com.betterhorses.item.BreedingChartItem
 */
public class BreedingChartScreen extends Screen {

    private static final Identifier CLIPBOARD_TEXTURE = Identifier.of(BetterHorses.MOD_ID, "textures/gui/clipboard.png");

    private final AbstractHorseEntity horse;

    public BreedingChartScreen(Text title, AbstractHorseEntity horse) {
        super(title);
        this.horse = horse;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int midX = this.width / 2;
        int midY = this.height / 2;
        int textX = (this.width - 164) / 2 + 24;
        int textY = 44;

        InventoryScreen.drawEntity(context, midX + 7, midY - 80, midX + 7 + 60, midY - 80 + 60, 20, 0.25F, (float) mouseX, (float) mouseX, this.horse);

        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.info").formatted(Formatting.BLACK, Formatting.UNDERLINE, Formatting.BOLD),
                textX,
                textY,
                0,
                false
        );
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.name", MutableText.of(horse.getName().getContent()).formatted(Formatting.BLACK)),
                textX,
                textY + 15,
                0,
                false
        );
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.type", MutableText.of(horse.getType().getName().getContent()).formatted(Formatting.BLACK)),
                textX,
                textY + 30,
                0,
                false
        );
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.attribute").formatted(Formatting.BLACK, Formatting.UNDERLINE, Formatting.BOLD),
                textX,
                textY + 50,
                0,
                false
        );

        double h = horse.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH);
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.health", Text.literal(String.valueOf(HorseHelper.roundToTwoDecimals(h))).formatted(HorseHelper.getFormatForStat(EntityAttributes.GENERIC_MAX_HEALTH, h))),
                textX,
                textY + 65,
                0,
                false
        );

        double s = horse.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.speed", Text.literal(String.valueOf(HorseHelper.convertToBlocksPerSecond(s))).formatted(HorseHelper.getFormatForStat(EntityAttributes.GENERIC_MOVEMENT_SPEED, s))),
                textX,
                textY + 80,
                0,
                false
        );

        double j = horse.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH);
        context.drawText(
                this.textRenderer,
                Text.translatable("text.betterhorses.jump", Text.literal(String.valueOf(HorseHelper.convertToJumpBlocks(j))).formatted(HorseHelper.getFormatForStat(EntityAttributes.GENERIC_JUMP_STRENGTH, j))),
                textX,
                textY + 95,
                0,
                false
        );
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
