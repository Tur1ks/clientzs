package my.client.function;

import my.client.function.impl.client.*;

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
        moduleManager.register(new ClickGuiModule());
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
        );
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}