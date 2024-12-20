package infinituum.fastconfigapi.neoforge.utils;

import infinituum.fastconfigapi.FastConfigAPI;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.helpers.FastConfigHelper;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.void_lib.api.utils.UnsafeLoader;
import net.minecraft.util.Tuple;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ConfigScanner {
    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        return ModList.get()
                .getModFiles()
                .stream()
                .filter(ConfigScanner::dependents)
                .map(ConfigScanner::getAnnotationScanResult)
                .flatMap(ConfigScanner::getValidAnnotations)
                .map(annotation -> ConfigScanner.<T>toTuple(annotation, side))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Tuple::getA, Tuple::getB));
    }

    private static boolean dependents(IModFileInfo mod) {
        return mod.getMods()
                .stream()
                .anyMatch(modInfo -> modInfo
                        .getDependencies()
                        .stream()
                        .anyMatch(dependency -> MOD_ID.equals(dependency.getModId()))
                        || MOD_ID.equals(modInfo.getModId())
                        || modInfo.getModId().startsWith("generated_"));
    }

    private static ModFileScanData getAnnotationScanResult(IModFileInfo mod) {
        return mod.getFile().getScanResult();
    }

    private static Stream<ModFileScanData.AnnotationData> getValidAnnotations(ModFileScanData mod) {
        return mod.getAnnotations()
                .stream()
                .filter(ConfigScanner::hasAnnotation);
    }

    private static <T> Tuple<Class<T>, FastConfigFileImpl<T>> toTuple(ModFileScanData.AnnotationData annotation, FastConfig.Side side) {
        Class<T> clazz = getClass(annotation);

        if (clazz == null) {
            return null;
        }

        FastConfigFileImpl<T> configFile;

        try {
            configFile = FastConfigHelper.toFile(clazz, annotation.annotationData(), side);
        } catch (Exception e) {
            FastConfigAPI.LOGGER.error("Could not load config class {}: {}", clazz.getName(), e);
            return null;
        }

        if (configFile == null) {
            return null;
        }

        return new Tuple<>(clazz, configFile);
    }

    private static boolean hasAnnotation(ModFileScanData.AnnotationData annotation) {
        String annDescriptor = annotation.annotationType().getDescriptor();

        return annDescriptor.equals(FastConfig.class.descriptorString());
    }

    private static <T> Class<T> getClass(ModFileScanData.AnnotationData data) {
        String className = data.clazz().getClassName();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Class<T> result = UnsafeLoader.loadClassNoInit(className, contextClassLoader);

        if (result == null) {
            FastConfigAPI.LOGGER.error("Could not load class {}", className);
        }

        return result;
    }
}
