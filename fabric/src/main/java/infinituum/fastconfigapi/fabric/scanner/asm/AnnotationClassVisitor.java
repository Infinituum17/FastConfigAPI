package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import org.objectweb.asm.*;

import java.util.List;

public final class AnnotationClassVisitor extends ClassVisitor {
    private final List<ModAnnotation> annotations;
    private final String className;

    public AnnotationClassVisitor(List<ModAnnotation> annotations, String className) {
        super(Opcodes.ASM9);
        this.annotations = annotations;
        this.className = className;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation annotation = new ModAnnotation(annotationName, className);

        annotations.add(annotation);

        return new AnnotationDataVisitor(annotation, className);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new AnnotationMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), annotations, className);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
}
