package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.widgets.type.Resizable;
import infinituum.fastconfigapi.screens.widgets.wrappers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public abstract class InputWidgetWrapper<T> implements Resizable {
    private static final int HEIGHT = 16;

    public static <X> InputWidgetWrapper<X> createWidgetWrapper(X data, Font font, String fieldName, int parentWidth, boolean needsComputing) {
        Component name = Component.literal(fieldName);

        int i = 0;
        int j = 0;

        if (needsComputing) {
            parentWidth = computeWidth(parentWidth);
        }

        InputWidgetWrapper<?> widgetWrapper = switch (data) {
            case Integer value -> new IntegerEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Short value -> new ShortEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Long value -> new LongEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Double value -> new DoubleEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Float value -> new FloatEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Boolean value -> new BooleanButton(font, i, j, parentWidth, HEIGHT, name, value);
            case Byte value -> new ByteEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Character value -> new CharacterEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case String value -> new StringEditBox(font, i, j, parentWidth, HEIGHT, name, value);
            case Enum<?> value -> new EnumButton(font, i, j, parentWidth, HEIGHT, name, value);
            case X value -> getExactType(font, i, j, parentWidth, HEIGHT, name, value);
        };

        return (InputWidgetWrapper<X>) widgetWrapper;
    }

    protected static int computeWidth(int listWidth) {
        return listWidth / 5;
    }

    private static <X> InputWidgetWrapper<?> getExactType(Font font, int i, int j, int k, int l, Component name, X value) {
        if (value instanceof List<?> list) {
            return new ListEditBoxCompound<>(font, i, j, k, l, name, list);
        }

        if (value instanceof int[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof short[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof long[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof double[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof float[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof boolean[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof byte[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof char[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, ArrayUtils.toObject(array));
        }

        if (value instanceof Object[] array) {
            return new ArrayEditBoxCompound<>(font, i, j, k, l, name, array);
        }

        return new ObjectEditBoxCompound(font, i, j, k, l, name, value);
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

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
    }

    public void setValue(T value) {
    }
}
