package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.impl.AnnotationData.EnumValue;
import infinituum.fastconfigapi.fabric.scanner.impl.ModAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class ModAnnotationVisitor extends AnnotationVisitor {
    private final ModAnnotation annotation;

    public ModAnnotationVisitor(AnnotationVisitor annotationVisitor, ModAnnotation annotation) {
        super(Opcodes.ASM9, annotationVisitor);
        this.annotation = annotation;
    }

    @Override
    public void visit(String name, Object value) {
        annotation.addField(name, value);

        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        String className = Type.getType(descriptor).getClassName();
        annotation.addField(name, new EnumValue<>(className, value));

        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor nextVisitor = super.visitArray(name);

        return new ModAnnotationArrayDataVisitor(nextVisitor, annotation);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        AnnotationVisitor nextVisitor = super.visitAnnotation(name, descriptor);
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation nestedAnnotation = new ModAnnotation(annotationName);

        annotation.addField(name, nestedAnnotation);

        return new ModAnnotationVisitor(nextVisitor, nestedAnnotation);
    }
}
