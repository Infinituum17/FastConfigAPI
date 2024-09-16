package infinituum.fastconfigapi.fabric.utils;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.helpers.FastConfigHelper;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.fastconfigapi.utils.Global;
import infinituum.void_lib.api.utils.UnsafeLoader;
import infinituum.void_lib.fabric.scanner.ModAnnotationScanner;
import infinituum.void_lib.fabric.scanner.api.AnnotatedClass;
import infinituum.void_lib.fabric.scanner.api.ScannedFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

public final class ConfigScanner {
    private static volatile Set<ScannedFile> scannedFiles;

    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        ModAnnotationScanner scanner = ModAnnotationScanner.init();
        Map<Class<T>, FastConfigFile<T>> result = new HashMap<>();

        if (scannedFiles == null) {
            scannedFiles = scanner.search(FastConfig.class, MOD_ID);
        }

        for (ScannedFile file : scannedFiles) {
            analyzeFile(file, side, result);
        }

        return result;
    }

    public static <T> void analyzeFile(ScannedFile file, FastConfig.Side side, Map<Class<T>, FastConfigFile<T>> result) {
        for (AnnotatedClass annotatedClass : file.getAnnotatedClasses()) {
            if (!annotatedClass.hasClassAnnotations()) {
                continue;
            }

            analyzeAnnotations(annotatedClass, side, result);
        }
    }

    public static <T> void analyzeAnnotations(AnnotatedClass annotatedClass, FastConfig.Side side, Map<Class<T>, FastConfigFile<T>> result) {
        for (var annotation : annotatedClass.getAnnotations()) {
            if (!annotation.is(FastConfig.class)) {
                continue;
            }

            Class<T> clazz = UnsafeLoader.loadClassNoInit(annotatedClass.getName(), FastConfig.class.getClassLoader());

            if (clazz == null) {
                continue;
            }

            FastConfigFileImpl<T> configFile;

            try {
                configFile = FastConfigHelper.toFile(clazz, annotation.getFields(), side);
            } catch (Exception e) {
                Global.LOGGER.error("Could not load config class {}: {}", clazz.getName(), e);
                continue;
            }

            if (configFile == null) {
                continue;
            }

            result.put(clazz, configFile);
        }
    }
}