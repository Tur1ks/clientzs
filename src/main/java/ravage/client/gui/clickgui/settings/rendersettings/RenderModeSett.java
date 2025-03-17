package ravage.client.gui.clickgui.settings.rendersettings;

import com.mojang.blaze3d.matrix.MatrixStack;
import ravage.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class RenderModeSett {

    public void render(MatrixStack matrixStack, int x, int y, int width, int height,
                       List<String> modes, String currentMode, boolean hovering,
                       int accentColor, int bgColor, int hoverColor) {
        RenderUtil.drawRect(matrixStack, x, y, x + width, y + height,
                hovering ? hoverColor : bgColor);

        int currentIndex = modes.indexOf(currentMode);
        if (currentIndex == -1) return;

        float segmentWidth = (float) width / modes.size();

        RenderUtil.drawRect(matrixStack, x + (int)(currentIndex * segmentWidth), y,
                x + (int)((currentIndex + 1) * segmentWidth), y + height, accentColor);

        for (int i = 1; i < modes.size(); i++) {
            int divX = x + (int)(i * segmentWidth);
            RenderUtil.drawLine(matrixStack, divX, y, divX, y + height, 1f, hoverColor);
        }

        RenderUtil.drawLine(matrixStack, x, y, x + width, y + height, 1f,
                hovering ? accentColor : hoverColor);
    }
}