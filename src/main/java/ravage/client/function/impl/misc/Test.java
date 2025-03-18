package ravage.client.function.impl.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import ravage.client.function.Category;
import ravage.client.function.Module;
import ravage.client.gui.clickgui.settings.CheckBoxSett;
import ravage.client.gui.clickgui.settings.ModeSett;
import ravage.client.gui.clickgui.settings.SliderSett;
import org.lwjgl.glfw.GLFW;

public class Test extends Module {

    MatrixStack ms = new MatrixStack();
    private final SliderSett speed;
    private final CheckBoxSett checkbox;
    private final ModeSett mode;

    public Test() {
        super("Test", "Test", Category.MISC);
        this.speed = new SliderSett("Slider", "Speed", 0.2f, 0.1f, 1.0f, 0.1f);
        this.checkbox = new CheckBoxSett("CheckBox", "чёта типа воспроизводить", true);
        this.mode = new ModeSett("ModeSet", "Моды", "Синяя", "Синяя", "Красная", "Зеленая", "Фиолетовая");
        addSettings(speed, checkbox, mode);
    }

    @Override
    public void onEnable() {
        this.toggle();
    }
}