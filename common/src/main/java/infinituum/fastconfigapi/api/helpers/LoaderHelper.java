package infinituum.fastconfigapi.api.helpers;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.annotations.Loader;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.void_lib.api.utils.UnsafeLoader;
import org.objectweb.asm.Type;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public final class LoaderHelper {
    private static Map<String, Object> getAnnotationFields(Map<String, Object> data) {
        if (data.containsKey("loader")) {
            return PlatformHelper.getPlatformLoaderData(data.get("loader"));
        }

        return null;
    }

    public static Loader.Type getLoaderOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("type")) {
            return PlatformHelper.getPlatformLoaderType(innerData.get("type"));
        }

        return Loader.Type.DEFAULT;
    }

    public static String getTargetOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("target") && innerData.get("target") instanceof String target) {
            return target;
        }

        return "";
    }

    public static boolean getSilentlyFailOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("silentlyFail") && innerData.get("silentlyFail") instanceof Boolean silentlyFail) {
            return silentlyFail;
        }

        return false;
    }

    public static boolean isCurrentDeserializerOriginal(Path filePath) {
        try {
            File file = filePath.toFile();

            if (file.exists()) {
                return false;
            }
        } catch (Exception ignored) {
        }

        return true;
    }

    public static <T> ConfigSerializer<T> getOriginalDeserializerOrDefault(Map<String, Object> data, ConfigSerializer<T> currentSerializer, Loader.Type loaderType) {
        if (loaderType.ordinal() == Loader.Type.DEFAULT.ordinal()) {
            return currentSerializer;
        }

        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData == null) {
            return currentSerializer;
        }

        Class<? extends SerializerWrapper<T>> clazz = null;

        if (innerData.containsKey("deserializer")) {
            Object object = innerData.get("deserializer");

            if (object instanceof Type type) {
                clazz = UnsafeLoader.loadClass(type.getClassName());
            } else if (object != null) {
                try {
                    clazz = (Class<? extends SerializerWrapper<T>>) object;
                } catch (Exception ignored) {
                }
            }
        }

        if (clazz == null) {
            return currentSerializer;
        }

        return UnsafeLoader.loadInstance(clazz).get();
    }
}
