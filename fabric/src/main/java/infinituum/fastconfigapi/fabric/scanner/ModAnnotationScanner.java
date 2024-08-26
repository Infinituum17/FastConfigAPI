package infinituum.fastconfigapi.fabric.scanner;

import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedClass;
import infinituum.fastconfigapi.fabric.scanner.impl.ScannedModFile;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ModAnnotationScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModAnnotationScanner");
    private static ModAnnotationScanner INSTANCE;
    private final Set<ScannedModFile> result;

    private ModAnnotationScanner() {
        Collection<ModContainer> loadedMods = FabricLoader.getInstance().getAllMods();
        this.result = scan(loadedMods);
    }

    public static ModAnnotationScanner init() {
        if (INSTANCE == null) {
            INSTANCE = new ModAnnotationScanner();
        }

        return INSTANCE;
    }

    public Set<ScannedModFile> get() {
        return result;
    }

    private Set<ScannedModFile> scan(Collection<ModContainer> mods) {
        Set<ScannedModFile> result = new HashSet<>();

        for (ModContainer mod : mods) {
            Set<AnnotatedClass> classes = scanEntrypoints(mod.getRootPaths());
            ScannedModFile file = new ScannedModFile(mod, classes);

            result.add(file);

            if (!mod.getContainedMods().isEmpty()) {
                scan(mod.getContainedMods());
            }
        }

        return result;
    }

    private Set<AnnotatedClass> scanEntrypoints(List<Path> entrypoints) {
        Set<AnnotatedClass> annotatedClasses = new HashSet<>();

        for (Path entrypoint : entrypoints) {
            ModAnnotationFileTreeVisitor visitor = new ModAnnotationFileTreeVisitor(entrypoint);

            annotatedClasses.addAll(scanAnnotations(visitor));
        }

        return Collections.unmodifiableSet(annotatedClasses);
    }

    private Set<AnnotatedClass> scanAnnotations(ModAnnotationFileTreeVisitor visitor) {
        try {
            Files.walkFileTree(visitor.getBasePath(), visitor);
        } catch (IOException e) {
            LOGGER.error("Could not visit classes of entrypoint '{}'", visitor.getBasePath());
        }

        return visitor.getAnnotatedClasses();
    }
}
