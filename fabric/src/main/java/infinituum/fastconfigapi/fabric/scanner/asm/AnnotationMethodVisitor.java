package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

public final class AnnotationMethodVisitor extends MethodVisitor {
    private final List<ModAnnotation> annotations;
    private final String origin;

    public AnnotationMethodVisitor(MethodVisitor methodVisitor, List<ModAnnotation> annotations, String origin) {
        super(Opcodes.ASM9, methodVisitor);
        this.annotations = annotations;
        this.origin = origin;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation annotation = new ModAnnotation(annotationName, origin);

        annotations.add(annotation);

        return new AnnotationDataVisitor(annotation, origin);
    }

    // TODO: Implement other visitors
}
