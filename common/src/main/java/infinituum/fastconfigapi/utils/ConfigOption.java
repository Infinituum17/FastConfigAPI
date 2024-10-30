package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.screens.utils.InputWidgetWrapper;
import net.minecraft.client.gui.Font;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigOption<T> {
    private final Consumer<T> setter;
    private final String fieldName;
    private final Supplier<T> getter;
    private final InputWidgetWrapper<T> widgetWrapper;

    public ConfigOption(String fieldName, Supplier<T> getter, Consumer<T> setter, Font font) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
        this.widgetWrapper = InputWidgetWrapper.createWidgetWrapper(getter.get(), font, fieldName);
    }

    public T getValue() {
        return this.getter.get();
    }

    public void setValue(T value) {
        this.setter.accept(value);
    }

    public String getFieldName() {
        return fieldName;
    }

    public InputWidgetWrapper<?> getWidgetWrapper() {
        return this.widgetWrapper;
    }
}
