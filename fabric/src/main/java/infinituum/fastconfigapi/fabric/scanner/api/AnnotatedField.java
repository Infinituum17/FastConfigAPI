package infinituum.fastconfigapi.fabric.scanner.api;

public interface AnnotatedField extends AnnotatedElement {
    String getClassName();

    String getDescriptor();

    boolean hasAnnotations();
}
