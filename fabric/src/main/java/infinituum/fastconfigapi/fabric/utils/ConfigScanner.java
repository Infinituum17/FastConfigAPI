package infinituum.fastconfigapi.fabric.utils;

import infinituum.fastconfigapi.api.config.FastConfigFile;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.void_lib.fabric.scanner.ModAnnotationScanner;
import infinituum.void_lib.fabric.scanner.api.ScannedFile;
import infinituum.void_lib.fabric.scanner.impl.AnnotationData.EnumValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static infinituum.fastconfigapi.api.FastConfigs.MOD_ID;

public class ConfigScanner {
    private static volatile Set<ScannedFile> scannedFiles;

    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        ModAnnotationScanner scanner = ModAnnotationScanner.init();
        Map<Class<T>, FastConfigFile<T>> result = new HashMap<>();

        if (scannedFiles == null) {
            scannedFiles = scanner.search(FastConfig.class, MOD_ID);
        }

        for (var file : scannedFiles) {
            for (var annotatedClass : file.getAnnotatedClasses()) {
                if (!annotatedClass.hasClassAnnotations()) {
                    continue;
                }

                for (var annotation : annotatedClass.getAnnotations()) {
                    if (!annotation.is(FastConfig.class)) {
                        continue;
                    }

                    String fileName = "";
                    Class<? extends SerializerWrapper<T>> serializerWrapper = (Class<? extends SerializerWrapper<T>>) (Class<?>) SerializerWrapper.class;
                    FastConfig.Side configSide = FastConfig.Side.COMMON;

                    if (annotation.containsField("fileName")) {
                        fileName = (String) annotation.getField("fileName");
                    }

                    if (annotation.containsField("serializer")) {
                        serializerWrapper = (Class<? extends SerializerWrapper<T>>) annotation.getField("serializer");
                    }

                    if (annotation.containsField("side")) {
                        configSide = ((EnumValue<FastConfig.Side>) annotation.getField("side")).get(FastConfig.Side.class);
                    }

                    if (configSide.ordinal() == side.ordinal()) {
                        Class<T> configClass = (Class<T>) annotatedClass.get();
                        result.put(configClass, new FastConfigFile<>(
                                configClass,
                                fileName,
                                serializerWrapper,
                                configSide
                        ));
                    }
                }
            }
        }

        return result;
    }
}