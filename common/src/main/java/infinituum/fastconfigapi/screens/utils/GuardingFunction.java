package infinituum.fastconfigapi.screens.utils;

@FunctionalInterface
public interface GuardingFunction {
    boolean isValid(String string);
}
