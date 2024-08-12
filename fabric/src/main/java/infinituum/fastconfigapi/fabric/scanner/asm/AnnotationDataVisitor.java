package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class AnnotationDataVisitor extends AnnotationVisitor {
    private final ModAnnotation annotation;

    public AnnotationDataVisitor(ModAnnotation annotation) {
        super(Opcodes.ASM9);
        this.annotation = annotation;
    }

    @Override
    public void visit(String name, Object value) {
        annotation.addField(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        String className = Type.getType(descriptor).getClassName();
        annotation.addField(name, new ModAnnotation.EnumData(className, value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation nestedAnnotation = new ModAnnotation(annotationName);

        annotation.addField(name, nestedAnnotation);

        return new AnnotationDataVisitor(nestedAnnotation);
    }


    @Override
    public AnnotationVisitor visitArray(String name) {
        return new AnnotationArrayDataVisitor(name, annotation);
    }
}
