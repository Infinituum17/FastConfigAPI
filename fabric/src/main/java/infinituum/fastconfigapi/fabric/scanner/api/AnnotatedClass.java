package infinituum.fastconfigapi.fabric.scanner.api;

import java.util.Set;

public interface AnnotatedClass extends AnnotatedElement {
    Set<AnnotatedField> getAnnotatedFields();

    Set<AnnotatedMethod> getAnnotatedMethods();

    Class<?> get();

    boolean is(Class<?> clazz);

    boolean hasClassAnnotations();

    boolean hasFieldAnnotations();

    boolean hasMethodAnnotations();
}
