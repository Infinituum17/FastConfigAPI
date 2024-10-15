package infinituum.fastconfigapi.api;

import infinituum.fastconfigapi.api.annotations.FastConfig.Side;
import infinituum.fastconfigapi.api.annotations.Loader.Type;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Interface that defines all config file's methods.
 * <p>
 * Implementation: {@link FastConfigFileImpl}.
 *
 * @param <T> The type of the class instance contained in the current config file.
 */
public interface FastConfigFile<T> extends GenericConfigFile {
    /**
     * Gets the class contained in the current config file.
     *
     * @return A class of type {@link T}.
     */
    @NotNull
    Class<T> getConfigClass();

    /**
     * Gets the {@link Side side} on which this config file will be accessible.
     *
     * @return {@link Side Side}.
     */
    @NotNull
    Side getSide();

    /**
     * Gets the file name (used on disk) of this config file.
     *
     * @return {@link String}.
     */
    @NotNull
    String getFileName();

    /**
     * Gets the file name + extension (used on disk) of this config file.
     *
     * @return {@link String}.
     */
    @NotNull
    String getFileNameWithExtension();

    /**
     * Gets the full path to the subdirectory where the config file is stored.
     *
     * @return {@link Path}.
     */
    @NotNull
    Path getConfigSubdirectoryPath();

    /**
     * Gets the full path to the mod-loader's config directory (platform-specific).
     *
     * @return {@link Path}.
     */
    @NotNull
    Path getConfigDirectoryPath();

    /**
     * Gets the full path to the file that will contain the data stored in this config file.
     *
     * @return {@link Path}.
     */
    @NotNull
    Path getFullFilePath();

    /**
     * Gets the serializer instance used to serialize this config file.
     *
     * @return A {@link ConfigSerializer} instance.
     */
    @NotNull
    ConfigSerializer<T> getSerializer();

    /**
     * Gets the loader-serializer instance used to deserialize this config file.
     *
     * @return A {@link ConfigSerializer} instance.
     */
    @NotNull
    ConfigSerializer<T> getDeserializer();

    /**
     * If {@code true} all the file loading logging has been disabled.
     *
     * @return {@link Boolean}.
     */
    boolean isSilentlyFailing();

    /**
     * Gets the loader type used to load the default values of this config file.
     *
     * @return {@link Type}.
     */
    @NotNull
    Type getLoaderType();

    /**
     * Gets the loader target (if specified) of the current config file's loader.
     *
     * @return {@link String}.
     */
    @NotNull
    String getLoaderTarget();

    /**
     * Gets the latest instance of this config file.
     *
     * @return The instance's type {@link T}.
     */
    @NotNull
    T getInstance();

    /**
     * Gets a human-readable representation of the class name / file name used.
     *
     * @return The human-readable {@link String}.
     */
    String getHumanReadableName();

    /**
     * Gets the mod-id of the mod which manages this config.
     *
     * @return A mod-id {@link String}.
     */
    String getModId();
}
