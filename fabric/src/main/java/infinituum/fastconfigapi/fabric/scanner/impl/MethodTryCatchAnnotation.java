package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.TryCatchAnnotation;
import org.objectweb.asm.TypePath;

public final class MethodTryCatchAnnotation extends ModAnnotation implements TryCatchAnnotation {
    private final int typeRef;
    private final TypePath typePath;

    public MethodTryCatchAnnotation(String name, int typeRef, TypePath typePath) {
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
