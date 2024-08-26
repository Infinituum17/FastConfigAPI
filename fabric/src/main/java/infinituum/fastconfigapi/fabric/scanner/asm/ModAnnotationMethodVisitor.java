package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.impl.*;
import org.objectweb.asm.*;

public final class ModAnnotationMethodVisitor extends MethodVisitor {
    private final AnnotatedClassImpl clazz;
    private final AnnotatedMethodImpl method;

    public ModAnnotationMethodVisitor(MethodVisitor methodVisitor, AnnotatedClassImpl clazz, String descriptor, String name) {
        super(Opcodes.ASM9, methodVisitor);
        this.clazz = clazz;
        this.method = new AnnotatedMethodImpl(clazz.getName(), descriptor, name);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitAnnotation(descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            ModAnnotation annotation = new ModAnnotation(annotationName);

            method.addAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            MethodTypeAnnotation annotation = new MethodTypeAnnotation(annotationName, typeRef, typePath);

            method.addTypeAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitParameterAnnotation(parameter, descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            MethodParameterAnnotation annotation = new MethodParameterAnnotation(annotationName, parameter);

            method.addParameterAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            MethodInstructionAnnotation annotation = new MethodInstructionAnnotation(annotationName, typeRef, typePath);

            method.addInstructionAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            MethodTryCatchAnnotation annotation = new MethodTryCatchAnnotation(annotationName, typeRef, typePath);

            method.addTryCatchAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        AnnotationVisitor nextVisitor = super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);

        if (visible) {
            Type annotationType = Type.getType(descriptor);
            String annotationName = annotationType.getClassName();
            MethodLocalVariableAnnotation annotation = new MethodLocalVariableAnnotation(annotationName, typeRef, typePath, start, end, index);

            method.addLocalVariableAnnotation(annotation);

            return new ModAnnotationVisitor(nextVisitor, annotation);
        }

        return nextVisitor;
    }

    @Override
    public void visitEnd() {
        if (hasAnyAnnotations()) {
            clazz.add(method);
        }

        super.visitEnd();
    }

    private boolean hasAnyAnnotations() {
        return !method.getAnnotations().isEmpty()
                || !method.getTypeAnnotations().isEmpty()
                || !method.getParameterAnnotations().isEmpty()
                || !method.getInstructionAnnotations().isEmpty()
                || !method.getTryCatchAnnotations().isEmpty()
                || !method.getLocalVariableAnnotations().isEmpty();
    }
}
