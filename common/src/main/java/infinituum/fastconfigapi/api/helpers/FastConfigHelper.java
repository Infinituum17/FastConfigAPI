package infinituum.fastconfigapi.api.helpers;

import com.google.common.base.CaseFormat;
import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.impl.ConfigMetadata;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.void_lib.api.utils.UnsafeLoader;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.util.Map;

public final class FastConfigHelper {
    private FastConfigHelper() {
    }

    public static @NotNull String getFileNameOrDefault(Map<String, Object> data, String className, FastConfig.Side side) {
        String fileName = className;

        if (data.containsKey("fileName") && data.get("fileName") instanceof String name) {
            fileName = name;
        }

        return side.appendTo(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, fileName));
    }
    
    public static <T> @NotNull ConfigSerializer<T> getSerializerOrDefault(Map<String, Object> data) {
        Class<? extends SerializerWrapper<T>> clazz = null;

        if (data.containsKey("serializer")) {
            Object object = data.get("serializer");

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
            clazz = PlatformHelper.getDefaultSerializer();
        }

        return UnsafeLoader.loadInstance(clazz).get();
    }

    public static @NotNull String getSubdirectoryOrDefault(Map<String, Object> data) {
        if (data.containsKey("subdirectory") && data.get("subdirectory") instanceof String subdirectory) {
            return subdirectory;
        }

        return "";
    }

    public static <T> FastConfigFileImpl<T> toFile(Class<T> clazz, Map<String, Object> data, FastConfig.Side expectedSide) throws RuntimeException {
        Class<T> validClass = validateClass(clazz);
        FastConfig.Side currentSide = getSideOrDefault(data);
        ConfigMetadata<T> metadata = new ConfigMetadata<>(clazz);

        if (currentSide.ordinal() != expectedSide.ordinal()) {
            return null;
        }

        FastConfigFileImpl<T> configFile = new FastConfigFileImpl<>(validClass, expectedSide, data, metadata);
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length > 1 || (constructors.length == 1 && constructors[0].getParameterCount() > 0)) {
            throw new RuntimeException("Fast Config class " + clazz.getName() + " should not define any constructors");
        }

        return configFile;
    }

    private static <T> @NotNull Class<T> validateClass(Class<T> clazz) throws RuntimeException {
        if (clazz.isInterface()) {
            throw new RuntimeException("Fast Config class " + clazz.getName() + " can't be an interface");
        }

        return clazz;
    }

    public static @NotNull FastConfig.Side getSideOrDefault(Map<String, Object> data) {
        if (data.containsKey("side")) {
            return PlatformHelper.getPlatformSide(data.get("side"));
        }

        return FastConfig.Side.COMMON;
    }

    public static <T> @NotNull String getModId(Class<T> clazz, @NotNull Map<String, Object> data) {
        if (!data.containsKey("modId")) {
            throw new RuntimeException("Class " + clazz.getSimpleName() + " did not specify a mod-id");
        }

        return (String) data.get("modId");
    }
}
