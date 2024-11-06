package infinituum.fastconfigapi.impl;

import infinituum.fastconfigapi.api.annotations.FastDesc;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class ConfigMetadata<T> {
    private final String name;
    private final String description;
    private final String[] tooltip;
    private final Map<Field, ConfigFieldMetadata> fields;

    public ConfigMetadata(Class<T> clazz) {
        FastDesc clazzDesc = clazz.getAnnotation(FastDesc.class);

        if (clazzDesc != null) {
            String name = clazzDesc.name();
            String description = clazzDesc.description();
            String[] tooltip = clazzDesc.tooltip();

            if (name.isEmpty()) {
                name = clazz.getSimpleName();
            }

            this.name = Component.translatable(name).getString();
            this.description = Component.translatable(description).getString();
            this.tooltip = tooltip;
        } else {
            this.name = clazz.getSimpleName();
            this.description = "";
            this.tooltip = new String[]{};
        }

        this.fields = parseFields(clazz);
    }

    private Map<Field, ConfigFieldMetadata> parseFields(Class<T> clazz) {
        Map<Field, ConfigFieldMetadata> fieldMetadata = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            FastDesc fieldDesc = field.getAnnotation(FastDesc.class);

            if (fieldDesc != null) {
                String name = fieldDesc.name();
                String description = fieldDesc.description();
                String[] tooltip = fieldDesc.tooltip();

                if (name.isEmpty()) {
                    name = field.getName();
                }

                fieldMetadata.put(field, new ConfigFieldMetadata(
                        Component.translatable(name).getString(),
                        Component.translatable(description).getString(),
                        tooltip
                ));
            } else {
                fieldMetadata.put(field, new ConfigFieldMetadata(
                        field.getName(),
                        "",
                        new String[]{}
                ));
            }
        }

        return fieldMetadata;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getTooltip() {
        return tooltip;
    }

    public ConfigFieldMetadata getField(Field field) {
        return fields.get(field);
    }

    public Map<Field, ConfigFieldMetadata> getFields() {
        return fields;
    }

    public record ConfigFieldMetadata(String name, String description, String[] tooltip) {
    }
}
