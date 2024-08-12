package infinituum.fastconfigapi.fabric.scanner;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public final class ModAnnotationScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModAnnotationScanner");
    private static ModAnnotationScanner INSTANCE;
    private final Map<String, List<ModAnnotation>> RESULT;

    private ModAnnotationScanner() {
        Collection<ModContainer> loadedMods = FabricLoader.getInstance().getAllMods();
        this.RESULT = new HashMap<>();

        scan(loadedMods, RESULT);
    }

    public static ModAnnotationScanner init() {
        if (INSTANCE == null) {
            INSTANCE = new ModAnnotationScanner();
        }

        return INSTANCE;
    }

    public Map<String, List<ModAnnotation>> get() {
        return RESULT;
    }

    private void scan(Collection<ModContainer> mods, Map<String, List<ModAnnotation>> result) {
        for (ModContainer mod : mods) {
            String modid = mod.getMetadata().getId();
            List<ModAnnotation> annotations = scanEntrypoints(mod.getRootPaths());

            result.put(modid, annotations);

            if (!mod.getContainedMods().isEmpty()) {
                scan(mod.getContainedMods(), result);
            }
        }
    }

    private List<ModAnnotation> scanEntrypoints(List<Path> entrypoints) {
        List<ModAnnotation> annotations = new ArrayList<>();

        for (Path entrypoint : entrypoints) {
            annotations.addAll(scanAnnotations(entrypoint));
        }

        return annotations;
    }

    private List<ModAnnotation> scanAnnotations(Path entrypoint) {
        ModAnnotationVisitor visitor = new ModAnnotationVisitor(entrypoint);

        try {
            Files.walkFileTree(entrypoint, visitor);
        } catch (IOException e) {
            LOGGER.error("Could not visit classes of entrypoint '{}'", entrypoint);
        }

        return visitor.getAnnotations();
    }

    public static class ModAnnotation {
        private final String annotationName;
        private final Map<String, Object> fields;

        public ModAnnotation(String annotationName) {
            this.annotationName = annotationName;
            this.fields = new HashMap<>();
        }

        public String getAnnotationName() {
            return annotationName;
        }

        public void addField(String name, Object value) {
            fields.put(name, value);
        }

        public record EnumData(String className, String value) {
            public <T extends Enum<T>> T get(Function<String, Class<T>> loadClass) {
                return get(loadClass.apply(className));
            }

            public <T extends Enum<T>> T get(Class<T> clazz) {
                return Enum.valueOf(clazz, value);
            }
        }

        public static class ArrayData {
            private final ArrayList<Object> values;

            public ArrayData() {
                this.values = new ArrayList<>();
            }

            public void add(Object element) {
                this.values.add(element);
            }

            public Object[] get() {
                return values.toArray();
            }
        }
    }
}
