package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.ParameterAnnotation;

public final class MethodParameterAnnotation extends ModAnnotation implements ParameterAnnotation {
    private final int parameter;

    public MethodParameterAnnotation(String name, int parameter) {
        super(name);
        this.parameter = parameter;
    }

    @Override
    public int getParameter() {
        return parameter;
    }
}
