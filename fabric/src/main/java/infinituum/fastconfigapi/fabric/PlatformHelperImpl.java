package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.apiV2.serializers.JSONSerializer;
import infinituum.fastconfigapi.apiV2.serializers.SerializerWrapper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getDefaultConfigDirPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        return (Class<? extends SerializerWrapper<T>>) (Class<?>) JSONSerializer.class;
    }
}
