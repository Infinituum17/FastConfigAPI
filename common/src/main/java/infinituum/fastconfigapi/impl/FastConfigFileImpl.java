package infinituum.fastconfigapi.impl;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.helpers.FastConfigHelper;
import infinituum.fastconfigapi.api.helpers.LoaderHelper;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.utils.Global;
import infinituum.void_lib.api.utils.UnsafeLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Actual implementation of the interface {@link FastConfigFile}.
 *
 * @param <T> The type of the class instance contained in the current config file.
 */
public final class FastConfigFileImpl<T> implements infinituum.fastconfigapi.api.FastConfigFile<T> {
    private final Class<T> clazz;
    private final FastConfig.Side side;
    private final String fileName;
    private final String subdirectoryName;
    private final Path configDirectoryPath;
    private final Path configSubdirectoryPath;
    private final Path fullFilePath;
    private final SerializerWrapper<T> serializer;
    private final boolean silentlyFail;
    private CompletableFuture<HttpResponse<String>> pendingRequest;
    private Loader.Type loader;
    private String loaderTarget;
    private T instance;

    public FastConfigFileImpl(@NotNull Class<T> clazz, @NotNull FastConfig.Side side, @NotNull Map<String, Object> data) {
        this.clazz = clazz;
        this.side = side;
        this.fileName = FastConfigHelper.getFileNameOrDefault(data, clazz.getSimpleName(), side);
        this.serializer = FastConfigHelper.getSerializerOrDefault(data);
        this.subdirectoryName = FastConfigHelper.getSubdirectoryOrDefault(data);
        this.configDirectoryPath = PlatformHelper.getDefaultConfigDirPath();
        this.configSubdirectoryPath = (!subdirectoryName.isEmpty()) ? configDirectoryPath.resolve(subdirectoryName) : configDirectoryPath;
        this.fullFilePath = this.configSubdirectoryPath.resolve(this.getFileNameWithExtension());
        this.loader = LoaderHelper.getLoaderOrDefault(data);
        this.loaderTarget = LoaderHelper.getTargetOrDefault(data);
        this.silentlyFail = LoaderHelper.getSilentlyFailOrDefault(data);
        this.pendingRequest = null;

        this.load();
    }

    @Override
    public void loadDefault() {
        this.loader.load(this);
    }

    @Override
    public void load() {
        try {
            serializer.get().deserialize(this);
            Global.LOGGER.info("Config '{}' was successfully loaded", this.getFileNameWithExtension());
        } catch (Exception e) {
            try {
                this.loadDefault();
            } catch (Exception e2) {
                throw new RuntimeException("Config '" + getFileNameWithExtension() + "' could not be loaded");
            }
        }
    }

    @Override
    public void loadStateUnsafely(String content) throws IOException {
        serializer.get().deserialize(this, new StringReader(content));
    }

    @Override
    public void save() {
        if (pendingRequest == null) {
            try {
                Files.createDirectories(this.getConfigSubdirectoryPath());

                serializer.get().serialize(this);

                return;
            } catch (Exception e) {
                throw new RuntimeException("Contents of Config '" + getFileNameWithExtension() + "' could not be saved");
            }
        }

        throw new RuntimeException("Config HttpRequest is still pending, waiting until timeout...");
    }

    public void setDefaultClassInstance() {
        this.setInstance(UnsafeLoader.loadInstance(clazz));
    }

    @Override
    public @NotNull String getFileNameWithExtension() {
        return fileName + "." + this.serializer.get().getFileExtension();
    }

    @Override
    public @NotNull Path getConfigSubdirectoryPath() {
        return configSubdirectoryPath;
    }

    @Override
    public @NotNull String getConfigSubdirectoryName() {
        return subdirectoryName;
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
        return serializer.get();
    }

    @Override
    public @NotNull Class<T> getConfigClass() {
        return clazz;
    }

    @Override
    public @NotNull T getInstance() throws RuntimeException {
        if (pendingRequest == null) {
            return instance;
        }

        throw new RuntimeException("Config HttpRequest is still pending, waiting until timeout...");
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    @Override
    public Loader.@NotNull Type getLoaderType() {
        return loader;
    }

    public void setLoaderType(Loader.Type loader) {
        this.loader = loader;
    }

    @Override
    public @NotNull String getLoaderTarget() {
        return loaderTarget;
    }

    public void setLoaderTarget(String loaderTarget) {
        this.loaderTarget = loaderTarget;
    }

    @Override
    public boolean isSilentlyFailing() {
        return silentlyFail;
    }

    public void setPendingRequest(CompletableFuture<HttpResponse<String>> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }
}
