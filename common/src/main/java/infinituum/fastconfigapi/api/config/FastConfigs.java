package infinituum.fastconfigapi.api.config;

import java.util.HashMap;
import java.util.Map;

public final class FastConfigs {
    public static final String MOD_ID = "fastconfigapi";
    private static final Map<Class<?>, FastConfigFile<?>> CONFIGS = new HashMap<>();

    private FastConfigs() {
    }

    public static <T> void register(Class<T> clazz, FastConfigFile<T> config) {
        CONFIGS.put(clazz, config);
    }

    public static <T> void register(Map<Class<T>, FastConfigFile<T>> configs) {
        CONFIGS.putAll(configs);
    }

    public static <T> FastConfigFile<T> get(Class<T> clazz) {
        return (FastConfigFile<T>) CONFIGS.get(clazz);
    }
}
