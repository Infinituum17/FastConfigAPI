package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.LocalVariableAnnotation;
import org.objectweb.asm.Label;
import org.objectweb.asm.TypePath;

public final class MethodLocalVariableAnnotation extends ModAnnotation implements LocalVariableAnnotation {
    private final int typeRef;
    private final TypePath typePath;
    private final Label[] start;
    private final Label[] end;
    private final int[] index;

    public MethodLocalVariableAnnotation(String name, int typeRef, TypePath typePath, Label[] start,
                                         Label[] end, int[] index) {
        super(name);
        this.typeRef = typeRef;
        this.typePath = typePath;
        this.start = start;
        this.end = end;
        this.index = index;
    }

    @Override
    public int typeRef() {
        return typeRef;
    }

    @Override
    public TypePath typePath() {
        return typePath;
    }

    @Override
    public Label[] start() {
        return start;
    }

    @Override
    public Label[] end() {
        return end;
    }

    @Override
    public int[] index() {
        return index;
    }
}
