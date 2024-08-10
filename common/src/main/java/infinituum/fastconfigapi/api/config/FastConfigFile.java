package infinituum.fastconfigapi.api.config;


import com.google.common.base.CaseFormat;
import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.api.utils.UnsafeLoader;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class FastConfigFile<T> implements ConfigFile {
    private final Class<T> CLASS;
    private final FastConfig.Side SIDE;
    private final String FILE_NAME;
    private final Path FILE_PATH;
    private final SerializerWrapper<T> SERIALIZER;

    private T instance;

    public FastConfigFile(@NotNull Class<T> clazz, @NotNull String fileName, @NotNull Class<? extends SerializerWrapper<T>> serializerWrapper, @NotNull FastConfig.Side side) {
        Class<? extends SerializerWrapper<T>> platDefault = PlatformHelper.getDefaultSerializer();

        this.CLASS = clazz;
        this.SIDE = side;

        if (fileName == null || fileName.isEmpty()) {
            fileName = this.CLASS.getSimpleName();
        }

        this.FILE_NAME = side.appendTo(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName));

        if (serializerWrapper.isInterface()) {
            this.SERIALIZER = UnsafeLoader.loadInstance(platDefault);
        } else {
            this.SERIALIZER = UnsafeLoader.loadInstance(serializerWrapper);
        }

        this.FILE_PATH = PlatformHelper.getDefaultConfigDirPath(this.getFileNameWithExtension());

        this.load();
    }

    @Override
    public void loadDefault() {
        this.setInstance(UnsafeLoader.loadInstance(CLASS));
        this.save();
    }

    @Override
    public void load() {
        try {
            SERIALIZER.get().deserialize(this);
        } catch (Exception e) {
            try {
                this.loadDefault();
            } catch (Exception e2) {
                throw new RuntimeException("Config '" + getFileNameWithExtension() + "' could not be loaded");
            }
        }
    }

    @Override
    public void save() {
        try {
            SERIALIZER.get().serialize(this);
        } catch (Exception e) {
            throw new RuntimeException("Contents of Config '" + getFileNameWithExtension() + "' could not be saved");
        }
    }

    public String getFileNameWithExtension() {
        return FILE_NAME + "." + this.SERIALIZER.get().getFileExtension();
    }

    public String getFileName() {
        return FILE_NAME;
    }

    public FastConfig.Side getSide() {
        return SIDE;
    }

    public Path getFullPath() {
        return FILE_PATH;
    }

    public Class<T> getConfigClass() {
        return CLASS;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }
}
