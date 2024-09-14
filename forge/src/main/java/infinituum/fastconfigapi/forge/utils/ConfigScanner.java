package infinituum.fastconfigapi.forge.utils;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.helpers.FastConfigHelper;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.fastconfigapi.utils.Global;
import infinituum.void_lib.api.utils.UnsafeLoader;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

public final class ConfigScanner {
    public static <T> Map<Class<T>, FastConfigFileImpl<T>> getSidedConfigs(FastConfig.Side side) {
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

    private static <T> Tuple<Class<T>, FastConfigFileImpl<T>> toTuple(AnnotationData annotation, FastConfig.Side side) {
        Class<T> clazz = getClass(annotation);

        if (clazz == null) {
            return null;
        }

        FastConfigFileImpl<T> configFile;

        try {
            configFile = FastConfigHelper.toFile(clazz, annotation.annotationData(), side);
        } catch (Exception e) {
            Global.LOGGER.error("Could not load config class {}: {}", clazz.getName(), e);
            return null;
        }

        if (configFile == null) {
            return null;
        }

        Global.LOGGER.info("Config '{}' was successfully loaded", configFile.getFileName());

        return new Tuple<>(clazz, configFile);
    }

    private static Stream<AnnotationData> getValidAnnotations(ModFileScanData mod) {
        return mod.getAnnotations()
                .stream()
                .filter(ConfigScanner::hasAnnotation);
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
        Class<T> result = UnsafeLoader.loadClassNoInit(className, FastConfig.class.getClassLoader());

        if (result == null) {
            Global.LOGGER.error("Could not load class {}", className);
        }

        return result;
    }
}
