package infinituum.fastconfigapi.api;

import infinituum.fastconfigapi.impl.FastConfigFileImpl;

/**
 * Interface that defines Config's methods to handle Files.
 * <p>
 * See {@link FastConfigFileImpl}.
 */
public interface ConfigFile {
    /**
     * Method that saves the current state of the Fast Config class.
     */
    void save() throws RuntimeException;

    /**
     * Method that loads the current state (on disk) into the Fast Config class.
     */
    void load() throws RuntimeException;

    /**
     * Method that loads the default values of the Fast Config class.
     * <p>
     * Should not be called directly since it performs async operations in sync ways (the thread can be blocked).
     */
    void loadDefault() throws RuntimeException;
}
