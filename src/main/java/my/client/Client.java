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

        // Initialize event manager
        this.eventManager = new EventManager();
        System.out.println("Event Manager initialized!");

        // Initialize function register which will register all modules
        this.funcRegister = new FuncRegister();
        this.moduleManager = this.funcRegister.getModuleManager();
        System.out.println("Function Register initialized!");

        // Initialize ClickGui
        this.clickGui = new ClickGui();
        System.out.println("ClickGui initialized!");

        // Client initialization complete
        System.out.println("Client initialized!");
    }

    public void onStart() {
        // Method called when Minecraft starts
        System.out.println("Client started!");
    }

    public void onUpdate() {
        // Method for updating the client every frame/tick
        if (moduleManager != null) {
            moduleManager.onUpdate();
        }
    }

    public void onRender() {
        // Method for rendering client elements
        if (moduleManager != null) {
            moduleManager.onRender();
        }
    }

    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
        // Method for handling key presses
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            // Open ClickGui when right shift is pressed
            Minecraft.getInstance().displayGuiScreen(clickGui);
        }

        // Pass key press to module manager for module keybinds
        //if (moduleManager != null) {
        //    moduleManager.onKeyPressed(keyCode, scanCode, modifiers);
        //}
    }

    public void onShutdown() {
        // Method for actions when the game closes
        System.out.println("Client shutdown!");
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