package infinituum.fastconfigapi.api;

import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.IOException;

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
     * Method that loads the provided state into the Fast Config class.
     */
    void loadStateUnsafely(String state) throws IOException;

    /**
     * Method that loads the default values of the Fast Config class.
     */
    void loadDefault() throws RuntimeException;
}
