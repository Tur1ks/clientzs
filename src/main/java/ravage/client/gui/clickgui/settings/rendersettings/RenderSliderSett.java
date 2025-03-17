package ravage.client.gui.clickgui.settings.rendersettings;

import com.mojang.blaze3d.matrix.MatrixStack;
import ravage.client.util.render.RenderUtil;

public class RenderSliderSett {

    public void render(MatrixStack matrixStack, int x, int y, int width, int height,
                       float value, float min, float max, boolean hovering,
                       int accentColor, int bgColor, int hoverColor) {
        RenderUtil.drawRect(matrixStack, x, y, x + width, y + height,
                hovering ? hoverColor : bgColor);

        float percentage = (value - min) / (max - min);
        int filledWidth = (int)(width * percentage);

        RenderUtil.drawRect(matrixStack, x, y, x + filledWidth, y + height, accentColor);

        int knobSize = height * 2;
        RenderUtil.drawRect(matrixStack, x + filledWidth - knobSize/2, y - knobSize/2 + height/2,
                x + filledWidth + knobSize/2, y + knobSize/2 + height/2,
                hovering ? accentColor : hoverColor);

        RenderUtil.drawLine(matrixStack, x, y, x + width, y + height, 1f,
                hovering ? accentColor : hoverColor);
    }
}