package infinituum.fastconfigapi;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import infinituum.fastconfigapi.utils.Utils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static infinituum.fastconfigapi.utils.Utils.setupEnv;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModScannerTest {
    @Test
    public void canSetupEnvironment() {
        assertDoesNotThrow(Utils::setupEnv);
    }

    @Test
    public void testNoParam() {
        Map<String, List<ModAnnotation>> annotatedClasses;

        try {
            annotatedClasses = setupEnv();
        } catch (Exception e) {
            throw new RuntimeException("Cannot setup environment: ", e);
        }

        // Test: AnnotationNoParams should be present and AnnotationNoParams.NoParam.isEmpty should be true
        boolean classExists = false;
        boolean annotationExists = false;
        boolean isEmpty = false;

        for (var classEntry : annotatedClasses.entrySet()) {
            if (classEntry.getKey().endsWith("AnnotationNoParams")) {
                classExists = true;

                for (var annotation : classEntry.getValue()) {
                    if (annotation.getAnnotationName().endsWith("NoParam")) {
                        annotationExists = true;

                        if (annotation.getFields().isEmpty()) {
                            isEmpty = true;
                        }
                    }
                }
            }
        }

        assertTrue(classExists, "Annotation NoParam is not present");
        assertTrue(annotationExists, "Annotation NoParam is not present");
        assertTrue(isEmpty, "AnnotationNoParams.value.isEmpty() is false");
    }

    @Test
    public void testOneString() {
        Map<String, List<ModAnnotation>> annotatedClasses;

        try {
            annotatedClasses = setupEnv();
        } catch (Exception e) {
            throw new RuntimeException("Cannot setup environment: ", e);
        }

        // Test: AnnotationOneString and a string field should be present
        boolean classExists = false;
        boolean annotationExists = false;
        boolean hasField = false;

        for (var classEntry : annotatedClasses.entrySet()) {
            if (classEntry.getKey().endsWith("AnnotationOneString")) {
                classExists = true;

                for (var annotation : classEntry.getValue()) {
                    if (annotation.getAnnotationName().endsWith("OneString")) {
                        annotationExists = true;

                        if (annotation.getFields().containsKey("str")) {
                            hasField = true;
                        }
                    }
                }
            }
        }

        assertTrue(classExists, "Annotation OneString is not present");
        assertTrue(annotationExists, "Annotation OneString is not present");
        assertTrue(hasField, "Annotation OneString hasField is false");
    }

    @Test
    public void testInnerClass() {
        Map<String, List<ModAnnotation>> annotatedClasses;

        try {
            annotatedClasses = setupEnv();
        } catch (Exception e) {
            throw new RuntimeException("Cannot setup environment: ", e);
        }

        boolean classExists = false;
        boolean annotationExists = false;

        for (var classEntry : annotatedClasses.entrySet()) {
            if (classEntry.getKey().endsWith("AnnotatedInnerClass")) {
                classExists = true;

                for (var annotation : classEntry.getValue()) {
                    if (annotation.getAnnotationName().endsWith("NoParam")) {
                        annotationExists = true;
                        break;
                    }
                }
            }
        }

        assertTrue(classExists, "Annotation OneString is not present");
        assertTrue(annotationExists, "Annotation OneString is not present");
    }
}
