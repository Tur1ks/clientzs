package my.client.gui.clickgui.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeSett extends Setting {
    private final List<String> modes;
    private int currentIndex;

    public ModeSett(String name, String description, String defaultMode, String... modes) {
        super(name, description);
        this.modes = new ArrayList<>(Arrays.asList(modes));
        this.currentIndex = this.modes.indexOf(defaultMode);
        if (this.currentIndex == -1 && !this.modes.isEmpty()) {
            this.currentIndex = 0;
        }
    }

    public String getMode() {
        if (currentIndex >= 0 && currentIndex < modes.size()) {
            return modes.get(currentIndex);
        }
        return "";
    }

    public void setMode(String mode) {
        int index = modes.indexOf(mode);
        if (index != -1) {
            this.currentIndex = index;
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public void cycle(boolean forward) {
        if (modes.isEmpty()) return;

        if (forward) {
            currentIndex = (currentIndex + 1) % modes.size();
        } else {
            currentIndex = (currentIndex - 1 + modes.size()) % modes.size();
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}