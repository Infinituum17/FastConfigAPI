package infinituum.fastconfigapi.fabric.scanner.api;

import java.util.List;

public interface AnnotatedElement {
    List<Annotation> getAnnotations();

    String getName();
}
