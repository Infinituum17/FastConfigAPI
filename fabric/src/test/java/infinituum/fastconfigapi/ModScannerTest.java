package infinituum.fastconfigapi;

import infinituum.fastconfigapi.examples.annotated_classes.AnnotationInnerClass;
import infinituum.fastconfigapi.examples.annotated_classes.AnnotationNoParams;
import infinituum.fastconfigapi.examples.annotated_classes.AnnotationOneString;
import infinituum.fastconfigapi.examples.annotations.NoParam;
import infinituum.fastconfigapi.examples.annotations.OneString;
import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedClass;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static infinituum.fastconfigapi.utils.Utils.setupEnv;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModScannerTest {
    private final static Set<AnnotatedClass> annotatedClasses;

    static {
        try {
            annotatedClasses = setupEnv();
        } catch (Exception e) {
            throw new RuntimeException("Cannot setup environment: ", e);
        }
    }

    @Test
    public void testNoParam() {
        // Test: AnnotationNoParams should be present and AnnotationNoParams.NoParam.isEmpty should be true
        boolean classExists = false;
        boolean annotationExists = false;
        boolean isEmpty = false;

        for (var annotatedClass : annotatedClasses) {
            if (annotatedClass.is(AnnotationNoParams.class)) {
                classExists = true;

                for (var annotation : annotatedClass.getAnnotations()) {
                    if (annotation.is(NoParam.class)) {
                        annotationExists = true;

                        if (!annotation.hasFields()) {
                            isEmpty = true;
                        }
                    }
                }
            }
        }

        assertTrue(classExists, "ModAnnotation NoParam is not present");
        assertTrue(annotationExists, "ModAnnotation NoParam is not present");
        assertTrue(isEmpty, "AnnotationNoParams.value.isEmpty() is false");
    }

    @Test
    public void testOneString() {
        // Test: AnnotationOneString and a string field should be present
        boolean classExists = false;
        boolean annotationExists = false;
        boolean hasField = false;

        for (var annotatedClass : annotatedClasses) {
            if (annotatedClass.is(AnnotationOneString.class)) {
                classExists = true;

                for (var annotation : annotatedClass.getAnnotations()) {
                    if (annotation.is(OneString.class)) {
                        annotationExists = true;

                        if (annotation.containsField("str")) {
                            hasField = true;
                        }
                    }
                }
            }
        }

        assertTrue(classExists, "ModAnnotation OneString is not present");
        assertTrue(annotationExists, "ModAnnotation OneString is not present");
        assertTrue(hasField, "ModAnnotation OneString hasField is false");
    }

    @Test
    public void testInnerClass() {
        boolean innerClassExists = false;
        boolean innerClassHasAnnotation = false;
        boolean nestedInnerClassExists = false;
        boolean nestedInnerClassHasAnnotation = false;
        boolean nestedInnerClassHasAnnotationField = false;

        for (var annotatedClass : annotatedClasses) {
            if (annotatedClass.is(AnnotationInnerClass.AnnotatedInnerClass.class)) {
                innerClassExists = true;

                for (var annotation : annotatedClass.getAnnotations()) {
                    if (annotation.is(NoParam.class)) {
                        innerClassHasAnnotation = true;
                    }
                }
            }

            if (annotatedClass.is(AnnotationInnerClass.AnnotatedInnerClass.AnnotatedNestedInnerClass.class)) {
                nestedInnerClassExists = true;

                for (var annotation : annotatedClass.getAnnotations()) {
                    if (annotation.is(OneString.class)) {
                        nestedInnerClassHasAnnotation = true;

                        if (annotation.containsField("str")) {
                            nestedInnerClassHasAnnotationField = true;
                        }
                    }
                }
            }
        }

        assertTrue(innerClassExists, "Class AnnotationInnerClass.AnnotatedInnerClass does not exist");
        assertTrue(innerClassHasAnnotation, "Class AnnotationInnerClass.AnnotatedInnerClass does not have any annotation");
        assertTrue(nestedInnerClassExists, "Class AnnotationInnerClass.AnnotatedInnerClass.AnnotatedNestedInnerClass does not exist");
        assertTrue(nestedInnerClassHasAnnotation, "Class AnnotationInnerClass.AnnotatedInnerClass.AnnotatedNestedInnerClass does not have any annotations");
        assertTrue(nestedInnerClassHasAnnotationField, "Class AnnotationInnerClass.AnnotatedInnerClass.AnnotatedNestedInnerClass annotation does not have any fields");
    }
}
