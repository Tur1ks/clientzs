package my.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;

public class EventRender2D extends Event {
    private final float partialTicks;
    private final MatrixStack matrixStack;
    private final MainWindow mainWindow;

    public EventRender2D(float partialTicks, MatrixStack matrixStack, MainWindow mainWindow) {
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
        this.mainWindow = mainWindow;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}