package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.InstructionAnnotation;
import org.objectweb.asm.TypePath;

public final class MethodInstructionAnnotation extends ModAnnotation implements InstructionAnnotation {
    private final int typeRef;
    private final TypePath typePath;

    public MethodInstructionAnnotation(String name, int typeRef, TypePath typePath) {
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
