package my.client.function.impl.client;

import my.client.Client;
import my.client.function.Category;
import my.client.function.Module;
import my.client.gui.clickgui.ClickGui;
import my.client.gui.clickgui.settings.CheckBoxSett;
import my.client.gui.clickgui.settings.ModeSett;
import my.client.gui.clickgui.settings.SliderSett;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class ClickGuiModule extends Module {

    private final SliderSett animationSpeed;
    private final CheckBoxSett sound;
    private final ModeSett colorTheme;

    public ClickGuiModule() {
        super("ClickGui", "Графический интерфейс для управления модулями", Category.CLIENT);

        // Устанавливаем клавишу по умолчанию (правый Shift)
        this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);

        // Добавляем настройки
        this.animationSpeed = new SliderSett("Скорость анимации", "Скорость анимации открытия/закрытия панелей", 0.2f, 0.1f, 1.0f, 0.1f);
        this.sound = new CheckBoxSett("Звуки", "Воспроизводить звуки при взаимодействии", true);
        this.colorTheme = new ModeSett("Тема", "Цветовая тема интерфейса", "Синяя", "Синяя", "Красная", "Зеленая", "Фиолетовая");

        addSettings(animationSpeed, sound, colorTheme);
    }

    @Override
    public void onEnable() {
        // Открываем GUI при включении модуля
        Minecraft.getInstance().displayGuiScreen(new ClickGui());

        // Сразу отключаем модуль, чтобы он не оставался активным
        this.toggle();
    }

    @Override
    public void onDisable() {
        // Если GUI открыт, закрываем его
        if (Minecraft.getInstance().currentScreen instanceof ClickGui) {
            Minecraft.getInstance().displayGuiScreen(null);
        }
    }
}