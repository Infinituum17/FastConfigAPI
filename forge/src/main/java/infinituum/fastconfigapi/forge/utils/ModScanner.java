package infinituum.fastconfigapi.forge.utils;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.config.FastConfigFile;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.api.utils.Global;
import infinituum.fastconfigapi.api.utils.UnsafeLoader;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation.EnumHolder;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static infinituum.fastconfigapi.api.FastConfigs.MOD_ID;

public final class ModScanner {
    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        return ModList.get()
                .getModFiles()
                .stream()
                .filter(ModScanner::dependents)
                .map(ModScanner::getAnnotationScanResult)
                .flatMap(ModScanner::getValidAnnotations)
                .map(annotation -> ModScanner.<T>toTuple(annotation, side))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Tuple::getA, Tuple::getB));
    }

    private static <T> Tuple<Class<T>, FastConfigFile<T>> toTuple(AnnotationData annotation, FastConfig.Side side) {
        Class<T> clazz = getClass(annotation);

        if (clazz == null) {
            return null;
        }

        FastConfigFile<T> configFile = getFile(annotation, clazz);

        if (configFile == null || configFile.getSide().ordinal() != side.ordinal()) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length > 1 || (constructors.length == 1 && constructors[0].getParameterCount() > 0)) {
            throw new RuntimeException("Fast Config class " + clazz.getName() + " should not define any constructors.");
        }

        Global.LOGGER.info("Config '{}' was successfully loaded", configFile.getFileName());

        return new Tuple<>(clazz, configFile);

    }

    private static Stream<AnnotationData> getValidAnnotations(ModFileScanData mod) {
        return mod.getAnnotations()
                .stream()
                .filter(ModScanner::hasAnnotation);
    }

    private static boolean hasAnnotation(AnnotationData annotation) {
        String annDescriptor = annotation.annotationType().getDescriptor();

        return annDescriptor.equals(FastConfig.class.descriptorString());
    }

    private static boolean dependents(IModFileInfo mod) {
        return mod.getMods()
                .stream()
                .anyMatch(modInfo -> modInfo
                        .getDependencies()
                        .stream()
                        .anyMatch(dependencies -> dependencies
                                .getModId()
                                .equals(MOD_ID))
                        || modInfo.getModId().equals(MOD_ID));
    }

    private static ModFileScanData getAnnotationScanResult(IModFileInfo mod) {
        return mod.getFile().getScanResult();
    }

    private static <T> Class<T> getClass(AnnotationData data) {
        String className = data.clazz().getClassName();
        Class<T> result = UnsafeLoader.loadClass(className, FastConfig.class.getClassLoader());

        if (result == null) {
            Global.LOGGER.error("Could not load class {}", className);
        }

        return result;
    }

    private static <T> FastConfigFile<T> getFile(AnnotationData data, Class<T> clazz) {
        Map<String, Object> annotationData = data.annotationData();

        try {
            var fileName = Objects.requireNonNull(getFileName(annotationData.get("fileName")));
            var serializer = Objects.requireNonNull(ModScanner.<T>getSerializer(annotationData.get("serializer")));
            var side = Objects.requireNonNull(getSide(annotationData.get("side")));

            return new FastConfigFile<>(clazz, fileName, serializer, side);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getFileName(Object obj) {
        if (obj instanceof String name) {
            return name;
        }

        return null;
    }

    private static FastConfig.Side getSide(Object obj) {
        if (obj instanceof EnumHolder holder) {
            return Enum.valueOf(FastConfig.Side.class, holder.getValue());
        }

        return null;
    }

    private static <T> Class<? extends SerializerWrapper<T>> getSerializer(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Type serType) {
            String className = serType.getClassName();
            Class<? extends SerializerWrapper<T>> loadedClass = UnsafeLoader.loadClass(className);

            if (loadedClass == null) {
                Global.LOGGER.error("Could not parse serializer {}", className);
                return null;
            }

            return loadedClass;
        }

        return PlatformHelper.getDefaultSerializer();
    }
}
