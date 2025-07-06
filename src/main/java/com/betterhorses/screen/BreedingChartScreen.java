package com.betterhorses.screen;

import com.betterhorses.BetterHorses;
import com.betterhorses.duck.TrackedParents;
import com.betterhorses.horse.HorseHelper;
import com.betterhorses.screen.widget.PageWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen to view the stats of {@link AbstractHorseEntity}.
 *
 * @see com.betterhorses.item.BreedingChartItem
 */
public class BreedingChartScreen extends Screen {

    private static final Identifier CLIPBOARD_TEXTURE = Identifier.of(BetterHorses.MOD_ID, "textures/gui/clipboard.png");
    private static final int CLIPBOARD_SIZE = 256;

    private final AbstractHorseEntity horse;
    private final float facing;
    private Page currentPage;

    public BreedingChartScreen(Text title, AbstractHorseEntity horse) {
        super(title);
        this.horse = horse.getClass().cast(horse);
        currentPage = Page.STATISTIC;
        facing = Math.random() > 0.5 ? (float) this.width : 0f;
        System.out.println("constructor called");
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int midX = this.width / 2;
        int midY = this.height / 2;

        switch (currentPage) {
            case STATISTIC -> {
                int textX = (this.width - 164) / 2 + 24;
                int textY = 44;

                InventoryScreen.drawEntity(context, midX + 7, midY - 80, midX + 7 + 60, midY - 80 + 60, 20, 0.25F, (float) mouseX + 50, (float) mouseY, this.horse);

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

                context.drawText(
                        this.textRenderer,
                        Text.translatable("text.betterhorses.coloration").formatted(Formatting.BLACK, Formatting.BOLD, Formatting.UNDERLINE),
                        textX,
                        textY + 115,
                        0,
                        false
                );

                int variant = horse.writeNbt(new NbtCompound()).getInt("Variant");
                context.drawText(
                        this.textRenderer,
                        Text.translatable("tooltip.betterhorses.color").append(Text.translatable("color.minecraft.horse." + HorseHelper.getColor(variant))),
                        textX,
                        textY + 130,
                        0,
                        false
                );

                context.drawText(
                        this.textRenderer,
                        Text.translatable("tooltip.betterhorses.marking").append(Text.translatable("marking.minecraft.horse." + HorseHelper.getMarkings(variant))),
                        textX,
                        textY + 145,
                        0,
                        false
                );
            }

            case ANCESTRY -> {
                //Draw Horse Borders
                context.drawTexture(CLIPBOARD_TEXTURE, midX - 58, midY - 70, 51, 51, 164, 0, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
                context.drawTexture(CLIPBOARD_TEXTURE, midX + 7, midY - 70, 51, 51, 164, 51, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
                context.drawTexture(CLIPBOARD_TEXTURE, midX - 25, midY + 21, 51, 51, 164, 0, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);

                //Convert NBT to horse entities
                TrackedParents tp = (TrackedParents) horse;
                if (tp.getParentsNbt().isPresent()) {
                    List<AbstractHorseEntity> parentsList = new ArrayList<>(2);
                    ((NbtList) tp.getParentsNbt().get().get("Parents"))
                            .forEach(p -> {
                                AbstractHorseEntity h = EntityType.HORSE.create(horse.getWorld());
                                h.readNbt((NbtCompound) p);
                                parentsList.add(h);
                            });
                    AbstractHorseEntity[] parents = parentsList.toArray(new AbstractHorseEntity[2]);

                    //Draw Logic (if horse has parents)
                    InventoryScreen.drawEntity(context, midX - 58, midY - 68, midX - 65 + 50, midY - 68 + 50, 15, 0.25f, this.width, 81f, parents[0]);
                    InventoryScreen.drawEntity(context, midX + 7, midY - 68, midX + 15 + 50, midY - 68 + 50, 15, 0.25f, 0f, 81f, parents[1]);
                } else {
                    //TODO create horse silhouette with question mark in addition to the text
                    context.drawText(this.textRenderer, Text.translatable("text.betterhorses.wild"), midX + 50, midY - 75, 0, false);
                    context.drawText(this.textRenderer, Text.translatable("text.betterhorses.wild"), midX - 50, midY - 75, 0, false);
                }

                //TODO copy horse to present in static state
                InventoryScreen.drawEntity(context, midX - 25, midY + 23, midX - 25 + 50, midY + 23 + 50, 15, 0.25f, facing, 172f, horse);

                //Draw Heart & Connection Lines
                context.drawTexture(CLIPBOARD_TEXTURE, midX - 12, midY - 12, 24, 24, 0, 215, 24, 24, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
            }
            default -> BetterHorses.LOGGER.warn("Unknown page!");
        }
//        System.out.println("(X: " + mouseX + ", Y: " + mouseY + ")");
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        int x = (this.width - 164) / 2;
        int y = 5;

        context.drawTexture(CLIPBOARD_TEXTURE, x, y, 164, 215, 0, 0, 164, 215, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
    }

    public enum Page {
        STATISTIC,
        ANCESTRY
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - 164) / 2;
        int y = 5;

        this.addDrawableChild(new PageWidget(x + 148, y + 42, 15, 13, Text.empty(), this, Page.STATISTIC));
        this.addDrawableChild(new PageWidget(x + 148, y + 61, 15, 13, Text.empty(), this, Page.ANCESTRY));
    }
}
