package infinituum.fastconfigapi.screens.utils;

import infinituum.fastconfigapi.screens.widgets.wrappers.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

public abstract class InputWidgetWrapper<T> {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 16;

    public static <X> InputWidgetWrapper<X> createWidgetWrapper(X data, Font font, String fieldName) {
        Component name = Component.literal(fieldName);

        int i = 0;
        int j = 0;

        InputWidgetWrapper<?> widgetWrapper = switch (data) {
            case Integer value -> new IntegerEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Short value -> new ShortEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Long value -> new LongEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Double value -> new DoubleEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Float value -> new FloatEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Boolean value -> new BooleanEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Byte value -> new ByteEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Character value -> new CharacterEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case String value -> new StringEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case Enum<?> value -> new EnumEditorWrapper(font, i, j, WIDTH, HEIGHT, name, value);
            case X value -> getExactType(font, i, j, WIDTH, HEIGHT, name, value);
        };

        return (InputWidgetWrapper<X>) widgetWrapper;
    }

    private static <X> InputWidgetWrapper<?> getExactType(Font font, int i, int j, int k, int l, Component name, X value) {
        if (value instanceof int[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof short[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof long[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof double[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof float[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof boolean[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof byte[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof char[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof Object[] array) {
            return new ArrayEditorWrapper<>(font, i, j, k, l, name, array);
        }

        return new ObjectEditorWrapper(font, i, j, k, l, name, value);
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

    public int getTotalHeight() {
        return getHeight();
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract int getX();

    public abstract int getY();
}
