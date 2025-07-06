package com.betterhorses.screen.widget;

import com.betterhorses.screen.BreedingChartScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class PageWidget extends ClickableWidget {
    BreedingChartScreen parent;
    BreedingChartScreen.Page pageBinding;


    public PageWidget(int x, int y, int width, int height, Text message, BreedingChartScreen parent, BreedingChartScreen.Page page) {
        super(x, y, width, height, message);
        this.parent = parent;
        this.pageBinding = page;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        //Rendering in a texture in the clipboard.png sprite sheet
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        parent.setCurrentPage(this.pageBinding);
    }


}
