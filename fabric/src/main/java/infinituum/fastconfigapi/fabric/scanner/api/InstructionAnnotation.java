package infinituum.fastconfigapi.fabric.scanner.api;

import org.objectweb.asm.TypePath;

public interface InstructionAnnotation extends Annotation {
    int getTypeRef();

    TypePath getTypePath();
}
