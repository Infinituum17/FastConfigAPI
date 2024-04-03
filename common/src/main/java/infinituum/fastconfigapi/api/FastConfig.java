package infinituum.fastconfigapi.api;

import com.google.common.base.CaseFormat;

import java.io.File;
import java.nio.file.Path;

/**
 * Fast Config class.
 */
public class FastConfig {
    private final Path CONFIG_DIR_PATH;
    private FastConfig SUB_DIR_CONFIG;

    /**
     * {@link FastConfig} constructor.
     * @param configDirectory Path to the ".minecraft/config" directory (or custom).
     */
    public FastConfig(Path configDirectory) {
        this.CONFIG_DIR_PATH = configDirectory;
        this.createDirIfNotExist();
        this.SUB_DIR_CONFIG = null;
    }

    /**
     * {@link FastConfig} constructor.
     * @param configDirectory Path to the ".minecraft/config" directory (or custom).
     * @param subDirectoryName Name of the subdirectory (created if it doesn't exist).
     *                         This name is used to directly create a sub-FastConfig instance for files in a specific directory.
     */
    public FastConfig(Path configDirectory, final String subDirectoryName) {
        this.CONFIG_DIR_PATH = configDirectory;
        this.createDirIfNotExist();
        this.createSubDirConfig(subDirectoryName);
    }

    /**
     * Gets the directory path this {@link FastConfig} is targeting.
     * @return Path to the current Config target directory.
     */
    public Path getConfigDirPath() {
        return CONFIG_DIR_PATH;
    }

    /**
     * Gets the {@link FastConfig} associated with the subdirectory, if one exists.
     * @return The subdirectory {@link FastConfig}.
     */
    public FastConfig getSubDirConfig() {
        return SUB_DIR_CONFIG;
    }

    /**
     * Creates a subdirectory {@link FastConfig} from a subdirectory name.
     * @param subDirectoryName Name of a subdirectory (created if it doesn't exist).
     */
    public void createSubDirConfig(String subDirectoryName) {
        this.SUB_DIR_CONFIG = new FastConfig(CONFIG_DIR_PATH.resolve(subDirectoryName));
    }

    /**
     * Checks if the current Config target directory exists.
     * @return `true` if the current Config target directory exists, otherwise `false`.
     */
    private boolean existsDir() {
        return new File(CONFIG_DIR_PATH.toUri()).exists();
    }

    /**
     * Creates the current Config target directory.
     * @return `true` if the current Config target directory is created, otherwise `false`.
     */
    private boolean createDir() {
        return new File(CONFIG_DIR_PATH.toUri()).mkdir();
    }

    /**
     * If it doesn't exist, creates a directory (based on Config location).
     * @throws RuntimeException if the Config directory cannot not be created.
     */
    private void createDirIfNotExist() throws RuntimeException {
        if (!this.existsDir() && !this.createDir()) {
            throw new RuntimeException("Could not create Config directory \"" + this.CONFIG_DIR_PATH.toString() + "\"");
        }
    }

    /**
     * Checks if a file has a JSON extension.
     * @param fileName The file name.
     * @return the complete file name, otherwise `null` if the file name is not correct.
     */
    private String validateFileName(String fileName) {
        return (fileName.endsWith(".json")) ? fileName : fileName + ".json";
    }

    /**
     * Creates a new {@link FastConfigFile} based on attributes of the provided class' instance.
     * @param defaultInstance The class instance where the default values are set.
     * @return A {@link FastConfigFile} that contains an instance of the current class, modifiable at runtime.
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    public <T> FastConfigFile<T> getConfigFile(T defaultInstance) throws RuntimeException {
        String className = defaultInstance.getClass().getSimpleName();
        File configFile = new File(CONFIG_DIR_PATH.resolve(validateFileName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, className))).toUri());

        return new FastConfigFile<>(configFile, defaultInstance, CONFIG_DIR_PATH);
    }

    /**
     * Creates a new {@link FastConfigFile} based on attributes of the provided class' instance.
     * @param defaultInstance The class instance where the default values are set.
     * @param fileName A custom name that will be used for the Config file.
     * @return A {@link FastConfigFile} that contains an instance of the current class, modifiable at runtime.
     * @throws RuntimeException Thrown when errors occur in reading / writing files.
     */
    public <T> FastConfigFile<T> getConfigFile(T defaultInstance, String fileName) throws RuntimeException {
        File configFile = new File(CONFIG_DIR_PATH.resolve(validateFileName(fileName)).toUri());

        return new FastConfigFile<>(configFile, defaultInstance, CONFIG_DIR_PATH);
    }
}