package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.api.utils.UnsafeLoader;
import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner;
import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedClass;
import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedField;
import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedMethod;
import infinituum.fastconfigapi.fabric.scanner.api.Annotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AnnotatedClassImpl implements AnnotatedClass {
    private final List<Annotation> classAnnotations;
    private final Set<AnnotatedField> annotatedFields;
    private final Set<AnnotatedMethod> annotatedMethods;
    private final String className;
    private final Class<?> clazz;

    public AnnotatedClassImpl(String className) {
        this.classAnnotations = new ArrayList<>();
        this.annotatedFields = new HashSet<>();
        this.annotatedMethods = new HashSet<>();
        this.className = className;

        this.clazz = UnsafeLoader.loadClass(className, ModAnnotationScanner.class.getClassLoader());
    }

    public void add(Annotation annotation) {
        this.classAnnotations.add(annotation);
    }

    public void add(AnnotatedField field) {
        this.annotatedFields.add(field);
    }

    public void add(AnnotatedMethod method) {
        this.annotatedMethods.add(method);
    }

    @Override
    public List<Annotation> getAnnotations() {
        return classAnnotations;
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Set<AnnotatedField> getAnnotatedFields() {
        return annotatedFields;
    }

    @Override
    public Set<AnnotatedMethod> getAnnotatedMethods() {
        return annotatedMethods;
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
    public boolean hasClassAnnotations() {
        return !this.classAnnotations.isEmpty();
    }

    @Override
    public boolean hasFieldAnnotations() {
        return !this.annotatedFields.isEmpty();
    }

    @Override
    public boolean hasMethodAnnotations() {
        return !this.annotatedMethods.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Name: '").append(className).append("'\n");

        if (hasClassAnnotations()) {
            result.append("Annotations: \n");
            for (var annotation : classAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasFieldAnnotations()) {
            result.append("Fields: \n");
            for (var field : annotatedFields) {
                result.append(" - ").append(field.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasMethodAnnotations()) {
            result.append("Methods: \n");
            for (var method : annotatedMethods) {
                result.append(" - ").append(method.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        return result.toString();
    }
}
