package my.client.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер модулей, отвечающий за регистрацию, хранение и управление модулями
 */
public class ModuleManager {
    private static ModuleManager instance;

    // Основной список всех модулей
    private final List<Module> modules = new ArrayList<>();

    // Карты для быстрого доступа к модулям
    private final Map<String, Module> modulesByName = new HashMap<>();
    private final Map<Category, List<Module>> modulesByCategory = new HashMap<>();
    private final Map<Integer, List<Module>> modulesByKey = new HashMap<>();

    public ModuleManager() {
        instance = this;

        // Инициализация списков для каждой категории
        for (Category category : Category.values()) {
            modulesByCategory.put(category, new ArrayList<>());
        }
    }

    /**
     * Регистрирует новый модуль в системе
     * @param module Модуль для регистрации
     * @return true если модуль успешно зарегистрирован, false если модуль с таким именем уже существует
     */
    public boolean register(Module module) {
        if (modulesByName.containsKey(module.getName().toLowerCase())) {
            System.err.println("Модуль с именем " + module.getName() + " уже зарегистрирован!");
            return false;
        }

        // Добавляем модуль в основной список
        modules.add(module);

        // Добавляем модуль в карты для быстрого доступа
        modulesByName.put(module.getName().toLowerCase(), module);
        modulesByCategory.get(module.getCategory()).add(module);

        // Если у модуля есть клавиша, добавляем его в соответствующий список
        if (module.getKey() != 0) {
            modulesByKey.computeIfAbsent(module.getKey(), k -> new ArrayList<>()).add(module);
        }

        return true;
    }

    /**
     * Отменяет регистрацию модуля
     * @param module Модуль для удаления
     */
    public void unregister(Module module) {
        modules.remove(module);
        modulesByName.remove(module.getName().toLowerCase());
        modulesByCategory.get(module.getCategory()).remove(module);

        if (module.getKey() != 0 && modulesByKey.containsKey(module.getKey())) {
            modulesByKey.get(module.getKey()).remove(module);
            if (modulesByKey.get(module.getKey()).isEmpty()) {
                modulesByKey.remove(module.getKey());
            }
        }
    }

    /**
     * Обновляет ключ модуля в системе
     * @param module Модуль для обновления
     * @param oldKey Старый ключ
     * @param newKey Новый ключ
     */
    public void updateModuleKey(Module module, int oldKey, int newKey) {
        // Удаляем модуль из старого списка ключей
        if (oldKey != 0 && modulesByKey.containsKey(oldKey)) {
            modulesByKey.get(oldKey).remove(module);
            if (modulesByKey.get(oldKey).isEmpty()) {
                modulesByKey.remove(oldKey);
            }
        }

        // Добавляем модуль в новый список ключей
        if (newKey != 0) {
            modulesByKey.computeIfAbsent(newKey, k -> new ArrayList<>()).add(module);
        }
    }

    /**
     * Регистрирует несколько модулей сразу
     * @param modules Список модулей для регистрации
     */
    public void registerAll(Module... modules) {
        for (Module module : modules) {
            register(module);
        }
    }

    /**
     * Получает модуль по имени
     * @param name Имя модуля (регистр не имеет значения)
     * @return Модуль или null, если модуль не найден
     */
    public Module getModuleByName(String name) {
        return modulesByName.get(name.toLowerCase());
    }

    /**
     * Получает список всех модулей
     * @return Список всех зарегистрированных модулей
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    /**
     * Получает список модулей определенной категории
     * @param category Категория модулей
     * @return Список модулей указанной категории
     */
    public List<Module> getModulesByCategory(Category category) {
        return new ArrayList<>(modulesByCategory.get(category));
    }

    /**
     * Получает список модулей, привязанных к определенной клавише
     * @param key Код клавиши
     * @return Список модулей, привязанных к указанной клавише
     */
    public List<Module> getModulesByKey(int key) {
        return modulesByKey.containsKey(key) ? new ArrayList<>(modulesByKey.get(key)) : new ArrayList<>();
    }

    /**
     * Обрабатывает нажатие клавиши и переключает соответствующие модули
     * @param keyCode Код нажатой клавиши
     * @return true если хотя бы один модуль был переключен
     */
    public boolean onKeyPress(int keyCode) {
        boolean handled = false;

        if (modulesByKey.containsKey(keyCode)) {
            for (Module module : modulesByKey.get(keyCode)) {
                module.toggle();
                handled = true;
            }
        }

        return handled;
    }

    /**
     * Вызывает метод onUpdate для всех включенных модулей
     */
    public void onUpdate() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onUpdate();
            }
        }
    }

    /**
     * Вызывает метод onRender для всех включенных модулей
     */
    public void onRender() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onRender();
            }
        }
    }

    /**
     * Включает модуль по имени
     * @param name Имя модуля
     * @return true если модуль найден и включен
     */
    public boolean enableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null && !module.isEnabled()) {
            module.toggle();
            return true;
        }
        return false;
    }

    /**
     * Выключает модуль по имени
     * @param name Имя модуля
     * @return true если модуль найден и выключен
     */
    public boolean disableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null && module.isEnabled()) {
            module.toggle();
            return true;
        }
        return false;
    }

    /**
     * Получает количество зарегистрированных модулей
     * @return Общее количество модулей
     */
    public int getModuleCount() {
        return modules.size();
    }

    /**
     * Получает количество включенных модулей
     * @return Количество включенных модулей
     */
    public int getEnabledModuleCount() {
        int count = 0;
        for (Module module : modules) {
            if (module.isEnabled()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Получает экземпляр ModuleManager
     * @return Экземпляр ModuleManager
     */
    public static ModuleManager getInstance() {
        return instance;
    }
}