package infinituum.fastconfigapi.fabric.scanner.api;

import org.objectweb.asm.TypePath;

public interface TryCatchAnnotation extends Annotation {
    int getTypeRef();

    TypePath getTypePath();
}
