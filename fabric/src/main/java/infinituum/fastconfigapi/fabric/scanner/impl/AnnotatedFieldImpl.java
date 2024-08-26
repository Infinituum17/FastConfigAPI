package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedField;
import infinituum.fastconfigapi.fabric.scanner.api.Annotation;

import java.util.ArrayList;
import java.util.List;

public final class AnnotatedFieldImpl implements AnnotatedField {
    private final String descriptor;
    private final String className;
    private final String fieldName;
    private final List<Annotation> fieldAnnotations;

    public AnnotatedFieldImpl(String className, String descriptor, String fieldName) {
        this.className = className;
        this.descriptor = descriptor;
        this.fieldName = fieldName;
        this.fieldAnnotations = new ArrayList<>();
    }

    public void add(Annotation annotation) {
        this.fieldAnnotations.add(annotation);
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean hasAnnotations() {
        return !fieldAnnotations.isEmpty();
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return fieldAnnotations;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Name: ").append(fieldName).append("\n");
        result.append("Descriptor: ").append(descriptor).append("\n");
        result.append("Class Name: ").append(className).append("\n");

        if (hasAnnotations()) {
            result.append("Annotations: \n");
            for (var annotation : fieldAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        return result.toString();
    }
}
