package ravage.client.gui.clickgui.settings;

public class CheckBoxSett extends Setting {
    private boolean value;

    public CheckBoxSett(String name, String description, boolean defaultValue) {
        super(name, description);
        this.value = defaultValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }
}