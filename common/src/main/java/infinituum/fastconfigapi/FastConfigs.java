package infinituum.fastconfigapi;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.annotations.Loader.Type;
import infinituum.fastconfigapi.utils.Global;

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
     * Runs code on a class' instance that a {@link FastConfigFile} manages and saves the new state to disk.
     *
     * @param clazz          Class that the {@link FastConfigFile} manages.
     * @param configConsumer A consumer that accepts a class' instance as a parameter.
     * @param <T>            The type of the class' instance contained in the {@link FastConfigFile}.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class or if the new state could not be saved.
     */
    public static <T> void run(Class<T> clazz, Consumer<T> configConsumer) throws RuntimeException {
        FastConfigFile<T> config = get(clazz);

        configConsumer.accept(config.getInstance());

        config.save();
    }

    /**
     * Runs asynchronous code on a class' instance that a {@link FastConfigFile} manages and saves the new state to disk.
     * <p>
     * This method is useful if this {@link FastConfigFile} is using a {@link Loader} that could take some time to process
     * a data request (e.g. {@link Type#URL Loader.Type.URL}).
     *
     * @param clazz          Class that the {@link FastConfigFile} manages.
     * @param configConsumer A consumer that accepts a class' instance as a parameter.
     * @param <T>            The type of the class' instance contained in the {@link FastConfigFile}.
     * @throws RuntimeException Thrown when we're trying to access a non-existent class or if the new state could not be saved.
     */
    public static <T> void runAsync(Class<T> clazz, Consumer<T> configConsumer) throws RuntimeException {
        FastConfigFile<T> config = get(clazz);

        config.getInstanceAsync().thenAccept(instance -> {
            configConsumer.accept(instance);
            config.save();
        }).exceptionally(t -> {
            Thread.currentThread().setName("RunAsync Method");
            Global.LOGGER.info("Config '{}' could not be saved: {}", config.getFileNameWithExtension(), t);
            return null;
        });
    }
}
