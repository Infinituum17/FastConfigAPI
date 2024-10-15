package infinituum.fastconfigapi.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigOption<T> {
    private final Consumer<T> setter;
    private final String fieldName;
    private final Supplier<T> getter;
    // TODO: Implement this class (we need to store it in each entry so that we can create an element directly from here).
    //  We probably need a setter (consumer-like) to set the original instance.
    //  Maybe this can also be optimized and done directly with a FastConfigFile interaction. <.<

    public ConfigOption(String fieldName, Supplier<T> getter, Consumer<T> setter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.setter = setter;
    }

    public T getValue() {
        return this.getter.get();
    }

    // TODO: Check casting String -> T
    public void setValue(T value) {
        this.setter.accept(value);
    }

    public String getFieldName() {
        return fieldName;
    }
}
