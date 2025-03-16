package my.client.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {
    private static ModuleManager instance;

    private final List<Module> modules = new ArrayList<>();

    private final Map<String, Module> modulesByName = new HashMap<>();
    private final Map<Category, List<Module>> modulesByCategory = new HashMap<>();
    private final Map<Integer, List<Module>> modulesByKey = new HashMap<>();

    public ModuleManager() {
        instance = this;

        for (Category category : Category.values()) {
            modulesByCategory.put(category, new ArrayList<>());
        }
    }

    public boolean register(Module module) {
        if (modulesByName.containsKey(module.getName().toLowerCase())) {
            return false;
        }

        modules.add(module);

        modulesByName.put(module.getName().toLowerCase(), module);
        modulesByCategory.get(module.getCategory()).add(module);

        if (module.getKey() != 0) {
            modulesByKey.computeIfAbsent(module.getKey(), k -> new ArrayList<>()).add(module);
        }

        return true;
    }

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

    public void updateModuleKey(Module module, int oldKey, int newKey) {
        if (oldKey != 0 && modulesByKey.containsKey(oldKey)) {
            modulesByKey.get(oldKey).remove(module);
            if (modulesByKey.get(oldKey).isEmpty()) {
                modulesByKey.remove(oldKey);
            }
        }

        if (newKey != 0) {
            modulesByKey.computeIfAbsent(newKey, k -> new ArrayList<>()).add(module);
        }
    }

    public void registerAll(Module... modules) {
        for (Module module : modules) {
            register(module);
        }
    }

    public Module getModuleByName(String name) {
        return modulesByName.get(name.toLowerCase());
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public List<Module> getModulesByCategory(Category category) {
        return new ArrayList<>(modulesByCategory.get(category));
    }

    public List<Module> getModulesByKey(int key) {
        return modulesByKey.containsKey(key) ? new ArrayList<>(modulesByKey.get(key)) : new ArrayList<>();
    }

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

    public void onUpdate() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onUpdate();
            }
        }
    }

    public void onRender() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onRender();
            }
        }
    }

    public boolean enableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null && !module.isEnabled()) {
            module.toggle();
            return true;
        }
        return false;
    }

    public boolean disableModule(String name) {
        Module module = getModuleByName(name);
        if (module != null && module.isEnabled()) {
            module.toggle();
            return true;
        }
        return false;
    }

    public int getModuleCount() {
        return modules.size();
    }

    public int getEnabledModuleCount() {
        int count = 0;
        for (Module module : modules) {
            if (module.isEnabled()) {
                count++;
            }
        }
        return count;
    }

    public static ModuleManager getInstance() {
        return instance;
    }
}