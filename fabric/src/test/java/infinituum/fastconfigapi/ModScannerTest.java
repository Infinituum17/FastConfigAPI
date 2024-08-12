package infinituum.fastconfigapi;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner;
import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModScannerTest {
    @Test
    public void testScan() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method scanEntrypoints = ModAnnotationScanner.class.getDeclaredMethod("scanEntrypoints", List.class);
        scanEntrypoints.setAccessible(true);

        List<Path> paths = new ArrayList<>();
        String cwd = System.getProperty("user.dir");
        Path testingPath = Path.of(cwd).resolve("build/classes/java/test");

        paths.add(testingPath);

        List<ModAnnotation> annotations = (List<ModAnnotation>) scanEntrypoints.invoke(ModAnnotationScanner.init(), paths);

        for (var annotation : annotations) {
            System.out.println(annotation.getAnnotationName()); // TODO: print origin class
        }
    }
}
