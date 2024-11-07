package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.renderer.widget.GuardedEditBox;
import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class FloatEditor extends InputWidgetWrapper<Float> {
    private final GuardedEditBox editBox;

    public FloatEditor(Font font, int i, int j, int k, int l, Component name, Float initValue) {
        this.editBox = new GuardedEditBox(font, i, j, k, l, name, this::isValid);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private boolean isValid(String string) {
        String value = this.editBox.getValue();

        if (value.isEmpty() && string.equals("-")) {
            return true;
        }

        if (!value.contains(".") && string.equals(".")) {
            return true;
        }

        try {
            Float.parseFloat(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void postInsertion() {
        String str = this.editBox.getValue();

        if (str.equals("-")) {
            return;
        }

        if (str.equals(".")) {
            return;
        }

        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Float.MIN_VALUE : Float.MAX_VALUE));
        }
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
    public Float get() {
        try {
            return Float.parseFloat(this.editBox.getValue());
        } catch (Exception e) {
            return null;
        }
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
