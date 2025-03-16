package my.client.gui.clickgui.settings;

public class SliderSett extends Setting {
    private float value;
    private final float min;
    private final float max;
    private final float increment;

    public SliderSett(String name, String description, float defaultValue, float min, float max, float increment) {
        super(name, description);
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.value = defaultValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        float steps = Math.round((value - min) / increment);
        this.value = Math.max(min, Math.min(max, min + (steps * increment)));
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getIncrement() {
        return increment;
    }
}