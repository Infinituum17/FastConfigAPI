package infinituum.fastconfigapi.api;

import infinituum.fastconfigapi.api.config.FastConfigFile;

import java.util.HashMap;
import java.util.Map;

public final class FastConfigs {
    public static final String MOD_ID = "fastconfigapi";
    private static final Map<Class<?>, infinituum.fastconfigapi.api.config.FastConfigFile<?>> CONFIGS = new HashMap<>();

    private FastConfigs() {
    }

    public static <T> void register(Class<T> clazz, infinituum.fastconfigapi.api.config.FastConfigFile<T> config) {
        CONFIGS.put(clazz, config);
    }

    public static <T> void register(Map<Class<T>, infinituum.fastconfigapi.api.config.FastConfigFile<T>> configs) {
        CONFIGS.putAll(configs);
    }

    public static <T> infinituum.fastconfigapi.api.config.FastConfigFile<T> get(Class<T> clazz) {
        return (FastConfigFile<T>) CONFIGS.get(clazz);
    }
}
