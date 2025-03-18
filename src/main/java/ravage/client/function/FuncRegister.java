package ravage.client.function;

import org.lwjgl.glfw.GLFW;
import ravage.client.function.impl.client.*;
import ravage.client.function.impl.misc.Test;

public class FuncRegister {
    private final ModuleManager moduleManager;

    public FuncRegister() {
        moduleManager = new ModuleManager();
        registerModules();
    }

    private void registerModules() {
        registerClientModules();
        registerCombatModules();
        registerMovementModules();
        registerPlayerModules();
        registerRenderModules();
        registerMiscModules();
    }
    private void registerClientModules() {
        moduleManager.registerAll(
                new ClickGuiModule()
        );
    }
    private void registerCombatModules() {
        moduleManager.registerAll(
        );
    }
    private void registerMovementModules() {
        moduleManager.registerAll(
        );
    }
    private void registerPlayerModules() {
        moduleManager.registerAll(
        );
    }
    private void registerRenderModules() {
        moduleManager.registerAll(
        );
    }

    private void registerMiscModules() {
        moduleManager.registerAll(
                new Test()
        );
        Test test = new Test();
        test.toggle();
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}