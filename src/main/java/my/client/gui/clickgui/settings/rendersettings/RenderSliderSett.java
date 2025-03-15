package my.client.gui.clickgui.settings.rendersettings;

import com.mojang.blaze3d.matrix.MatrixStack;
import my.client.util.render.RenderUtil;

public class RenderSliderSett {

    public void render(MatrixStack matrixStack, int x, int y, int width, int height,
                       float value, float min, float max, boolean hovering,
                       int accentColor, int bgColor, int hoverColor) {
        // Рисуем фон слайдера
        RenderUtil.drawRect(matrixStack, x, y, x + width, y + height,
                hovering ? hoverColor : bgColor);

        // Рассчитываем заполненную часть слайдера
        float percentage = (value - min) / (max - min);
        int filledWidth = (int)(width * percentage);

        // Рисуем заполненную часть
        RenderUtil.drawRect(matrixStack, x, y, x + filledWidth, y + height, accentColor);

        // Рисуем ползунок
        int knobSize = height * 2;
        RenderUtil.drawRect(matrixStack, x + filledWidth - knobSize/2, y - knobSize/2 + height/2,
                x + filledWidth + knobSize/2, y + knobSize/2 + height/2,
                hovering ? accentColor : hoverColor);

        // Рисуем рамку
        RenderUtil.drawLine(matrixStack, x, y, x + width, y + height, 1f,
                hovering ? accentColor : hoverColor);
    }
}