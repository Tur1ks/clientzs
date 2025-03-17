package ravage.client.function.impl.client;

import ravage.client.function.Category;
import ravage.client.function.Module;
import ravage.client.gui.clickgui.ClickGui;
import ravage.client.gui.clickgui.settings.CheckBoxSett;
import ravage.client.gui.clickgui.settings.ModeSett;
import ravage.client.gui.clickgui.settings.SliderSett;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class ClickGuiModule extends Module {

    private final SliderSett animationSpeed;
    private final CheckBoxSett sound;
    private final ModeSett colorTheme;

    public ClickGuiModule() {
        super("ClickGui", "Графический интерфейс для управления модулями", Category.CLIENT);
        this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
        this.animationSpeed = new SliderSett("Скорость анимации", "Скорость анимации открытия/закрытия панелей", 0.2f, 0.1f, 1.0f, 0.1f);
        this.sound = new CheckBoxSett("Звуки", "Воспроизводить звуки при взаимодействии", true);
        this.colorTheme = new ModeSett("Тема", "Цветовая тема интерфейса", "Синяя", "Синяя", "Красная", "Зеленая", "Фиолетовая");

        addSettings(animationSpeed, sound, colorTheme);
    }

    @Override
    public void onEnable() {
        Minecraft.getInstance().displayGuiScreen(new ClickGui());

        this.toggle();
    }

    @Override
    public void onDisable() {
        if (Minecraft.getInstance().currentScreen instanceof ClickGui) {
            Minecraft.getInstance().displayGuiScreen(null);
        }
    }
}