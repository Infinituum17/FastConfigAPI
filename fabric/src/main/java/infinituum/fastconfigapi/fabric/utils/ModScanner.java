package infinituum.fastconfigapi.fabric.utils;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.config.FastConfigFile;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Tuple;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static infinituum.fastconfigapi.api.FastConfigs.MOD_ID;

public class ModScanner {
    public static <T> Map<Class<T>, FastConfigFile<T>> getSidedConfigs(FastConfig.Side side) {
        List<ModContainer> modList = new ArrayList<>();

        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            filterMods(container, modList);
        }

        List<Class<T>> classList = new ArrayList<>();

        for (ModContainer container : modList) {
            scanJar(container, classList);
        }

        return classList
                .stream()
                .filter(clazz -> clazz.getDeclaredAnnotations().length > 0)
                .filter(clazz -> clazz.getDeclaredAnnotation(FastConfig.class) != null)
                .map(clazz -> new Tuple<>(clazz, clazz.getDeclaredAnnotation(FastConfig.class)))
                .map(tuple -> {
                    String fileName = tuple.getB().fileName();
                    Class<? extends SerializerWrapper<T>> serializerWrapper = (Class<? extends SerializerWrapper<T>>) tuple.getB().serializer();
                    FastConfig.Side configSide = tuple.getB().side();

                    if (serializerWrapper.isInterface()) {
                        serializerWrapper = (Class<? extends SerializerWrapper<T>>) PlatformHelper.getDefaultSerializer();
                    }

                    if (configSide.ordinal() == side.ordinal()) {
                        return new Tuple<>(tuple.getA(), new FastConfigFile<>(tuple.getA(), fileName, serializerWrapper, side));
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Tuple::getA, Tuple::getB));
    }

    public static <T> void scanJar(ModContainer container, List<Class<T>> classList) {
        List<Path> rootPaths = container.getRootPaths();

        // Workaround to make it work (partially)
        if (container.getMetadata().getId().equals("fastconfigapi") && FabricLoader.getInstance().isDevelopmentEnvironment()) {
            rootPaths = List.of(
                    rootPaths.get(0)
                            .getParent()
                            .getParent()
                            .resolve("classes")
                            .resolve("java")
                            .resolve("main")
            );
        }

        for (Path rootPath : rootPaths) {
            try {
                ModVisitor<T> modVisitor = new ModVisitor<T>(rootPath);

                Files.walkFileTree(rootPath, modVisitor);

                classList.addAll(modVisitor.getClasses());
            } catch (Exception ignored) {
            }
        }
    }

    public static void filterMods(ModContainer container, List<ModContainer> modList) {
        boolean isAPI = container.getMetadata().getId().equals(MOD_ID);
        boolean isAPIDependent = container
                .getMetadata()
                .getDependencies()
                .stream()
                .anyMatch(dependency -> dependency
                        .getModId()
                        .equals(MOD_ID));

        if (isAPI || isAPIDependent) {
            modList.add(container);
        }

        if (!container.getContainedMods().isEmpty()) {
            for (ModContainer containedMod : container.getContainedMods()) {
                filterMods(containedMod, modList);
            }
        }
    }
}
