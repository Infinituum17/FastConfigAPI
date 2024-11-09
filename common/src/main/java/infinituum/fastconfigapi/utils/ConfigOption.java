package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.gui.Font;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigOption<T> {
    private final Consumer<T> setter;
    private final Supplier<T> getter;
    private final Font font;
    private InputWidgetWrapper<T> widgetWrapper;

    public ConfigOption(Supplier<T> getter, Consumer<T> setter, Font font) {
        this.getter = getter;
        this.setter = setter;
        this.font = font;
    }

    public InputWidgetWrapper<T> createWidgetWrapper(String fieldName, int width) {
        if (widgetWrapper == null) {
            this.widgetWrapper = InputWidgetWrapper.createWidgetWrapper(getter.get(), font, fieldName, width, true);
        }

        return widgetWrapper;
    }

    public T getValue() {
        return this.getter.get();
    }

    public void setValue(T value) {
        this.setter.accept(value);
    }
}
