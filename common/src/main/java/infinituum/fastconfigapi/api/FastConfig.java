package infinituum.fastconfigapi.api;

import com.google.common.base.CaseFormat;

import java.io.File;
import java.nio.file.Path;

public class FastConfig {
    private final Path CONFIG_DIR_PATH;
    private FastConfig SUB_DIR_CONFIG;

    /**
     * SimpleConfig constructor
     * @param configDirectory Path to the ".minecraft/config" directory
     */
    public FastConfig(Path configDirectory) {
        this.CONFIG_DIR_PATH = configDirectory;
        this.createDirIfNotExist();
        this.SUB_DIR_CONFIG = null;
    }

    /**
     * SimpleConfig constructor
     * @param configDirectory Path to the ".minecraft/config" directory
     * @param subDirectoryName Name of the subdirectory (created if it doesn't exist)
     */
    public FastConfig(Path configDirectory, final String subDirectoryName) {
        this.CONFIG_DIR_PATH = configDirectory;
        this.createDirIfNotExist();
        this.createSubDirConfig(subDirectoryName);
    }

    /**
     * Gets the current Config target directory
     * @return Path to the current Config target directory
     */
    public Path getConfigDirPath() {
        return CONFIG_DIR_PATH;
    }

    /**
     * Gets the subdirectory Config if one exists
     * @return the subdirectory Config
     */
    public FastConfig getSubDirConfig() {
        return SUB_DIR_CONFIG;
    }

    /**
     * Creates a subdirectory Config from a subdirectory name
     * @param subDirectoryName Name of the subdirectory (created if it doesn't exist)
     */
    public void createSubDirConfig(String subDirectoryName) {
        this.SUB_DIR_CONFIG = new FastConfig(CONFIG_DIR_PATH.resolve(subDirectoryName));
    }

    /**
     * Checks if the current Config target directory exists
     * @return `true` if the current Config target directory exists, `false` otherwise
     */
    private boolean existsDir() {
        return new File(CONFIG_DIR_PATH.toUri()).exists();
    }

    /**
     * Creates the current Config target directory
     * @return `true` if the current Config target directory is created, `false` otherwise
     */
    private boolean createDir() {
        return new File(CONFIG_DIR_PATH.toUri()).mkdir();
    }

    /**
     * Creates a directory (based on Config location) if it doesn't exist
     * @throws RuntimeException if the Config directory cannot not be created
     */
    private void createDirIfNotExist() throws RuntimeException {
        if (!this.existsDir() && !this.createDir()) {
            throw new RuntimeException("Could not create Config directory \"" + this.CONFIG_DIR_PATH.toString() + "\"");
        }
    }

    /**
     * Validates that a file name has a valid JSON extension
     * @param fileName The file name
     * @return the complete file name, otherwise `null` if the file name is not correct
     */
    private String validateFileName(String fileName) {
        return (fileName.endsWith(".json")) ? fileName : fileName + ".json";
    }

    /**
     * Gets or creates a new Config file based on attributes of the class provided
     * @param configTemplate The class used as a Config template
     * @return An instance of the `ConfigTemplate` class with values filled in
     * @throws RuntimeException For errors in reading/writing files
     */
    public <T> FastConfigFile<T> getConfigFile(Class<T> configTemplate) throws RuntimeException {
        File configFile = new File(CONFIG_DIR_PATH.resolve(validateFileName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, configTemplate.getSimpleName()))).toUri());

        return new FastConfigFile<>(configFile, configTemplate, CONFIG_DIR_PATH);
    }

    /**
     * Gets or creates a new Config file based on attributes of the class provided
     * @param fileName The Config file name
     * @param configTemplate The class used as a Config template
     * @return An instance of the `configTemplate` class with values filled in
     * @throws RuntimeException For errors in reading/writing files
     */
    public <T> FastConfigFile<T> getConfigFile(Class<T> configTemplate, String fileName) throws RuntimeException {
        File configFile = new File(CONFIG_DIR_PATH.resolve(validateFileName(fileName)).toUri());

        return new FastConfigFile<>(configFile, configTemplate, CONFIG_DIR_PATH);
    }
}
