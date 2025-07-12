package com.betterhorses.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererMixin {
    @ModifyVariable(method = "render*", at = @At("STORE"))
    private VertexConsumer renderHorseArmorGlint(VertexConsumer vertexConsumer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HorseEntity horseEntity,
                                                 float f, float g, float h, float j, float k, float l) {
        if (horseEntity.getBodyArmor().getItem() instanceof AnimalArmorItem animalArmorItem) {
            return ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(animalArmorItem.getEntityTexture()), horseEntity.getBodyArmor().hasGlint());
        }
        return vertexConsumer;
    }
}
