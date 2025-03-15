package my.client.function;

import java.util.ArrayList;
import java.util.List;
import my.client.gui.clickgui.settings.Setting;

public class Module {
    private final String name;
    private final String description;
    private int key;
    private final Category category;
    private boolean enabled;
    private final List<Setting> settings = new ArrayList<>();

    // Флаг, указывающий, был ли модуль зарегистрирован
    private boolean registered = false;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = 0;
        this.enabled = false;
    }

    /**
     * Переключает состояние модуля
     */
    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /**
     * Вызывается при включении модуля
     */
    public void onEnable() {}

    /**
     * Вызывается при выключении модуля
     */
    public void onDisable() {}

    /**
     * Вызывается каждый тик для включенных модулей
     */
    public void onUpdate() {}

    /**
     * Вызывается при рендеринге для включенных модулей
     */
    public void onRender() {}

    /**
     * Добавляет настройку к модулю
     * @param setting Настройка для добавления
     */
    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    /**
     * Добавляет несколько настроек к модулю
     * @param settings Настройки для добавления
     */
    public void addSettings(Setting... settings) {
        for (Setting setting : settings) {
            this.settings.add(setting);
        }
    }

    /**
     * Получает имя модуля
     * @return Имя модуля
     */
    public String getName() {
        return name;
    }

    /**
     * Получает описание модуля
     * @return Описание модуля
     */
    public String getDescription() {
        return description;
    }

    /**
     * Получает код клавиши для активации модуля
     * @return Код клавиши
     */
    public int getKey() {
        return key;
    }

    /**
     * Получает категорию модуля
     * @return Категория модуля
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Проверяет, включен ли модуль
     * @return true если модуль включен
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Получает список настроек модуля
     * @return Список настроек
     */
    public List<Setting> getSettings() {
        return new ArrayList<>(settings);
    }

    /**
     * Устанавливает клавишу для активации модуля
     * @param key Код клавиши
     */
    public void setKey(int key) {
        int oldKey = this.key;
        this.key = key;

        // Если модуль зарегистрирован, обновляем ключ в ModuleManager
        if (registered && ModuleManager.getInstance() != null) {
            ModuleManager.getInstance().updateModuleKey(this, oldKey, key);
        }
    }

    /**
     * Регистрирует модуль в ModuleManager
     * @return true если модуль успешно зарегистрирован
     */
    public boolean register() {
        if (ModuleManager.getInstance() != null && !registered) {
            boolean success = ModuleManager.getInstance().register(this);
            if (success) {
                registered = true;
            }
            return success;
        }
        return false;
    }

    /**
     * Отменяет регистрацию модуля в ModuleManager
     */
    public void unregister() {
        if (ModuleManager.getInstance() != null && registered) {
            ModuleManager.getInstance().unregister(this);
            registered = false;
        }
    }

    /**
     * Проверяет, зарегистрирован ли модуль
     * @return true если модуль зарегистрирован
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Включает модуль, если он выключен
     */
    public void enable() {
        if (!enabled) {
            toggle();
        }
    }

    /**
     * Выключает модуль, если он включен
     */
    public void disable() {
        if (enabled) {
            toggle();
        }
    }
}