package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.api.serializers.TOMLSerializer;
import infinituum.fastconfigapi.forge.utils.ConfigScanner;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;

import java.nio.file.Path;
import java.util.Map;

public final class PlatformHelperImpl {
    public static Path getDefaultConfigDirPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        return (Class<? extends SerializerWrapper<T>>) (Class<?>) TOMLSerializer.class;
    }

    public static FastConfig.Side getPlatformSide(Object object) {
        if (object instanceof ModAnnotation.EnumHolder holder) {
            return Enum.valueOf(FastConfig.Side.class, holder.getValue());
        }

        return FastConfig.Side.COMMON;
    }

    public static Loader.Type getPlatformLoaderType(Object object) {
        if (object instanceof ModAnnotation.EnumHolder holder) {
            return Enum.valueOf(Loader.Type.class, holder.getValue());
        }

        return Loader.Type.DEFAULT;
    }

    public static Map<String, Object> getPlatformLoaderData(Object object) {
        return (Map<String, Object>) object;
    }

    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        return ConfigScanner.getSidedConfigs(side);
    }
}
