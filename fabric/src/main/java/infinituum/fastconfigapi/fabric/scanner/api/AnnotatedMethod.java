package infinituum.fastconfigapi.fabric.scanner.api;

import java.util.List;

public interface AnnotatedMethod extends AnnotatedElement {
    String getClassName();

    String getDescriptor();

    List<TypeAnnotation> getTypeAnnotations();

    List<TryCatchAnnotation> getTryCatchAnnotations();

    List<ParameterAnnotation> getParameterAnnotations();

    List<InstructionAnnotation> getInstructionAnnotations();

    List<LocalVariableAnnotation> getLocalVariableAnnotations();

    boolean hasAnnotations();

    boolean hasTypeAnnotations();

    boolean hasTryCatchAnnotations();

    boolean hasParameterAnnotations();

    boolean hasInstructionAnnotations();
    
    boolean hasLocalVariableAnnotations();
}
