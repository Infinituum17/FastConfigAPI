package infinituum.fastconfigapi;

import dev.architectury.injectables.annotations.ExpectPlatform;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;

import java.nio.file.Path;
import java.util.Map;

public final class PlatformHelper {
    @ExpectPlatform
    public static Path getDefaultConfigDirPath() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FastConfig.Side getPlatformSide(Object object) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Map<String, Object> getPlatformLoaderData(Object object) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Loader.Type getPlatformLoaderType(Object object) {
        throw new AssertionError();
    }
}
