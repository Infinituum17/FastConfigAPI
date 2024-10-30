package infinituum.fastconfigapi.screens.utils;

import infinituum.fastconfigapi.screens.widgets.wrappers.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class InputWidgetWrapper<T> {
    public static <X> InputWidgetWrapper<X> createWidgetWrapper(X data, Font font, String fieldName) {
        Component name = Component.literal(fieldName);

        int i = 0;
        int j = 0;
        int k = 96;
        int l = 16;

        return (InputWidgetWrapper<X>) switch (data) {
            case Integer value -> new IntegerEditorWrapper(font, i, j, k, l, name, value);
            case Short value -> new ShortEditorWrapper(font, i, j, k, l, name, value);
            case Long value -> new LongEditorWrapper(font, i, j, k, l, name, value);
            case Double value -> new DoubleEditorWrapper(font, i, j, k, l, name, value);
            case Float value -> new FloatEditorWrapper(font, i, j, k, l, name, value);
            case Boolean value -> new BooleanEditorWrapper(font, i, j, k, l, name, value);
            case Byte value -> new ByteEditorWrapper(font, i, j, k, l, name, value);
            case Character value -> new CharacterEditorWrapper(font, i, j, k, l, name, value);
            case String value -> new StringEditorWrapper(font, i, j, k, l, name, value);
            case Enum<?> value -> new EnumEditorWrapper(font, i, j, k, l, name, value);
            case Object[] value -> new ArrayEditorWrapper<>(font, i, j, k, l, name, value);
            case X value -> new ObjectEditorWrapper(font, i, j, k, l, name, value);
        };
    }

    public abstract boolean keyPressed(int i, int j, int k);

    public abstract boolean keyReleased(int i, int j, int k);

    public abstract boolean charTyped(char c, int i);

    public abstract void setFocused(boolean bl);

    public abstract boolean isVisible();

    public abstract void setVisible(boolean b);

    public abstract void setPosition(int i, int j);

    public abstract void render(GuiGraphics guiGraphics, int i, int j, float f);

    public abstract T get();

    public abstract void onClick(double d, double e);

    public abstract int requiredHeight();
}
