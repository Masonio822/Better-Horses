package com.betterhorses.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class TooltipWidget extends ClickableWidget {
    public TooltipWidget(int x, int y, int width, int height, Tooltip tooltip) {
        super(x, y, width, height, Text.empty());
        this.setTooltip(tooltip);
    }

    /*
     * ---------------------------
     * Function-removing overrides
     * ---------------------------
     */

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        //Invisible widget (Operates in the background)
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        //Disabled to prevent anything happening on click including sound
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        //Disabled to prevent anything happening on click including sound
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
}
