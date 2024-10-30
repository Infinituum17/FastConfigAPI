package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public final class StringEditorWrapper extends InputWidgetWrapper<String> {
    private final EditBox editBox;

    public StringEditorWrapper(Font font, int i, int j, int k, int l, Component component, String initValue) {
        this.editBox = new EditBox(font, i, j, k, l, component);

        this.editBox.setValue(initValue);
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
    public String get() {
        return this.editBox.getValue();
    }

    @Override
    public void onClick(double d, double e) {
        this.editBox.onClick(d, e);
    }

    @Override
    public int requiredHeight() {
        int buttonHeight = this.editBox.getHeight();
        int borderSize = 2;

        return borderSize + buttonHeight;
    }
}
