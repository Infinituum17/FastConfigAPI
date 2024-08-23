package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner;
import infinituum.fastconfigapi.fabric.scanner.ModAnnotationVisitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final String cwd = System.getProperty("user.dir");
    private static final boolean debug = true;

    public static void logAnnotatedClasses(Map<String, List<ModAnnotationScanner.ModAnnotation>> annotatedClasses) {
        System.out.println();
        for (var annotationEntry : annotatedClasses.entrySet()) {
            System.out.println("Class: " + annotationEntry.getKey());
            for (var annotation : annotationEntry.getValue()) {
                System.out.println(" > Annotated Element: " + annotation.getAnnotatedElementName());
                System.out.println(" > Annotation Name: " + annotation.getAnnotationName());
                if (!annotation.getFields().isEmpty()) {
                    System.out.println(" > Fields: ");
                    for (var entry : annotation.getFields().entrySet()) {
                        System.out.println("    - " + entry.getKey() + " : " + entry.getValue());
                    }
                }
            }
            System.out.println();
        }
    }

    public static Map<String, List<ModAnnotationScanner.ModAnnotation>> setupEnv() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method scanEntrypoints = ModAnnotationScanner.class.getDeclaredMethod("scanAnnotations", ModAnnotationVisitor.class);
        scanEntrypoints.setAccessible(true);

        Path testingPath = Path.of(cwd).resolve("build/classes/java/test");
        ModAnnotationVisitor visitor = new ModAnnotationVisitor(testingPath, "infinituum.fastconfigapi.examples.annotated_classes");

        Map<String, List<ModAnnotationScanner.ModAnnotation>> annotatedClasses = (Map<String, List<ModAnnotationScanner.ModAnnotation>>) scanEntrypoints.invoke(ModAnnotationScanner.init(), visitor);

        if (debug) {
            logAnnotatedClasses(annotatedClasses);
        }

        return annotatedClasses;
    }
}
