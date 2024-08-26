package infinituum.fastconfigapi.fabric.scanner.impl;

import java.util.ArrayList;
import java.util.function.Function;

public final class AnnotationData {
    public static final class EnumValue<T extends Enum<T>> {
        private final String className;
        private final String value;

        public EnumValue(String className, String value) {
            this.className = className;
            this.value = value;
        }

        public T get(Function<String, Class<T>> loadClass) {
            return get(loadClass.apply(className));
        }

        public T get(Class<T> clazz) {
            return Enum.valueOf(clazz, value);
        }
    }

    public static class ArrayValue {
        private final ArrayList<Object> values = new ArrayList<>();

        public void add(Object element) {
            this.values.add(element);
        }

        public Object[] get() {
            return values.toArray();
        }
    }
}
