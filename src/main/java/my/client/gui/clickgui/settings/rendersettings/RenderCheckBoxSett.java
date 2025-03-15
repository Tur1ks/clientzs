package my.client.gui.clickgui.settings.rendersettings;

import com.mojang.blaze3d.matrix.MatrixStack;
import my.client.util.render.RenderUtil;

public class RenderCheckBoxSett {

    public void render(MatrixStack matrixStack, int x, int y, int width, int height,
                       boolean value, boolean hovering, int accentColor, int bgColor, int hoverColor) {
        // Рисуем фон чекбокса
        RenderUtil.drawRect(matrixStack, x, y, x + width, y + height,
                hovering ? hoverColor : bgColor);

        // Если чекбокс активен, рисуем галочку
        if (value) {
            RenderUtil.drawRect(matrixStack, x + 2, y + 2, x + width - 2, y + height - 2, accentColor);
        }

        // Рисуем рамку
        RenderUtil.drawLine(matrixStack, x, y, x + width, y + height, 1f,
                hovering ? accentColor : hoverColor);
    }
}