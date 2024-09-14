package infinituum.fastconfigapi.api;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface FastConfigFile<T> extends ConfigFile {
    @NotNull
    Class<T> getConfigClass();

    @NotNull
    FastConfig.Side getSide();

    @NotNull
    String getFileName();

    @NotNull
    String getFileNameWithExtension();

    @NotNull
    Path getConfigSubdirectoryPath();

    @NotNull
    Path getConfigDirectoryPath();

    @NotNull
    String getConfigSubdirectoryName();

    @NotNull
    Path getFullFilePath();

    @NotNull
    ConfigSerializer<T> getSerializer();

    boolean isSilentlyFailing();

    @NotNull
    Loader.Type getLoaderType();

    @NotNull
    String getLoaderTarget();

    @NotNull
    T getInstance();
}
