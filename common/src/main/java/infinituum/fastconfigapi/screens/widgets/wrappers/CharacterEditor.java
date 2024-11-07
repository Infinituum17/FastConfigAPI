package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.renderer.widget.GuardedEditBox;
import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class CharacterEditor extends InputWidgetWrapper<Character> {
    private final GuardedEditBox editBox;

    public CharacterEditor(Font font, int i, int j, int k, int l, Component component, Character initValue) {
        this.editBox = new GuardedEditBox(font, i, j, k, l, component, this::isValid);

        this.editBox.setValue(initValue.toString());
    }

    private boolean isValid(String string) {
        return this.editBox.getValue().isEmpty() && string.length() == 1;
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
    public Character get() {
        if (this.editBox.getValue().isEmpty()) {
            return null;
        }

        return this.editBox.getValue().charAt(0);
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
}
