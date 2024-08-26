package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.api.utils.UnsafeLoader;
import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner;
import infinituum.fastconfigapi.fabric.scanner.api.Annotation;

import java.util.HashMap;
import java.util.Map;

public class ModAnnotation implements Annotation {
    private final String name;
    private final Map<String, Object> fields;
    private final Class<?> clazz;

    public ModAnnotation(String name) {
        this.name = name;
        this.fields = new HashMap<>();
        this.clazz = UnsafeLoader.loadClass(name, ModAnnotationScanner.class.getClassLoader());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> get() {
        return clazz;
    }

    @Override
    public boolean is(Class<?> clazz) {
        return this.clazz.equals(clazz);
    }

    @Override
    public Map<String, Object> getFields() {
        return fields;
    }

    @Override
    public boolean hasFields() {
        return !fields.isEmpty();
    }

    @Override
    public boolean containsField(String fieldName) {
        return fields.containsKey(fieldName);
    }

    @Override
    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }

    public void addField(String key, Object value) {
        this.fields.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Name: ").append(name).append("\n");

        if (hasFields()) {
            result.append("Fields: \n");
            for (var fieldEntry : fields.entrySet()) {
                result.append(" - '")
                        .append(fieldEntry.getKey())
                        .append("' : '")
                        .append(fieldEntry.getValue())
                        .append("'\n");
            }
        }

        return result.toString();
    }
}
