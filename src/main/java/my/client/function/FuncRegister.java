package my.client.function;

import my.client.function.impl.client.*;

/**
 * Класс для регистрации всех функциональных модулей клиента
 */
public class FuncRegister {
    private final ModuleManager moduleManager;

    public FuncRegister() {
        // Создаем менеджер модулей
        moduleManager = new ModuleManager();

        // Регистрируем все модули
        registerModules();
    }

    private void registerModules() {
        registerClientModules();
        registerCombatModules();
        registerMovementModules();
        registerPlayerModules();
        registerRenderModules();
        registerMiscModules();

        System.out.println("Зарегистрировано " + moduleManager.getModuleCount() + " модулей");
    }
    private void registerClientModules() {
        // Здесь регистрируются модули категории CLIENT
        // Например:
        moduleManager.register(new ClickGuiModule());
        // Добавьте здесь другие модули категории CLIENT
    }
    private void registerCombatModules() {
        // Здесь регистрируются модули категории COMBAT
        // Например:
        moduleManager.registerAll(
                // Добавьте здесь другие модули категории COMBAT
        );
    }
    private void registerMovementModules() {
        // Здесь регистрируются модули категории MOVEMENT
        // Например:
        moduleManager.registerAll(
                // Добавьте здесь другие модули категории MOVEMENT
        );
    }
    private void registerPlayerModules() {
        // Здесь регистрируются модули категории PLAYER
        // Например:
        moduleManager.registerAll(
                // Добавьте здесь другие модули категории PLAYER
        );
    }
    private void registerRenderModules() {
        // Здесь регистрируются модули категории RENDER
        // Например:
        moduleManager.registerAll(
                // Добавьте здесь другие модули категории RENDER
        );
    }

    private void registerMiscModules() {
        // Здесь регистрируются модули категории MISC
        // Например:
        moduleManager.registerAll(
                // Добавьте здесь другие модули категории MISC
        );
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}