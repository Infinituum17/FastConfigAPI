package infinituum.fastconfigapi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Fast Config File class.
 *
 * @param <T> The type of the class which this Fast Config File is based on.
 */
@Deprecated(forRemoval = true)
public class FastConfigFile<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File CONFIG_FILE;
    private final T CONFIG_DEFAULT_INSTANCE;
    private final Class<T> CONFIG_CLASS;
    private final Path CONFIG_DIR;
    private T CACHED_INSTANCE;

    /**
     * Default constructor. You aren't supposed to directly use this.
     * Use the {@link FastConfig} class to create a {@link FastConfigFile} instance.
     *
     * @param file            The targeted Config file.
     * @param defaultInstance A default instance of the Config class.
     * @param configDir       The path to the Config directory where the Config file is created.
     */
    @SuppressWarnings("unchecked")
    public FastConfigFile(File file, T defaultInstance, Path configDir) {
        this.CONFIG_FILE = file;
        this.CONFIG_DEFAULT_INSTANCE = defaultInstance;
        this.CONFIG_CLASS = (Class<T>) defaultInstance.getClass();
        this.CONFIG_DIR = configDir;

        this.createIfNotExist();
        this.reloadConfig();
    }

    /**
     * Gets the Config directory targeted by <code>this</code> specific {@link FastConfigFile}.
     *
     * @return The Config directory targeted by <code>this</code> specific {@link FastConfigFile}.
     */
    public Path getConfigDir() {
        return CONFIG_DIR;
    }

    /**
     * Gets the cached Config instance.
     *
     * @return The cached Config instance, <code>null</code> if the file doesn't exist.
     */
    public T getConfig() {
        return CACHED_INSTANCE;
    }

    /**
     * Reads a Config file from disk and updates the cached Config instance.
     *
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    public void reloadConfig() throws RuntimeException {
        if (!CONFIG_FILE.exists()) {
            return;
        }

        Reader configFileReader;

        try {
            configFileReader = Files.newBufferedReader(CONFIG_FILE.toPath());
        } catch (IOException error) {
            throw new RuntimeException("Could not read Config file \"" + CONFIG_FILE.getName() + "\" in directory \"" + CONFIG_DIR + "\": " + error);
        }

        this.CACHED_INSTANCE = GSON.fromJson(configFileReader, this.CONFIG_CLASS);
    }

    /**
     * Writes the current cached values to the targeted Config file on disk.
     *
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    public void saveCurrent() throws RuntimeException {
        try {
            String json = GSON.toJson(CACHED_INSTANCE);
            FileWriter writer = new FileWriter(CONFIG_FILE);

            writer.write(json);

            writer.close();
        } catch (Exception error) {
            throw new RuntimeException("Error while writing Config file \"" + CONFIG_FILE.getName() + "\": " + error);
        }
    }

    /**
     * Writes the default values to the targeted Config file on disk.
     *
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    public void saveDefault() throws RuntimeException {
        try {
            String json = GSON.toJson(CONFIG_DEFAULT_INSTANCE);
            FileWriter writer = new FileWriter(CONFIG_FILE);

            writer.write(json);

            writer.close();
        } catch (Exception error) {
            throw new RuntimeException("Error while writing Config file \"" + CONFIG_FILE.getName() + "\": " + error);
        }
    }

    /**
     * Creates a new Config file if it doesn't already exist.
     *
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    private void createIfNotExist() throws RuntimeException {
        boolean needsDefaultWrite;

        try {
            needsDefaultWrite = CONFIG_FILE.createNewFile();
        } catch (IOException error) {
            throw new RuntimeException("Could not create Config file \"" + CONFIG_FILE.getName() + "\": " + error);
        }

        if (!needsDefaultWrite && CONFIG_FILE.length() == 0) {
            needsDefaultWrite = true;
        }

        if (needsDefaultWrite) {
            saveDefault();
        }
    }
}