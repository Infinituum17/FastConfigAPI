package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

public final class EnumEditor extends InputWidgetWrapper<Enum<?>> {
    private final CycleButton<Enum<?>> button;

    public EnumEditor(Font font, int i, int j, int k, int l, Component component, Enum<?> initValue) {
        this.button = CycleButton.<Enum<?>>builder(e -> Component.literal(e.name()))
                .displayOnlyValue()
                .withValues(initValue.getClass().getEnumConstants())
                .withInitialValue(initValue)
                .create(i, j, k, l, component);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return this.button.keyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return this.button.keyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.button.charTyped(c, i);
    }

    @Override
    public void setFocused(boolean bl) {
        this.button.setFocused(bl);
    }

    @Override
    public boolean isVisible() {
        return this.button.visible;
    }

    @Override
    public void setVisible(boolean b) {
        this.button.visible = b;
    }

    @Override
    public void setPosition(int i, int j) {
        this.button.setPosition(i, j);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.button.render(guiGraphics, i, j, f);
    }

    @Override
    public Enum<?> get() {
        return this.button.getValue();
    }

    @Override
    public void onClick(double d, double e) {
        this.button.playDownSound(Minecraft.getInstance().getSoundManager());
        this.button.onClick(d, e);
    }

    @Override
    public int getHeight() {
        return this.button.getHeight();
    }

    @Override
    public int getWidth() {
        return this.button.getWidth();
    }

    @Override
    public int getX() {
        return this.button.getX();
    }

    @Override
    public int getY() {
        return this.button.getY();
    }
}
