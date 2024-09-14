package infinituum.fastconfigapi;

import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.util.HashMap;
import java.util.Map;

public final class FastConfigs {
    private static final Map<Class<?>, FastConfigFileImpl<?>> CONFIGS = new HashMap<>();

    private FastConfigs() {
    }

    public static <T> void register(Class<T> clazz, FastConfigFileImpl<T> config) {
        CONFIGS.put(clazz, config);
    }

    public static <T> void register(Map<Class<T>, FastConfigFileImpl<T>> configs) {
        CONFIGS.putAll(configs);
    }

    public static <T> FastConfigFileImpl<T> get(Class<T> clazz) {
        return (FastConfigFileImpl<T>) CONFIGS.get(clazz);
    }

    // TODO: Create run-in-place method
}
