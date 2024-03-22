package infinituum.fastconfigapi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;

public class FastConfigFile<T> {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File CONFIG_FILE;
    private final Class<T> CONFIG_TEMPLATE;
    private final Path CONFIG_DIR;
    private T INSTANCE;

    /**
     * Default constructor. You aren't supposed to directly use this.
     * Use `SimpleConfig` to create a `ConfigFile` instance
     * @param file The targeted Config file
     * @param configTemplate The class associated with this Config file
     * @param configDir The path to the config directory where the Config file is created
     */
    public FastConfigFile(File file, Class<T> configTemplate, Path configDir) {
        this.CONFIG_FILE = file;
        this.CONFIG_TEMPLATE = configTemplate;
        this.CONFIG_DIR = configDir;

        createIfNotExist();
        update();
    }

    /**
     * Gets the Config directory targeted by a specific ConfigFile
     * @return The Config directory path
     */
    public Path getConfigDir() {
        return CONFIG_DIR;
    }

    /**
     * Gets the current Config values. Use function `update` to reload values from disk
     * @return The class object associated with configTemplate, `null` if the file doesn't exist
     */
    public T get() {
        return INSTANCE;
    }

    /**
     * Updates a Config file's instance
     * @throws RuntimeException For errors in reading files
     */
    public void update() throws RuntimeException {
        if(!CONFIG_FILE.exists()) return;

        Reader configFileReader;

        try {
            configFileReader = Files.newBufferedReader(CONFIG_FILE.toPath());
        } catch (IOException error) {
            throw new RuntimeException("Could not read Config file \"" + CONFIG_FILE.getName() + "\" in directory \"" + CONFIG_DIR + "\": " + error);
        }

        INSTANCE = GSON.fromJson(configFileReader, CONFIG_TEMPLATE);
    }

    /**
     * Creates a new Config file if one doesn't exist
     * @throws RuntimeException For errors in writing files
     */
    private void createIfNotExist() throws RuntimeException {
        boolean needsDefaultWrite;

        try {
            needsDefaultWrite = CONFIG_FILE.createNewFile();
        } catch (IOException error) {
            throw new RuntimeException("Could not create Config file \"" + CONFIG_FILE.getName() + "\": " + error);
        }

        if(!needsDefaultWrite && CONFIG_FILE.length() == 0) {
            needsDefaultWrite = true;
        }

        if(needsDefaultWrite) {
            writeDefault();
        }
    }

    /**
     * Writes the default values in the targeted file
     * @throws RuntimeException For errors in writing files
     */
    private void writeDefault() throws RuntimeException {
        try {
            String json = GSON.toJson(validateConfigInstance(CONFIG_TEMPLATE));
            FileWriter writer = new FileWriter(CONFIG_FILE);

            writer.write(json);

            writer.close();
        } catch(Exception error) {
            throw new RuntimeException("Error while writing Config file \"" + CONFIG_FILE.getName() + "\": " + error);
        }
    }

    /**
     * Deletes a Config file. Full file path is based on the config directory that generated it.
     * @return `true` if and only if the file or directory is successfully deleted, otherwise `false`
     */
    public boolean deleteFile() {
        return CONFIG_FILE.exists() && CONFIG_FILE.delete();
    }

    /**
     * Deletes a Config file's contents. Full file path is based on the config directory that generated it.
     * @return `true` if and only if the file or directory is successfully deleted, otherwise `false`
     */
    public boolean deleteContents() {
        if(CONFIG_FILE.exists()) {
            try {
                String json = GSON.toJson(validateConfigInstance(CONFIG_TEMPLATE));
                FileWriter writer = new FileWriter(CONFIG_FILE);
                writer.write(json);
                writer.close();

                return true;
            } catch (Exception error) {
                throw new RuntimeException("Error while writing Config file \"" + CONFIG_FILE.getName() + "\": " + error);
            }
        }

        return false;
    }
    /**
     * Validates that a class is suitable to become a Config
     * @param configTemplate The class used as a Config template
     * @return An instance of the class
     */
    private T validateConfigInstance(Class<T> configTemplate) {
        Constructor<T> emptyConstructor;

        try {
            emptyConstructor = configTemplate.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error during Config validation in class \"" + configTemplate.getSimpleName() + "\": Empty default constructor is not defined");
        }

        emptyConstructor.setAccessible(true);

        try {
            return emptyConstructor.newInstance();
        } catch (Exception error) {
            throw new RuntimeException("Error during Config validation in class \"" + configTemplate.getSimpleName() + "\": Could not call empty default constructor");
        }
    }

}