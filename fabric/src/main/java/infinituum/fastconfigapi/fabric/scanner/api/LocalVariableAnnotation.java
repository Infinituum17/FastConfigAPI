package infinituum.fastconfigapi.fabric.scanner.api;

import org.objectweb.asm.Label;
import org.objectweb.asm.TypePath;

public interface LocalVariableAnnotation extends Annotation {
    int typeRef();

    TypePath typePath();

    Label[] start();

    Label[] end();

    int[] index();
}
