package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.*;

import java.util.ArrayList;
import java.util.List;

public final class AnnotatedMethodImpl implements AnnotatedMethod {
    private final String descriptor;
    private final String className;
    private final String methodName;
    private final List<Annotation> methodAnnotations;
    private final List<TypeAnnotation> typeAnnotations;
    private final List<ParameterAnnotation> parameterAnnotations;
    private final List<InstructionAnnotation> instructionAnnotations;
    private final List<TryCatchAnnotation> tryCatchAnnotations;
    private final List<LocalVariableAnnotation> localVariableAnnotations;

    public AnnotatedMethodImpl(String className, String descriptor, String methodName) {
        this.className = className;
        this.descriptor = descriptor;
        this.methodName = methodName;
        this.methodAnnotations = new ArrayList<>();
        this.typeAnnotations = new ArrayList<>();
        this.parameterAnnotations = new ArrayList<>();
        this.instructionAnnotations = new ArrayList<>();
        this.tryCatchAnnotations = new ArrayList<>();
        this.localVariableAnnotations = new ArrayList<>();
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean hasAnnotations() {
        return !methodAnnotations.isEmpty();
    }

    @Override
    public boolean hasTypeAnnotations() {
        return !typeAnnotations.isEmpty();
    }

    @Override
    public boolean hasParameterAnnotations() {
        return !parameterAnnotations.isEmpty();
    }

    @Override
    public boolean hasInstructionAnnotations() {
        return !instructionAnnotations.isEmpty();
    }

    @Override
    public boolean hasTryCatchAnnotations() {
        return !tryCatchAnnotations.isEmpty();
    }

    @Override
    public boolean hasLocalVariableAnnotations() {
        return !localVariableAnnotations.isEmpty();
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public List<TypeAnnotation> getTypeAnnotations() {
        return typeAnnotations;
    }

    @Override
    public List<ParameterAnnotation> getParameterAnnotations() {
        return parameterAnnotations;
    }

    @Override
    public List<InstructionAnnotation> getInstructionAnnotations() {
        return instructionAnnotations;
    }

    @Override
    public List<TryCatchAnnotation> getTryCatchAnnotations() {
        return tryCatchAnnotations;
    }

    @Override
    public List<LocalVariableAnnotation> getLocalVariableAnnotations() {
        return localVariableAnnotations;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return methodAnnotations;
    }

    public void addAnnotation(ModAnnotation annotation) {
        this.methodAnnotations.add(annotation);
    }

    public void addTypeAnnotation(MethodTypeAnnotation annotation) {
        this.typeAnnotations.add(annotation);
    }

    public void addParameterAnnotation(MethodParameterAnnotation annotation) {
        this.parameterAnnotations.add(annotation);
    }

    public void addInstructionAnnotation(MethodInstructionAnnotation annotation) {
        this.instructionAnnotations.add(annotation);
    }

    public void addTryCatchAnnotation(MethodTryCatchAnnotation annotation) {
        this.tryCatchAnnotations.add(annotation);
    }

    public void addLocalVariableAnnotation(MethodLocalVariableAnnotation annotation) {
        this.localVariableAnnotations.add(annotation);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Name: ").append(methodName).append("\n");
        result.append("Descriptor: ").append(descriptor).append("\n");
        result.append("Class Name: ").append(className).append("\n");

        if (hasAnnotations()) {
            result.append("Annotations: \n");
            for (var annotation : methodAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasTypeAnnotations()) {
            result.append("Type Annotations: \n");
            for (var annotation : typeAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasParameterAnnotations()) {
            result.append("Parameter Annotations: \n");
            for (var annotation : parameterAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasInstructionAnnotations()) {
            result.append("Instruction Annotations: \n");
            for (var annotation : instructionAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasTryCatchAnnotations()) {
            result.append("Try Catch Annotations: \n");
            for (var annotation : tryCatchAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        if (hasLocalVariableAnnotations()) {
            result.append("Local Variables Annotations: \n");
            for (var annotation : localVariableAnnotations) {
                result.append(" - ").append(annotation.toString().replace("\n", "\n   ")).append("\n");
            }
        }

        return result.toString();
    }
}
