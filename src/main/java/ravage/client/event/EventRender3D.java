package ravage.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;

public class EventRender3D extends Event {
    private final float partialTicks;
    private final MatrixStack matrixStack;

    public EventRender3D(float partialTicks, MatrixStack matrixStack) {
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }
}