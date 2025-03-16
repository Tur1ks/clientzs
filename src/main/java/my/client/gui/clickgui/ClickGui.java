package my.client.gui.clickgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import my.client.Client;
import my.client.function.Category;
import my.client.function.FuncRegister;
import my.client.function.Module;
import my.client.gui.clickgui.settings.CheckBoxSett;
import my.client.gui.clickgui.settings.ModeSett;
import my.client.gui.clickgui.settings.Setting;
import my.client.gui.clickgui.settings.SliderSett;
import my.client.gui.clickgui.settings.rendersettings.RenderCheckBoxSett;
import my.client.gui.clickgui.settings.rendersettings.RenderModeSett;
import my.client.gui.clickgui.settings.rendersettings.RenderSliderSett;
import my.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final Minecraft mc = Minecraft.getInstance();

    // Основные цвета
    private final int backgroundColor = new Color(20, 20, 20, 240).getRGB();
    private final int accentColor = new Color(0, 140, 255).getRGB();
    private final int secondaryColor = new Color(40, 40, 40).getRGB();
    private final int headerColor = new Color(15, 15, 15).getRGB();
    private final int textColor = new Color(220, 220, 220).getRGB();

    // Размеры панелей
    private final int panelWidth = 120;
    private final int panelHeight = 20;
    private final int moduleHeight = 16;
    private final int settingHeight = 14;

    // Отступы
    private final int padding = 2;

    // Анимация
    private final float animationSpeed = 0.2f;

    // Рендереры настроек
    private final RenderCheckBoxSett checkBoxRenderer = new RenderCheckBoxSett();
    private final RenderSliderSett sliderRenderer = new RenderSliderSett();
    private final RenderModeSett modeRenderer = new RenderModeSett();

    // Список панелей категорий
    private final List<CategoryPanel> panels = new ArrayList<>();

    // Перетаскивание панелей
    private CategoryPanel draggingPanel = null;
    private int dragX, dragY;

    public ClickGui() {
        super(new StringTextComponent("ClickGui"));

        int startX = 50;  // Fixed starting position from left
        int startY = 20;  // Fixed top position
        int spacing = 10;

        for (Category category : Category.values()) {
            CategoryPanel panel = new CategoryPanel(category, startX, startY);
            panel.extended = true;
            panels.add(panel);
            startX += panelWidth + spacing;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.fillGradient(matrixStack, 0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 100).getRGB());

        // Обновление и отрисовка каждой панели
        for (CategoryPanel panel : panels) {
            panel.update(mouseX, mouseY, partialTicks);
            panel.draw(matrixStack, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked((int)mouseX, (int)mouseY, mouseButton)) {
                // Чтобы панель была поверх других после клика
                panels.remove(panel);
                panels.add(panel);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (CategoryPanel panel : panels) {
            panel.mouseReleased((int)mouseX, (int)mouseY, mouseButton);
        }
        draggingPanel = null;
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        if (draggingPanel != null) {
            draggingPanel.setX((int)mouseX - dragX);
            draggingPanel.setY((int)mouseY - dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.displayGuiScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Внутренний класс для панели категории
    class CategoryPanel {
        private final Category category;
        private int x, y;
        private boolean extended;
        private float openAnimation;
        private ModuleButton hoveringModule = null;

        private final List<ModuleButton> moduleButtons = new ArrayList<>();

        public CategoryPanel(Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.extended = false;
            this.openAnimation = 0f;

            // Получаем все модули для этой категории и создаем кнопки
            FuncRegister funcRegister = Client.getInstance().getFuncRegister();
            List<Module> modules = funcRegister.getModuleManager().getModulesByCategory(category);

            int yOffset = panelHeight;
            for (Module module : modules) {
                moduleButtons.add(new ModuleButton(module, yOffset));
                yOffset += moduleHeight;
            }
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void update(int mouseX, int mouseY, float partialTicks) {
            // Анимация открытия
            if (extended && openAnimation < 1) {
                openAnimation += animationSpeed;
                if (openAnimation > 1) openAnimation = 1;
            } else if (!extended && openAnimation > 0) {
                openAnimation -= animationSpeed;
                if (openAnimation < 0) openAnimation = 0;
            }

            // Проверка наведения мышью на модули
            hoveringModule = null;
            if (openAnimation > 0) {
                for (ModuleButton button : moduleButtons) {
                    button.update(mouseX, mouseY);
                    if (button.isHovering()) {
                        hoveringModule = button;
                    }
                }
            }
        }

        public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
            // Рисуем заголовок панели
            RenderUtil.drawRect(matrixStack, x, y, x + panelWidth, y + panelHeight+5, headerColor);
            RenderUtil.drawLine(matrixStack, x, y + panelHeight - 1, x + panelWidth, y + panelHeight - 1, 1f,
                    extended ? accentColor : secondaryColor);

            // Рисуем название категории
            RenderUtil.drawTextWithShadow(matrixStack, category.getName(), x + 5, y + panelHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            // Рисуем модули, если панель раскрыта
            if (openAnimation > 0) {
                int totalHeight = (int)(getTotalExpandedHeight() * openAnimation);
                RenderUtil.drawRect(matrixStack, x, y + panelHeight, x + panelWidth, y + panelHeight + totalHeight, backgroundColor);

                // Ограничиваем область рисования для модулей
                RenderSystem.pushMatrix();
                RenderSystem.enableScissor(x, y + panelHeight, panelWidth, totalHeight);

                for (ModuleButton button : moduleButtons) {
                    button.draw(matrixStack, mouseX, mouseY);
                }

                RenderSystem.disableScissor();
                RenderSystem.popMatrix();
            }
        }

        private int getTotalExpandedHeight() {
            int height = 0;
            for (ModuleButton button : moduleButtons) {
                height += moduleHeight;
                if (button.isExpanded()) {
                    height += button.getExpandedHeight();
                }
            }
            return height;
        }

        public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            // Remove header click handling for extending/collapsing
            if (mouseX >= x && mouseX <= x + panelWidth && mouseY >= y && mouseY <= y + panelHeight) {

            }

            // Check clicks on modules
            for (ModuleButton button : moduleButtons) {
                if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                    return true;
                }
            }

            return false;
        }

        public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
            if (extended) {
                for (ModuleButton button : moduleButtons) {
                    button.mouseReleased(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    // Внутренний класс для кнопки модуля
    class ModuleButton {
        private final Module module;
        private final int yOffset;
        private boolean expanded;
        private float expandAnimation;
        private boolean hovering;

        private final List<SettingButton> settingButtons = new ArrayList<>();
        private int x, y;


        public ModuleButton(Module module, int yOffset) {
            this.module = module;
            this.yOffset = yOffset;
            this.expanded = false;
            this.expandAnimation = 0f;
            this.x = 0; // Будет обновляться при отрисовке
            this.y = 0; // Будет обновляться при отрисовке

            // Получаем все настройки для этого модуля и создаем кнопки
            List<Setting> settings = module.getSettings();

            int settYOffset = 0;
            for (Setting setting : settings) {
                SettingButton settButton = null;

                if (setting instanceof CheckBoxSett) {
                    settButton = new CheckBoxButton((CheckBoxSett) setting, settYOffset);
                } else if (setting instanceof SliderSett) {
                    settButton = new SliderButton((SliderSett) setting, settYOffset);
                } else if (setting instanceof ModeSett) {
                    settButton = new ModeButton((ModeSett) setting, settYOffset);
                }

                if (settButton != null) {
                    settingButtons.add(settButton);
                    settYOffset += settingHeight;
                }
            }
        }

        public void update(int mouseX, int mouseY) {
            // Анимация открытия настроек
            if (expanded && expandAnimation < 1) {
                expandAnimation += animationSpeed;
                if (expandAnimation > 1) expandAnimation = 1;
            } else if (!expanded && expandAnimation > 0) {
                expandAnimation -= animationSpeed;
                if (expandAnimation < 0) expandAnimation = 0;
            }

            // Проверка наведения мышью
            hovering = mouseX >= x && mouseX <= x + panelWidth &&
                    mouseY >= y + yOffset && mouseY <= y + yOffset + moduleHeight;

            // Обновляем кнопки настроек
            if (expandAnimation > 0) {
                for (SettingButton button : settingButtons) {
                    button.update(mouseX, mouseY);
                }
            }
        }

        public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
            int moduleY = y + yOffset;

            // Фон кнопки модуля
            int bgColor = hovering ? secondaryColor : backgroundColor;
            if (module.isEnabled()) {
                RenderUtil.drawGradientRect(matrixStack, x, moduleY, x + panelWidth, moduleY + moduleHeight,
                        new Color(accentColor).darker().getRGB(), bgColor);
            } else {
                RenderUtil.drawRect(matrixStack, x, moduleY, x + panelWidth, moduleY + moduleHeight, bgColor);
            }

            // Название модуля
            RenderUtil.drawTextWithShadow(matrixStack, module.getName(), x + 5, moduleY + moduleHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2,
                    module.isEnabled() ? accentColor : textColor);

            // Индикатор наличия настроек
            if (!settingButtons.isEmpty()) {
                String settingsIcon = expanded ? "-" : "+";
                RenderUtil.drawTextWithShadow(matrixStack, settingsIcon, x + panelWidth - 10,
                        moduleY + moduleHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);
            }

            // Рисуем настройки, если модуль раскрыт
            if (expandAnimation > 0 && !settingButtons.isEmpty()) {
                int totalSettingsHeight = (int)(getExpandedHeight() * expandAnimation);
                int settingY = moduleY + moduleHeight;

                RenderUtil.drawRect(matrixStack, x + padding, settingY, x + panelWidth - padding, settingY + totalSettingsHeight, secondaryColor);

                // Ограничиваем область рисования для настроек
                RenderSystem.pushMatrix();
                RenderSystem.enableScissor(x + padding, settingY, panelWidth - padding * 2, totalSettingsHeight);

                for (SettingButton button : settingButtons) {
                    button.draw(matrixStack, mouseX, mouseY, settingY);
                }

                RenderSystem.disableScissor();
                RenderSystem.popMatrix();
            }
        }

        public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            int moduleY = y + yOffset;

            // Проверка клика по модулю
            if (mouseX >= x && mouseX <= x + panelWidth && mouseY >= moduleY && mouseY <= moduleY + moduleHeight) {
                if (mouseButton == 0) { // ЛКМ - включение/выключение модуля
                    module.toggle();
                    return true;
                } else if (mouseButton == 1 && !settingButtons.isEmpty()) { // ПКМ - раскрытие/скрытие настроек
                    expanded = !expanded;
                    return true;
                }
            }

            // Проверка клика по настройкам
            if (expanded && expandAnimation > 0.9f) {
                int settingY = moduleY + moduleHeight;
                for (SettingButton button : settingButtons) {
                    if (button.mouseClicked(mouseX, mouseY, settingY, mouseButton)) {
                        return true;
                    }
                    settingY += settingHeight;
                }
            }

            return false;
        }

        public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
            if (expanded) {
                int settingY = y + yOffset + moduleHeight;
                for (SettingButton button : settingButtons) {
                    button.mouseReleased(mouseX, mouseY, settingY, mouseButton);
                    settingY += settingHeight;
                }
            }
        }

        public boolean isExpanded() {
            return expanded && expandAnimation > 0;
        }

        public int getExpandedHeight() {
            return settingButtons.size() * settingHeight;
        }

        public boolean isHovering() {
            return hovering;
        }
    }

    // Абстрактный класс для кнопок настроек
    abstract class SettingButton {
        protected final Setting setting;
        protected final int yOffset;
        protected boolean hovering;
        protected int x, y;

        public SettingButton(Setting setting, int yOffset) {
            this.setting = setting;
            this.yOffset = yOffset;
            this.x = 0;
            this.y = 0;
        }

        public void update(int mouseX, int mouseY) {
            // Обновление состояния наведения
            hovering = isHovering(mouseX, mouseY, 0);
        }

        public abstract void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY);

        public abstract boolean mouseClicked(int mouseX, int mouseY, int parentY, int mouseButton);

        public void mouseReleased(int mouseX, int mouseY, int parentY, int mouseButton) {}

        protected boolean isHovering(int mouseX, int mouseY, int parentY) {
            int realY = parentY + yOffset;
            return mouseX >= x + padding * 2 && mouseX <= x + panelWidth - padding * 2 &&
                    mouseY >= realY && mouseY <= realY + settingHeight;
        }
    }

    // Кнопка настройки-чекбокса
    class CheckBoxButton extends SettingButton {
        private final CheckBoxSett checkBoxSett;

        public CheckBoxButton(CheckBoxSett setting, int yOffset) {
            super(setting, yOffset);
            this.checkBoxSett = setting;
        }

        @Override
        public void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY) {
            int settY = parentY + yOffset;

            // Название настройки
            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + settingHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            // Рисуем чекбокс
            checkBoxRenderer.render(matrixStack, x + panelWidth - padding * 6, settY + settingHeight / 2 - 4, 8, 8,
                    checkBoxSett.getValue(), hovering, accentColor, backgroundColor, secondaryColor);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int parentY, int mouseButton) {
            if (isHovering(mouseX, mouseY, parentY) && mouseButton == 0) {
                checkBoxSett.setValue(!checkBoxSett.getValue());
                return true;
            }
            return false;
        }
    }

    // Кнопка настройки-слайдера
    class SliderButton extends SettingButton {
        private final SliderSett sliderSett;
        private boolean dragging;

        public SliderButton(SliderSett setting, int yOffset) {
            super(setting, yOffset);
            this.sliderSett = setting;
        }

        @Override
        public void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY) {
            int settY = parentY + yOffset;

            // Название настройки и текущее значение
            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + 2, textColor);

            String valueStr = String.format("%.2f", sliderSett.getValue());
            RenderUtil.drawTextWithShadow(matrixStack, valueStr, x + panelWidth - padding * 3 - mc.fontRenderer.getStringWidth(valueStr),
                    settY + 2, textColor);

            // Рисуем слайдер
            int sliderX = x + padding * 3;
            int sliderY = settY + settingHeight - 4;
            int sliderWidth = panelWidth - padding * 6;

            // Обновляем значение слайдера при перетаскивании
            if (dragging) {
                float percentage = (float)(mouseX - sliderX) / sliderWidth;
                percentage = Math.max(0, Math.min(1, percentage));
                float range = sliderSett.getMax() - sliderSett.getMin();
                float newValue = sliderSett.getMin() + range * percentage;
                sliderSett.setValue(newValue);
            }

            // Рисуем слайдер
            sliderRenderer.render(matrixStack, sliderX, sliderY, sliderWidth, 2,
                    sliderSett.getValue(), sliderSett.getMin(), sliderSett.getMax(),
                    hovering || dragging, accentColor, backgroundColor, secondaryColor);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int parentY, int mouseButton) {
            if (isHovering(mouseX, mouseY, parentY) && mouseButton == 0) {
                dragging = true;
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY, int parentY, int mouseButton) {
            dragging = false;
        }
    }

    // Кнопка настройки-режима
    class ModeButton extends SettingButton {
        private final ModeSett modeSett;

        public ModeButton(ModeSett setting, int yOffset) {
            super(setting, yOffset);
            this.modeSett = setting;
        }

        @Override
        public void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY) {
            int settY = parentY + yOffset;

            // Название настройки и текущее значение
            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + 2, textColor);

            String currentMode = modeSett.getMode();
            RenderUtil.drawTextWithShadow(matrixStack, currentMode, x + panelWidth - padding * 3 - mc.fontRenderer.getStringWidth(currentMode),
                    settY + settingHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            // Рисуем индикаторы переключения
            modeRenderer.render(matrixStack, x + padding * 3, settY + settingHeight - 4, panelWidth - padding * 6, 2,
                    modeSett.getModes(), modeSett.getMode(), hovering, accentColor, backgroundColor, secondaryColor);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int parentY, int mouseButton) {
            if (isHovering(mouseX, mouseY, parentY)) {
                if (mouseButton == 0) { // ЛКМ - следующий режим
                    modeSett.cycle(true);
                    return true;
                } else if (mouseButton == 1) { // ПКМ - предыдущий режим
                    modeSett.cycle(false);
                    return true;
                }
            }
            return false;
        }
    }
}