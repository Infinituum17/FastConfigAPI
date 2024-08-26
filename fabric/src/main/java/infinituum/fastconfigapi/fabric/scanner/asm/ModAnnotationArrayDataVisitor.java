package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.impl.AnnotationData.ArrayValue;
import infinituum.fastconfigapi.fabric.scanner.impl.AnnotationData.EnumValue;
import infinituum.fastconfigapi.fabric.scanner.impl.ModAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class ModAnnotationArrayDataVisitor extends AnnotationVisitor {
    private final ModAnnotation annotation;
    private final ArrayValue array;
    private String name;

    public ModAnnotationArrayDataVisitor(AnnotationVisitor visitor, ModAnnotation annotation) {
        super(Opcodes.ASM9, visitor);
        this.annotation = annotation;
        this.array = new ArrayValue();
    }

    @Override
    public void visit(String name, Object value) {
        this.name = name;
        array.add(value);

        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        String className = Type.getType(descriptor).getClassName();
        annotation.addField(name, new EnumValue<>(className, value));

        super.visitEnum(name, descriptor, value);
    }

    @Override
    public void visitEnd() {
        annotation.addField(name, array);

        super.visitEnd();
    }
}
