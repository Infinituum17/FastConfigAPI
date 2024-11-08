package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.widgets.type.Resizable;
import infinituum.fastconfigapi.screens.widgets.wrappers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

public abstract class InputWidgetWrapper<T> implements Resizable {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 16;

    public static <X> InputWidgetWrapper<X> createWidgetWrapper(X data, Font font, String fieldName, int parentWidth) {
        Component name = Component.literal(fieldName);

        int i = 0;
        int j = 0;

        InputWidgetWrapper<?> widgetWrapper = switch (data) {
            case Integer value -> new IntegerEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Short value -> new ShortEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Long value -> new LongEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Double value -> new DoubleEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Float value -> new FloatEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Boolean value -> new BooleanEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Byte value -> new ByteEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Character value -> new CharacterEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case String value -> new StringEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case Enum<?> value -> new EnumEditor(font, i, j, WIDTH, HEIGHT, name, value);
            case X value -> getExactType(font, i, j, WIDTH, HEIGHT, name, value, parentWidth);
        };

        return (InputWidgetWrapper<X>) widgetWrapper;
    }

    private static <X> InputWidgetWrapper<?> getExactType(Font font, int i, int j, int k, int l, Component name, X value, int width) {
        if (value instanceof int[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof short[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof long[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof double[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof float[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof boolean[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof byte[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof char[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, ArrayUtils.toObject(array), width);
        }

        if (value instanceof Object[] array) {
            return new ArrayEditor<>(font, i, j, k, l, name, array, width);
        }

        return new ObjectEditor(font, i, j, k, l, name, value, width);
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

    protected int computeWidth(int listWidth) {
        return listWidth / 5;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
    }
}
