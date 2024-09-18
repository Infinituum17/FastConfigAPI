package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.serializers.JSONSerializer;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import infinituum.void_lib.fabric.scanner.impl.AnnotationData;
import infinituum.void_lib.fabric.scanner.impl.ModAnnotation;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.Map;

public final class PlatformHelperImpl {
    public static Path getDefaultConfigDirPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        return (Class<? extends SerializerWrapper<T>>) (Class<?>) JSONSerializer.class;
    }

    public static FastConfig.Side getPlatformSide(Object object) {
        return ((AnnotationData.EnumValue<FastConfig.Side>) object).get(FastConfig.Side.class);
    }

    public static Loader.Type getPlatformLoaderType(Object object) {
        return ((AnnotationData.EnumValue<Loader.Type>) object).get(Loader.Type.class);
    }

    public static Map<String, Object> getPlatformLoaderData(Object object) {
        ModAnnotation data = (ModAnnotation) object;

        return data.getFields();
    }

    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        return ConfigScanner.getSidedConfigs(side);
    }
}
