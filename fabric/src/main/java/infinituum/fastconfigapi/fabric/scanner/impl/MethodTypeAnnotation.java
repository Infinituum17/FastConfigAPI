package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.TypeAnnotation;
import org.objectweb.asm.TypePath;

public final class MethodTypeAnnotation extends ModAnnotation implements TypeAnnotation {
    private final int typeRef;
    private final TypePath typePath;

    public MethodTypeAnnotation(String name, int typeRef, TypePath typePath) {
        super(name);
        this.typeRef = typeRef;
        this.typePath = typePath;
    }

    @Override
    public int getTypeRef() {
        return typeRef;
    }

    @Override
    public TypePath getTypePath() {
        return typePath;
    }
}
