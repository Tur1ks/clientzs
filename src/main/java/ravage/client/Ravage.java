package ravage.client;

import ravage.client.function.FuncRegister;
import ravage.client.function.ModuleManager;
import ravage.client.event.EventManager;
import ravage.client.gui.clickgui.ClickGui;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import ravage.client.util.font.Fonts;

public class Ravage {
    private static Ravage instance;
    private FuncRegister funcRegister;
    private ModuleManager moduleManager;
    private EventManager eventManager;
    private ClickGui clickGui;

    public Ravage() {
        instance = this;
        /////////////////////////////////////////////////////////////////
        Fonts.init();
        /////////////////////////////////////////////////////////////////

        this.eventManager = new EventManager();
        this.funcRegister = new FuncRegister();
        this.moduleManager = this.funcRegister.getModuleManager();
        this.clickGui = new ClickGui();
    }

    public void onStart() {
    }

    public void onUpdate() {
        if (moduleManager != null) {
            moduleManager.onUpdate();
        }
    }

    public void onRender() {
        if (moduleManager != null) {
            moduleManager.onRender();
        }
    }

    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            Minecraft.getInstance().displayGuiScreen(clickGui);
        }

        if (moduleManager != null) {
            moduleManager.onKeyPress(keyCode);
        }
    }

    public void onShutdown() {
    }

    public static Ravage getInstance() {
        return instance;
    }

    public FuncRegister getFuncRegister() {
        return funcRegister;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ClickGui getClickGui() {
        return clickGui;
    }
}