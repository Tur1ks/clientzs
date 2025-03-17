package ravage.client.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void drawRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(red, green, blue, alpha);

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(matrix, left, bottom, 0.0F).endVertex();
        bufferBuilder.pos(matrix, right, bottom, 0.0F).endVertex();
        bufferBuilder.pos(matrix, right, top, 0.0F).endVertex();
        bufferBuilder.pos(matrix, left, top, 0.0F).endVertex();
        Tessellator.getInstance().draw();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedRect(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int color) {
        drawRect(matrixStack, x + radius, y, x + width - radius, y + height, color);
        drawRect(matrixStack, x, y + radius, x + width, y + height - radius, color);

        drawFilledCircle(matrixStack, x + radius, y + radius, radius, color);
        drawFilledCircle(matrixStack, x + width - radius, y + radius, radius, color);
        drawFilledCircle(matrixStack, x + radius, y + height - radius, radius, color);
        drawFilledCircle(matrixStack, x + width - radius, y + height - radius, radius, color);
    }

    public static void drawFilledCircle(MatrixStack matrixStack, float x, float y, float radius, int color) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        RenderSystem.color4f(red, green, blue, alpha);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        buffer.pos(matrix, x, y, 0).endVertex();

        for (int i = 0; i <= 360; i++) {
            double angle = Math.PI * 2 * i / 360;
            buffer.pos(matrix, (float) (x + Math.sin(angle) * radius), (float) (y + Math.cos(angle) * radius), 0).endVertex();
        }

        Tessellator.getInstance().draw();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void drawCircle(MatrixStack matrixStack, float x, float y, float radius, int color) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        RenderSystem.color4f(red, green, blue, alpha);
        GL11.glLineWidth(1.5F);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);

        Matrix4f matrix = matrixStack.getLast().getMatrix();

        for (int i = 0; i <= 360; i++) {
            double angle = Math.PI * 2 * i / 360;
            buffer.pos(matrix, (float) (x + Math.sin(angle) * radius), (float) (y + Math.cos(angle) * radius), 0).endVertex();
        }

        Tessellator.getInstance().draw();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void drawGradientRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int startColor, int endColor) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        float alpha1 = (float) (startColor >> 24 & 255) / 255.0F;
        float red1 = (float) (startColor >> 16 & 255) / 255.0F;
        float green1 = (float) (startColor >> 8 & 255) / 255.0F;
        float blue1 = (float) (startColor & 255) / 255.0F;
        float alpha2 = (float) (endColor >> 24 & 255) / 255.0F;
        float red2 = (float) (endColor >> 16 & 255) / 255.0F;
        float green2 = (float) (endColor >> 8 & 255) / 255.0F;
        float blue2 = (float) (endColor & 255) / 255.0F;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(matrix, right, top, 0).color(red1, green1, blue1, alpha1).endVertex();
        bufferBuilder.pos(matrix, left, top, 0).color(red1, green1, blue1, alpha1).endVertex();
        bufferBuilder.pos(matrix, left, bottom, 0).color(red2, green2, blue2, alpha2).endVertex();
        bufferBuilder.pos(matrix, right, bottom, 0).color(red2, green2, blue2, alpha2).endVertex();
        Tessellator.getInstance().draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static void drawLine(MatrixStack matrixStack, float x1, float y1, float x2, float y2, float thickness, int color) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(red, green, blue, alpha);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(thickness);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(matrix, x1, y1, 0).endVertex();
        bufferBuilder.pos(matrix, x2, y2, 0).endVertex();
        Tessellator.getInstance().draw();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawTexture(MatrixStack matrixStack, ResourceLocation texture, float x, float y, float width, float height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bindTexture(texture);

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(matrix, x, y + height, 0).tex(0, 1).endVertex();
        bufferBuilder.pos(matrix, x + width, y + height, 0).tex(1, 1).endVertex();
        bufferBuilder.pos(matrix, x + width, y, 0).tex(1, 0).endVertex();
        bufferBuilder.pos(matrix, x, y, 0).tex(0, 0).endVertex();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void drawColoredTexture(MatrixStack matrixStack, ResourceLocation texture, float x, float y, float width, float height, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getTextureManager().bindTexture(texture);

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        bufferBuilder.pos(matrix, x, y + height, 0).color(red, green, blue, alpha).tex(0, 1).endVertex();
        bufferBuilder.pos(matrix, x + width, y + height, 0).color(red, green, blue, alpha).tex(1, 1).endVertex();
        bufferBuilder.pos(matrix, x + width, y, 0).color(red, green, blue, alpha).tex(1, 0).endVertex();
        bufferBuilder.pos(matrix, x, y, 0).color(red, green, blue, alpha).tex(0, 0).endVertex();
        Tessellator.getInstance().draw();

        RenderSystem.disableBlend();
    }

    public static void drawShadow(MatrixStack matrixStack, float x, float y, float width, float height, float shadowSize) {
        drawGradientRect(matrixStack, x - shadowSize, y - shadowSize, x, y + height,
                new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());
        drawGradientRect(matrixStack, x, y - shadowSize, x + width, y,
                new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());

        drawGradientRect(matrixStack, x + width, y, x + width + shadowSize, y + height,
                new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 50).getRGB());
        drawGradientRect(matrixStack, x, y + height, x + width, y + height + shadowSize,
                new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 50).getRGB());

        drawGradientRect(matrixStack, x + width, y - shadowSize, x + width + shadowSize, y,
                new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 0).getRGB());
        drawGradientRect(matrixStack, x - shadowSize, y + height, x, y + height + shadowSize,
                new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 0).getRGB());
    }

    public static void drawTextWithShadow(MatrixStack matrixStack, String text, float x, float y, int color) {
        mc.fontRenderer.drawStringWithShadow(matrixStack, text, x, y, color);
    }

    public static void drawCenteredText(MatrixStack matrixStack, String text, float x, float y, int color) {
        mc.fontRenderer.drawStringWithShadow(matrixStack, text, x - mc.fontRenderer.getStringWidth(text) / 2, y, color);
    }

    public static void scissor(float x, float y, float width, float height) {
        final float scale = (float) mc.getMainWindow().getGuiScaleFactor();
        GL11.glScissor((int) (x * scale), (int) ((mc.getMainWindow().getFramebufferHeight() - (y + height)) * scale), (int) (width * scale), (int) (height * scale));
    }

    public static void enableScissor() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}