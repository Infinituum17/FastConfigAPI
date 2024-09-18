package infinituum.fastconfigapi;

import infinituum.fastconfigapi.api.FastConfigFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class used to manage {@link infinituum.fastconfigapi.api.FastConfigFile FastConfigFiles}.
 */
public final class FastConfigs {
    private static final Map<Class<?>, FastConfigFile<?>> CONFIGS = new HashMap<>();

    private FastConfigs() {
    }

    /**
     * Registers a new {@link FastConfigFile}.
     * <p>
     * The class contained inside this {@link FastConfigFile} will be used as a map index.
     *
     * @param config The {@link FastConfigFile}
     * @param <T>    The type of the class' instance contained in the {@link FastConfigFile} passed in.
     */
    public static <T> void register(FastConfigFile<T> config) {
        CONFIGS.put(config.getConfigClass(), config);
    }

    /**
     * Registers new {@link FastConfigFile FastConfigFiles}.
     *
     * @param configs A map containing a collection of classes and their corresponding {@link FastConfigFile FastConfigFiles}.
     * @param <T>     The type of the class' instance contained in the {@link FastConfigFile} passed in.
     */
    public static <T> void register(Map<Class<T>, FastConfigFile<T>> configs) {
        CONFIGS.putAll(configs);
    }

    /**
     * Gets a {@link FastConfigFile}.
     *
     * @param clazz Class that the {@link FastConfigFile} manages.
     * @param <T>   The type of the class' instance contained in the {@link FastConfigFile}.
     * @return The {@link FastConfigFile} corresponding to the class passed in.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class.
     */
    public static <T> FastConfigFile<T> get(Class<T> clazz) throws RuntimeException {
        FastConfigFile<?> fastConfigFile = CONFIGS.get(clazz);

        if (fastConfigFile == null) {
            throw new RuntimeException("Tried to retrieve config of non-existent class '" + clazz.getSimpleName() + "'");
        }

        return (FastConfigFile<T>) CONFIGS.get(clazz);
    }

    /**
     * Reloads all configs from disk.
     */
    public static void reloadAll() {
        CONFIGS.forEach((clazz, config) -> config.load());
    }
}
