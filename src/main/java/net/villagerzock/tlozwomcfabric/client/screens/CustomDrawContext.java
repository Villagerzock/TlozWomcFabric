package net.villagerzock.tlozwomcfabric.client.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

public class CustomDrawContext extends DrawContext {
    private final MatrixStack matrices;
    public CustomDrawContext(MinecraftClient client, VertexConsumerProvider.Immediate vertexConsumers) {
        super(client, vertexConsumers);
        this.matrices = getMatrices();
    }
    private void tryDraw() {
        this.draw();
    }
    public void fillVertGradient(int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        this.fillVertGradient(RenderLayer.getGui(), startX, startY, endX, endY, colorStart, colorEnd, z);
    }
    public void fillVertGradient(RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        VertexConsumer vertexConsumer = this.getVertexConsumers().getBuffer(layer);
        this.fillVertGradient(vertexConsumer, startX, startY, endX, endY, z, colorStart, colorEnd);
        this.tryDraw();
    }
    private void fillVertGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float f = (float) ColorHelper.Argb.getAlpha(colorStart) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(colorStart) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(colorStart) / 255.0F;
        float i = (float) ColorHelper.Argb.getBlue(colorStart) / 255.0F;
        float j = (float) ColorHelper.Argb.getAlpha(colorEnd) / 255.0F;
        float k = (float) ColorHelper.Argb.getRed(colorEnd) / 255.0F;
        float l = (float) ColorHelper.Argb.getGreen(colorEnd) / 255.0F;
        float m = (float) ColorHelper.Argb.getBlue(colorEnd) / 255.0F;
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)startY, (float)z).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)endY, (float)z).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)endY, (float)z).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)startY, (float)z).color(g, h, i, f).next();
    }
}
