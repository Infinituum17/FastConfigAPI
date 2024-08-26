package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.impl.AnnotatedClassImpl;
import infinituum.fastconfigapi.fabric.scanner.impl.ModAnnotation;
import org.objectweb.asm.*;

public final class ModAnnotationClassVisitor extends ClassVisitor {
    private final AnnotatedClassImpl annotatedClass;

    public ModAnnotationClassVisitor(AnnotatedClassImpl annotatedClass) {
        super(Opcodes.ASM9);
        this.annotatedClass = annotatedClass;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitAnnotation(descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            ModAnnotation annotation = new ModAnnotation(annotationType.getClassName());

            annotatedClass.add(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldVisitor nextVisitor = super.visitField(access, name, descriptor, signature, value);

        return new ModAnnotationFieldVisitor(nextVisitor, annotatedClass, descriptor, name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor nextVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        return new ModAnnotationMethodVisitor(nextVisitor, annotatedClass, descriptor, name);
    }
}
