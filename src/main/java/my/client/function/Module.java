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

    private boolean registered = false;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = 0;
        this.enabled = false;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}

    public void onRender() {}

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettings(Setting... settings) {
        for (Setting setting : settings) {
            this.settings.add(setting);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getKey() {
        return key;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Setting> getSettings() {
        return new ArrayList<>(settings);
    }

    public void setKey(int key) {
        int oldKey = this.key;
        this.key = key;

        if (registered && ModuleManager.getInstance() != null) {
            ModuleManager.getInstance().updateModuleKey(this, oldKey, key);
        }
    }

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

    public void unregister() {
        if (ModuleManager.getInstance() != null && registered) {
            ModuleManager.getInstance().unregister(this);
            registered = false;
        }
    }

    public boolean isRegistered() {
        return registered;
    }

    public void enable() {
        if (!enabled) {
            toggle();
        }
    }

    public void disable() {
        if (enabled) {
            toggle();
        }
    }
}