package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.apiV2.serializers.SerializerWrapper;
import infinituum.fastconfigapi.apiV2.serializers.TOMLSerializer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getDefaultConfigDirPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        return (Class<? extends SerializerWrapper<T>>) (Class<?>) TOMLSerializer.class;
    }
}
