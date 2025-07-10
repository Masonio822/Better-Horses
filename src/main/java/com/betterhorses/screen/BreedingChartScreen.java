package com.betterhorses.screen;

import com.betterhorses.BetterHorses;
import com.betterhorses.duck.TrackedParents;
import com.betterhorses.horse.HorseHelper;
import com.betterhorses.screen.widget.PageWidget;
import com.betterhorses.screen.widget.TooltipWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

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
    private final EntityType<? extends AbstractHorseEntity> horseType;
    private boolean facing;
    private Page currentPage;

    private ArrayList<TooltipWidget> widgets;

    @SuppressWarnings("unchecked")
    public BreedingChartScreen(Text title, AbstractHorseEntity horse) {
        super(title);
        /*
        This is here just in case someone attempts to mixin or change the params so they can feed in another entity
        That would be very damaging because of the unchecked casts below that rely on the param being an AbstractHorseEntity
         */
        if (!(horse instanceof AbstractHorseEntity)) {
            throw new IllegalArgumentException("Entity passed to BreedingChartScreen can only be of type 'AbstractHorseEntity'");
        }

        this.horse = horse.getClass().cast(horse);
        horseType = (EntityType<? extends AbstractHorseEntity>) EntityType.get(horse.getType().getTranslationKey()).orElse(horse.getType());
        currentPage = Page.STATISTIC;
        facing = Math.random() > 0.5;
        widgets = new ArrayList<>(3);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        widgets.forEach(w -> w.visible = false);

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
                widgets.forEach(w -> w.visible = true);
                //Draw Horse Borders
                context.drawTexture(CLIPBOARD_TEXTURE, midX - 58, midY - 70, 51, 51, 164, 0, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
                context.drawTexture(CLIPBOARD_TEXTURE, midX + 7, midY - 70, 51, 51, 164, 51, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
                context.drawTexture(CLIPBOARD_TEXTURE, midX - 25, midY + 21, 51, 51, 164, 0, 51, 51, CLIPBOARD_SIZE, CLIPBOARD_SIZE);

                AbstractHorseEntity[] parents = getParents(horse);
                if (parents != null) {
                    InventoryScreen.drawEntity(context, midX - 58, midY - 68, midX - 65 + 50, midY - 68 + 50, 15, 0.25f, this.width, 81f, parents[0]);
                    InventoryScreen.drawEntity(context, midX + 7, midY - 68, midX + 15 + 50, midY - 68 + 50, 15, 0.25f, 0f, 81f, parents[1]);
                } else {
                    //TODO create horse silhouette with question mark in addition to text
                }

                AbstractHorseEntity horseCopy = horseType.create(horse.getWorld());
                horseCopy.copyFrom(horse);
                NbtCompound copyNbt = horseCopy.writeNbt(new NbtCompound());
                copyNbt.remove("Brain");
                copyNbt.remove("Fire");
                copyNbt.remove("OnGround");
                copyNbt.remove("FallFlying");
                copyNbt.remove("HurtTime");
                horseCopy.readNbt(copyNbt);
                InventoryScreen.drawEntity(context, midX - 25, midY + 23, midX - 25 + 50, midY + 23 + 50, 15, 0.25f, facing ? (float) this.width : 0f, 172f, horseCopy);

                //Draw Heart & Connection Lines
                int startY = midY - 20;
                final int maleColor = 0xFF1D9CD6;
                final int femaleColor = 0xFFFF91B4;
                final int black = 0xFF000000;
                //Left (Male) Horse
                context.fill(midX - 34, startY, midX - 31, midY, maleColor);
                context.drawVerticalLine(midX - 35, startY, midY + 2, black);
                context.drawVerticalLine(midX - 31, startY, midY - 2, black);
                context.fill(midX - 34, midY - 1, midX, midY + 2, maleColor);
                context.drawHorizontalLine(midX - 35, midX, midY + 2, black);
                context.drawHorizontalLine(midX - 31, midX, midY - 2, black);
                //Right (Female) Horse
                context.fill(midX + 35, startY, midX + 32, midY, femaleColor);
                context.drawVerticalLine(midX + 35, startY, midY + 2, black);
                context.drawVerticalLine(midX + 31, startY, midY - 2, black);
                context.fill(midX + 35, midY - 1, midX, midY + 2, femaleColor);
                context.drawHorizontalLine(midX + 35, midX, midY + 2, black);
                context.drawHorizontalLine(midX + 31, midX, midY - 2, black);
                //Middle (Child) Horse
                context.fill(midX - 1, midY, midX + 2, midY + 21, 0xFF8E96C5);
                context.drawVerticalLine(midX - 2, midY, midY + 21, black);
                context.drawVerticalLine(midX + 2, midY, midY + 21, black);

                context.drawTexture(CLIPBOARD_TEXTURE, midX - 12, midY - 12, 24, 24, 0, 215, 24, 24, CLIPBOARD_SIZE, CLIPBOARD_SIZE);
            }
            default -> BetterHorses.LOGGER.warn("Unknown page!");
        }
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
        if (currentPage != this.currentPage) {
            this.currentPage = currentPage;
            facing = Math.random() > 0.5;
        }
    }

    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    @Nullable
    private static AbstractHorseEntity[] getParents(AbstractHorseEntity horse) {
        EntityType<? extends AbstractHorseEntity> type = (EntityType<? extends AbstractHorseEntity>) EntityType.get(horse.getType().getTranslationKey()).orElse(horse.getType());
        TrackedParents tp = (TrackedParents) horse;
        if (tp.getParentsNbt().isPresent()) {
            List<AbstractHorseEntity> parentsList = new ArrayList<>(2);
            ((NbtList) tp.getParentsNbt().get().get("Parents"))
                    .forEach(p -> {
                        AbstractHorseEntity h = type.create(horse.getWorld());
                        h.readNbt((NbtCompound) p);
                        parentsList.add(h);
                    });
            return parentsList.toArray(new AbstractHorseEntity[2]);
        }
        return null;
    }

    public static void fillHorizontalGradient(DrawContext context, int startX, int startY, int endX, int endY, int startColor, int endColor) {
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        VertexConsumer vc = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        vc.vertex(matrix4f, (float) startX, (float) endY, 0f).color(endColor);
        vc.vertex(matrix4f, (float) startX, (float) startY, 0f).color(startColor);
        vc.vertex(matrix4f, (float) endX, (float) startY, 0f).color(startColor);
        vc.vertex(matrix4f, (float) endX, (float) endY, 0f).color(endColor);

        context.draw();
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - 164) / 2;
        int y = 5;

        this.addDrawableChild(new PageWidget(x + 148, y + 42, 15, 13, Text.empty(), this, Page.STATISTIC));
        this.addDrawableChild(new PageWidget(x + 148, y + 61, 15, 13, Text.empty(), this, Page.ANCESTRY));

        int midX = this.width / 2;
        int midY = this.height / 2;
        AbstractHorseEntity[] parents = getParents(horse);

        if (parents != null) {
            //Male Tooltip
            MutableText maleText = MutableText.of(Text.translatable("text.betterhorses.male").getContent()).withColor(0xFF1D9CD6);
            maleText.append(getTooltip(parents[0].getAttributes()));
            widgets.add(new TooltipWidget(midX - 58, midY - 68, 50, 50, Tooltip.of(maleText)));

            //Female Tooltip
            MutableText femaleText = MutableText.of(Text.translatable("text.betterhorses.female").getContent()).withColor(0xFFFF91B4);
            femaleText.append(getTooltip(parents[1].getAttributes()));
            widgets.add(new TooltipWidget(midX + 7, midY - 68, 50, 50, Tooltip.of(femaleText)));
        } else {
            widgets.add(new TooltipWidget(midX - 58, midY - 68, 50, 50, Tooltip.of(getTooltip())));
            widgets.add(new TooltipWidget(midX + 7, midY - 68, 50, 50, Tooltip.of(getTooltip())));
        }
        //Child Tooltip
        MutableText childText = MutableText.of(Text.translatable("text.betterhorses.child").getContent()).withColor(0xFF8E96C5);
        childText.append(getTooltip(horse.getAttributes()));
        widgets.add(new TooltipWidget(midX - 25, midY + 23, 50, 50, Tooltip.of(childText)));

        widgets.forEach(w -> {
            w.visible = false;
            this.addDrawableChild(w);
        });
    }

    private static MutableText getTooltip(AttributeContainer attributeContainer) {
        MutableText text = Text.empty();
        text.append("\n");
        text.append(Text.translatable("text.betterhorses.health", HorseHelper.roundToTwoDecimals(attributeContainer.getValue(EntityAttributes.GENERIC_MAX_HEALTH))));
        text.append("\n");
        text.append(Text.translatable("text.betterhorses.speed", HorseHelper.convertToBlocksPerSecond(attributeContainer.getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED))));
        text.append("\n");
        text.append(Text.translatable("text.betterhorses.jump", HorseHelper.convertToJumpBlocks(attributeContainer.getValue(EntityAttributes.GENERIC_JUMP_STRENGTH))));
        return text;
    }

    private static MutableText getTooltip() {
        MutableText mutableText = MutableText.of(Text.translatable("text.betterhorses.wild").getContent()).formatted(Formatting.GRAY);
        mutableText.append("\n");
        mutableText.append(Text.translatable("text.betterhorses.health", "?"));
        mutableText.append("\n");
        mutableText.append(Text.translatable("text.betterhorses.speed", "?"));
        mutableText.append("\n");
        mutableText.append(Text.translatable("text.betterhorses.jump", "?"));
        return mutableText;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
