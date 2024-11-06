package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

// TODO: Implement
public final class ObjectEditorWrapper extends InputWidgetWrapper<Object> {
    public ObjectEditorWrapper(Font font, int i, int j, int k, int l, Component name, Object initValue) {
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return false;
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return false;
    }

    @Override
    public boolean charTyped(char c, int i) {
        return false;
    }

    @Override
    public void setFocused(boolean bl) {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean b) {

    }

    @Override
    public void setPosition(int i, int j) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {

    }

    @Override
    public Object get() {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public void onClick(double d, double e) {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }
}
