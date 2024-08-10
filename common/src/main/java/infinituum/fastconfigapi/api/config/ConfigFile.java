package infinituum.fastconfigapi.api.config;

/**
 * Interface that defines Config's methods to handle Files.
 * <p>
 * See {@link FastConfigFile}.
 */
public interface ConfigFile {
    /**
     * Method that saves the current state of the Fast Config class.
     */
    void save();

    /**
     * Method that loads the current state (on disk) into the Fast Config class.
     */
    void load();

    /**
     * Method that loads the default values of the Fast Config class.
     */
    void loadDefault();
}
