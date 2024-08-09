package infinituum.fastconfigapi;

import dev.architectury.injectables.annotations.ExpectPlatform;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;

import java.nio.file.Path;

public class PlatformHelper {
    @ExpectPlatform
    public static Path getDefaultConfigDirPath() {
        throw new AssertionError();
    }

    public static Path getDefaultConfigDirPath(String path) {
        return getDefaultConfigDirPath().resolve(path);
    }

    @ExpectPlatform
    public static <T> Class<? extends SerializerWrapper<T>> getDefaultSerializer() {
        throw new AssertionError();
    }
}
