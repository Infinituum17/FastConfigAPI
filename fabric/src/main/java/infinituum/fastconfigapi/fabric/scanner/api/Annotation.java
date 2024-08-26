package infinituum.fastconfigapi.fabric.scanner.api;

import java.util.Map;

public interface Annotation {
    String getName();

    Map<String, Object> getFields();

    boolean hasFields();

    boolean containsField(String fieldName);

    Object getField(String fieldName);

    Class<?> get();

    boolean is(Class<?> clazz);
}
