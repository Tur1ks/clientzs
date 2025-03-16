package my.client;

import my.client.function.FuncRegister;
import my.client.function.ModuleManager;
import my.client.event.EventManager;
import my.client.gui.clickgui.ClickGui;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Client {
    private static Client instance;
    private FuncRegister funcRegister;
    private ModuleManager moduleManager;
    private EventManager eventManager;
    private ClickGui clickGui;

    public Client() {
        instance = this;

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

        //if (moduleManager != null) {
        //    moduleManager.onKeyPressed(keyCode, scanCode, modifiers);
        //}
    }

    public void onShutdown() {
    }

    public static Client getInstance() {
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