package my.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class EventRender extends Event {
    private final float partialTicks;
    private final MatrixStack matrixStack;
    private final IRenderTypeBuffer buffer;
    private final MainWindow mainWindow;

    public EventRender(float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, MainWindow mainWindow) {
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
        this.buffer = buffer;
        this.mainWindow = mainWindow;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public IRenderTypeBuffer getBuffer() {
        return buffer;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}