package infinituum.fastconfigapi.screens.widgets.type;

import infinituum.fastconfigapi.screens.utils.renderer.type.GuardingFunction;
import infinituum.fastconfigapi.screens.utils.renderer.widget.GuardedEditBox;
import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public abstract class GuardedEditBoxEditor<T> extends InputWidgetWrapper<T> {
    protected final GuardedEditBox editBox;
    private final Function<String, T> getter;

    public GuardedEditBoxEditor(Font font, int i, int j, int k, int l, Component name, GuardingFunction guardingFunction, Function<String, T> getter) {
        this.editBox = new GuardedEditBox(font, i, j, k, l, name, guardingFunction);
        this.getter = getter;
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return this.editBox.keyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return this.editBox.keyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.editBox.charTyped(c, i);
    }

    @Override
    public void setFocused(boolean bl) {
        this.editBox.setFocused(bl);
    }

    @Override
    public boolean isVisible() {
        return this.editBox.isVisible();
    }

    @Override
    public void setVisible(boolean b) {
        this.editBox.setVisible(b);
    }

    @Override
    public void setPosition(int i, int j) {
        this.editBox.setPosition(i, j);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.editBox.render(guiGraphics, i, j, f);
    }

    @Override
    public T get() {
        return this.getter.apply(this.editBox.getValue());
    }

    @Override
    public void onClick(double d, double e) {
        this.editBox.onClick(d, e);
    }

    @Override
    public int getHeight() {
        return this.editBox.getHeight();
    }

    @Override
    public int getWidth() {
        return this.editBox.getWidth();
    }

    @Override
    public int getX() {
        return this.editBox.getX();
    }

    @Override
    public int getY() {
        return this.editBox.getY();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
        this.editBox.setWidth(computeWidth(listWidth));
    }

    @Override
    public void setValue(T value) {
        if (value == null) {
            this.editBox.setValue("");
        } else {
            this.editBox.setValue(String.valueOf(value));
        }
    }
}
