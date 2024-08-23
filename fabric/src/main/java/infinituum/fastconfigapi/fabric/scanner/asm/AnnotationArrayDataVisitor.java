package infinituum.fastconfigapi.fabric.scanner.asm;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation.ArrayData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class AnnotationArrayDataVisitor extends AnnotationVisitor {
    private final String name;
    private final ModAnnotation annotation;
    private final ArrayData array;

    public AnnotationArrayDataVisitor(String name, ModAnnotation annotation) {
        super(Opcodes.ASM9);
        this.name = name;
        this.annotation = annotation;
        this.array = new ArrayData();
    }

    @Override
    public void visit(String name, Object value) {
        array.add(value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        String annotationName = Type.getType(descriptor).getClassName();
        ModAnnotation nestedAnnotation = new ModAnnotation(annotationName, this.name);

        annotation.addField(name, nestedAnnotation);

        return new AnnotationDataVisitor(nestedAnnotation, this.name);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return new AnnotationArrayDataVisitor(name, annotation);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        String className = Type.getType(descriptor).getClassName();
        annotation.addField(name, new ModAnnotation.EnumData(className, value));
    }

    @Override
    public void visitEnd() {
        annotation.addField(name, array);
        super.visitEnd();
    }
}
