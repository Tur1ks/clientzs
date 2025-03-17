package ravage.client.gui.clickgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import ravage.client.Ravage;
import ravage.client.function.Category;
import ravage.client.function.FuncRegister;
import ravage.client.function.Module;
import ravage.client.gui.clickgui.settings.CheckBoxSett;
import ravage.client.gui.clickgui.settings.ModeSett;
import ravage.client.gui.clickgui.settings.Setting;
import ravage.client.gui.clickgui.settings.SliderSett;
import ravage.client.gui.clickgui.settings.rendersettings.RenderCheckBoxSett;
import ravage.client.gui.clickgui.settings.rendersettings.RenderModeSett;
import ravage.client.gui.clickgui.settings.rendersettings.RenderSliderSett;
import ravage.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final Minecraft mc = Minecraft.getInstance();

    private final int backgroundColor = new Color(20, 20, 20, 240).getRGB();
    private final int accentColor = new Color(0, 140, 255).getRGB();
    private final int secondaryColor = new Color(40, 40, 40).getRGB();
    private final int headerColor = new Color(15, 15, 15).getRGB();
    private final int textColor = new Color(220, 220, 220).getRGB();

    private final int panelWidth = 120;
    private final int panelHeight = 20;
    private final int moduleHeight = 16;
    private final int settingHeight = 14;
    private final int padding = 2;
    private final float animationSpeed = 0.2f;

    private final RenderCheckBoxSett checkBoxRenderer = new RenderCheckBoxSett();
    private final RenderSliderSett sliderRenderer = new RenderSliderSett();
    private final RenderModeSett modeRenderer = new RenderModeSett();

    private final List<CategoryPanel> panels = new ArrayList<>();

    private CategoryPanel draggingPanel = null;
    private int dragX, dragY;

    public ClickGui() {
        super(new StringTextComponent("ClickGui"));

        int startX = 20;
        for (Category category : Category.values()) {
            panels.add(new CategoryPanel(category, startX, 20));
            startX += panelWidth + 10;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.fillGradient(matrixStack, 0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 100).getRGB());

        for (CategoryPanel panel : panels) {
            panel.update(mouseX, mouseY, partialTicks);
            panel.draw(matrixStack, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked((int)mouseX, (int)mouseY, mouseButton)) {
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

            FuncRegister funcRegister = Ravage.getInstance().getFuncRegister();
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
            if (extended && openAnimation < 1) {
                openAnimation += animationSpeed;
                if (openAnimation > 1) openAnimation = 1;
            } else if (!extended && openAnimation > 0) {
                openAnimation -= animationSpeed;
                if (openAnimation < 0) openAnimation = 0;
            }

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
            RenderUtil.drawRect(matrixStack, x, y, x + panelWidth, y + panelHeight+5, headerColor);
            RenderUtil.drawLine(matrixStack, x, y + panelHeight - 1, x + panelWidth, y + panelHeight - 1, 1f,
                    extended ? accentColor : secondaryColor);

            RenderUtil.drawTextWithShadow(matrixStack, category.getName(), x + 5, y + panelHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            String expandIcon = extended ? "-" : "+";
            RenderUtil.drawTextWithShadow(matrixStack, expandIcon, x + panelWidth - 10, y + panelHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            if (openAnimation > 0) {
                int totalHeight = (int)(getTotalExpandedHeight() * openAnimation);
                RenderUtil.drawRect(matrixStack, x, y + panelHeight, x + panelWidth, y + panelHeight + totalHeight, backgroundColor);

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
            if (mouseX >= x && mouseX <= x + panelWidth && mouseY >= y && mouseY <= y + panelHeight) {
                if (mouseButton == 0) {
                    extended = !extended;
                    return true;
                } else if (mouseButton == 1) {
                    draggingPanel = this;
                    dragX = mouseX - x;
                    dragY = mouseY - y;
                    return true;
                }
            }

            if (extended && openAnimation > 0.9f) {
                for (ModuleButton button : moduleButtons) {
                    if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                        return true;
                    }
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
            this.x = 0;
            this.y = 0;

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
            if (expanded && expandAnimation < 1) {
                expandAnimation += animationSpeed;
                if (expandAnimation > 1) expandAnimation = 1;
            } else if (!expanded && expandAnimation > 0) {
                expandAnimation -= animationSpeed;
                if (expandAnimation < 0) expandAnimation = 0;
            }

            hovering = mouseX >= x && mouseX <= x + panelWidth &&
                    mouseY >= y + yOffset && mouseY <= y + yOffset + moduleHeight;

            if (expandAnimation > 0) {
                for (SettingButton button : settingButtons) {
                    button.update(mouseX, mouseY);
                }
            }
        }

        public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
            int moduleY = y + yOffset;

            int bgColor = hovering ? secondaryColor : backgroundColor;
            if (module.isEnabled()) {
                RenderUtil.drawGradientRect(matrixStack, x, moduleY, x + panelWidth, moduleY + moduleHeight,
                        new Color(accentColor).darker().getRGB(), bgColor);
            } else {
                RenderUtil.drawRect(matrixStack, x, moduleY, x + panelWidth, moduleY + moduleHeight, bgColor);
            }

            RenderUtil.drawTextWithShadow(matrixStack, module.getName(), x + 5, moduleY + moduleHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2,
                    module.isEnabled() ? accentColor : textColor);

            if (!settingButtons.isEmpty()) {
                String settingsIcon = expanded ? "-" : "+";
                RenderUtil.drawTextWithShadow(matrixStack, settingsIcon, x + panelWidth - 10,
                        moduleY + moduleHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);
            }

            if (expandAnimation > 0 && !settingButtons.isEmpty()) {
                int totalSettingsHeight = (int)(getExpandedHeight() * expandAnimation);
                int settingY = moduleY + moduleHeight;

                RenderUtil.drawRect(matrixStack, x + padding, settingY, x + panelWidth - padding, settingY + totalSettingsHeight, secondaryColor);

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

            if (mouseX >= x && mouseX <= x + panelWidth && mouseY >= moduleY && mouseY <= moduleY + moduleHeight) {
                if (mouseButton == 0) {
                    module.toggle();
                    return true;
                } else if (mouseButton == 1 && !settingButtons.isEmpty()) {
                    expanded = !expanded;
                    return true;
                }
            }

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

    class CheckBoxButton extends SettingButton {
        private final CheckBoxSett checkBoxSett;

        public CheckBoxButton(CheckBoxSett setting, int yOffset) {
            super(setting, yOffset);
            this.checkBoxSett = setting;
        }

        @Override
        public void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY) {
            int settY = parentY + yOffset;

            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + settingHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

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

            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + 2, textColor);

            String valueStr = String.format("%.2f", sliderSett.getValue());
            RenderUtil.drawTextWithShadow(matrixStack, valueStr, x + panelWidth - padding * 3 - mc.fontRenderer.getStringWidth(valueStr),
                    settY + 2, textColor);

            int sliderX = x + padding * 3;
            int sliderY = settY + settingHeight - 4;
            int sliderWidth = panelWidth - padding * 6;

            if (dragging) {
                float percentage = (float)(mouseX - sliderX) / sliderWidth;
                percentage = Math.max(0, Math.min(1, percentage));
                float range = sliderSett.getMax() - sliderSett.getMin();
                float newValue = sliderSett.getMin() + range * percentage;
                sliderSett.setValue(newValue);
            }

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

    class ModeButton extends SettingButton {
        private final ModeSett modeSett;

        public ModeButton(ModeSett setting, int yOffset) {
            super(setting, yOffset);
            this.modeSett = setting;
        }

        @Override
        public void draw(MatrixStack matrixStack, int mouseX, int mouseY, int parentY) {
            int settY = parentY + yOffset;

            RenderUtil.drawTextWithShadow(matrixStack, setting.getName(), x + padding * 3,
                    settY + 2, textColor);

            String currentMode = modeSett.getMode();
            RenderUtil.drawTextWithShadow(matrixStack, currentMode, x + panelWidth - padding * 3 - mc.fontRenderer.getStringWidth(currentMode),
                    settY + settingHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, textColor);

            modeRenderer.render(matrixStack, x + padding * 3, settY + settingHeight - 4, panelWidth - padding * 6, 2,
                    modeSett.getModes(), modeSett.getMode(), hovering, accentColor, backgroundColor, secondaryColor);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int parentY, int mouseButton) {
            if (isHovering(mouseX, mouseY, parentY)) {
                if (mouseButton == 0) {
                    modeSett.cycle(true);
                    return true;
                } else if (mouseButton == 1) {
                    modeSett.cycle(false);
                    return true;
                }
            }
            return false;
        }
    }
}