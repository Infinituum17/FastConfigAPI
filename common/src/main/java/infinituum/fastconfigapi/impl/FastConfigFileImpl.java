package infinituum.fastconfigapi.impl;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.helpers.FastConfigHelper;
import infinituum.fastconfigapi.api.helpers.LoaderHelper;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.utils.Global;
import infinituum.void_lib.api.utils.UnsafeLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Actual implementation of the interface {@link FastConfigFile}.
 *
 * @param <T> The type of the class instance contained in the current config file.
 */
public final class FastConfigFileImpl<T> implements FastConfigFile<T> {
    private final Class<T> clazz;
    private final FastConfig.Side side;
    private final String fileName;
    private final Path configDirectoryPath;
    private final Path configSubdirectoryPath;
    private final Path fullFilePath;
    private final ConfigSerializer<T> serializer;
    private final boolean silentlyFail;
    private final ConfigSerializer<T> originalDeserializer;
    private ConfigSerializer<T> deserializer;
    private Loader.Type loaderType;
    private String loaderTarget;
    private T instance;

    public FastConfigFileImpl(@NotNull Class<T> clazz, @NotNull FastConfig.Side side, @NotNull Map<String, Object> data) {
        String subdirectoryStringPath = FastConfigHelper.getSubdirectoryOrDefault(data);

        this.clazz = clazz;
        this.side = side;
        this.fileName = FastConfigHelper.getFileNameOrDefault(data, clazz.getSimpleName(), side);
        this.serializer = FastConfigHelper.getSerializerOrDefault(data);
        this.configDirectoryPath = PlatformHelper.getDefaultConfigDirPath();
        this.configSubdirectoryPath = (!subdirectoryStringPath.isEmpty()) ? configDirectoryPath.resolve(subdirectoryStringPath) : configDirectoryPath;
        this.fullFilePath = this.configSubdirectoryPath.resolve(this.getFileNameWithExtension());
        this.loaderType = LoaderHelper.getLoaderOrDefault(data);
        this.loaderTarget = LoaderHelper.getTargetOrDefault(data);
        this.silentlyFail = LoaderHelper.getSilentlyFailOrDefault(data);
        this.originalDeserializer = LoaderHelper.getOriginalDeserializerOrDefault(data, this.serializer, this.loaderType);

        if (LoaderHelper.isCurrentDeserializerOriginal(this.fullFilePath)) {
            this.deserializer = originalDeserializer;
        } else {
            this.deserializer = serializer;
        }
    }

    @Override
    public void loadDefault() throws RuntimeException {
        this.loaderType.load(this);

        if (!this.deserializer.getClass().equals(this.serializer.getClass())) {
            this.deserializer = this.serializer;
        }

        this.save();
    }

    @Override
    public void load() throws RuntimeException {
        try {
            deserializer.deserialize(this);
        } catch (Exception e) {
            try {
                this.loadDefault();
            } catch (Exception e2) {
                throw new RuntimeException("Config '" + getFileNameWithExtension() + "' could not be loaded");
            }
        }

        Global.LOGGER.info("Config '{}' was successfully loaded", this.getFileNameWithExtension());
    }

    public void loadStateUnsafely(String content) throws IOException {
        originalDeserializer.deserialize(this, new StringReader(content));
    }

    @Override
    public void save() throws RuntimeException {
        try {
            Files.createDirectories(this.getConfigSubdirectoryPath());

            serializer.serialize(this);
        } catch (Exception e) {
            throw new RuntimeException("Contents of Config '" + getFileNameWithExtension() + "' could not be saved: " + e);
        }
    }

    public void setDefaultClassInstance() {
        T instance = UnsafeLoader.loadInstance(clazz);

        if (instance == null) {
            throw new RuntimeException("Could not load default instance for class '" + clazz.getSimpleName() + "'");
        }

        this.setInstance(instance);
    }

    @Override
    public @NotNull String getFileNameWithExtension() {
        return fileName + "." + this.serializer.getFileExtension();
    }

    @Override
    public @NotNull Path getConfigSubdirectoryPath() {
        return configSubdirectoryPath;
    }

    @Override
    public @NotNull Path getConfigDirectoryPath() {
        return configDirectoryPath;
    }

    @Override
    public @NotNull String getFileName() {
        return fileName;
    }

    @Override
    public FastConfig.@NotNull Side getSide() {
        return side;
    }

    @Override
    public @NotNull Path getFullFilePath() {
        return fullFilePath;
    }

    @Override
    public @NotNull ConfigSerializer<T> getSerializer() {
        return serializer;
    }

    @Override
    public @NotNull ConfigSerializer<T> getDeserializer() {
        return deserializer;
    }

    @Override
    public @NotNull Class<T> getConfigClass() {
        return clazz;
    }

    @Override
    public @NotNull T getInstance() throws RuntimeException {
        return instance;
    }

    public void setInstance(@NotNull T instance) {
        this.instance = instance;
    }

    @Override
    public Loader.@NotNull Type getLoaderType() {
        return loaderType;
    }

    @Override
    public @NotNull String getLoaderTarget() {
        return loaderTarget;
    }

    @Override
    public boolean isSilentlyFailing() {
        return silentlyFail;
    }

    public void setDefaultLoader() {
        this.loaderType = Loader.Type.DEFAULT;
        this.loaderTarget = "";
        this.deserializer = this.serializer;
    }
}
