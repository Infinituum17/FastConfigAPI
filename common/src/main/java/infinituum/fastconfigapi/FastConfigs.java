package infinituum.fastconfigapi;

import infinituum.fastconfigapi.api.FastConfigFile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
     * Gets a specific {@link FastConfigFile}.
     *
     * @param clazz Class type that the {@link FastConfigFile} manages.
     * @param <T>   The type of the class' instance contained in the {@link FastConfigFile}.
     * @return The {@link FastConfigFile} corresponding to the class type passed in.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class.
     */
    public static <T> FastConfigFile<T> getFile(Class<T> clazz) throws RuntimeException {
        FastConfigFile<?> fastConfigFile = CONFIGS.get(clazz);

        if (fastConfigFile == null) {
            throw new RuntimeException("Tried to retrieve config of non-existent class '" + clazz.getSimpleName() + "'");
        }

        return (FastConfigFile<T>) CONFIGS.get(clazz);
    }

    /**
     * Gets a class instance contained in a {@link FastConfigFile}.
     * <p>
     * This method is mostly useful for "<b>read</b>" and "<b>edit</b>" (in memory) operations, since it doesn't automatically save
     * the new values to disk. For "<b>write</b>" operations you should use {@link FastConfigs#editAndSave(Class, Consumer)} or
     * get the {@link FastConfigFile} directly with {@link FastConfigs#getFile(Class)}.
     *
     * @param clazz Class type that the {@link FastConfigFile} manages.
     * @param <T>   The type of the class' instance contained in the {@link FastConfigFile}.
     * @return The class instance of the class type passed in.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class.
     */
    public static <T> T get(Class<T> clazz) throws RuntimeException {
        return getFile(clazz).getInstance();
    }

    /**
     * Executes the provided code on a {@link FastConfigFile}'s class instance and saves the result to disk.
     * <p>
     * The alternative of using this method is calling {@link FastConfigs#getFile(Class)} to get the {@link FastConfigFile}
     * directly.
     *
     * @param clazz          Class type that the {@link FastConfigFile} manages.
     * @param configConsumer The consumer that accepts a class instance (the actual config).
     * @param <T>            The type of the class' instance contained in the {@link FastConfigFile}.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class, or if the class could not be saved to disk.
     */
    public static <T> void editAndSave(Class<T> clazz, Consumer<T> configConsumer) {
        FastConfigFile<T> configFile = getFile(clazz);
        T instance = configFile.getInstance();

        configConsumer.accept(instance);

        configFile.save();
    }

    /**
     * Reloads all configs from disk.
     * <p>
     * By calling this method all not-saved changes in config files will be overwritten.
     */
    public static void reloadAll() {
        CONFIGS.forEach((clazz, config) -> config.load());
    }
}
