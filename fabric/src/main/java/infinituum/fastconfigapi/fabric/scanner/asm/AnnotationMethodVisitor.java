package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

public final class AnnotationMethodVisitor extends MethodVisitor {
    private final List<ModAnnotation> annotations;

    public AnnotationMethodVisitor(MethodVisitor methodVisitor, List<ModAnnotation> annotations) {
        super(Opcodes.ASM9, methodVisitor);
        this.annotations = annotations;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation annotation = new ModAnnotation(annotationName);

        annotations.add(annotation);

        return new AnnotationDataVisitor(annotation);
    }

    // TODO: Implement other visitors
}
