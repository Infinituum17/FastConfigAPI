package infinituum.fastconfigapi.screens.utils.renderer.type;

@FunctionalInterface
public interface GuardingFunction {
    boolean isValid(String oldValue, String newValue);
}
