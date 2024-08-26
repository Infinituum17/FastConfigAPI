package infinituum.fastconfigapi.examples.annotated_classes;

import infinituum.fastconfigapi.examples.annotations.NoParam;
import infinituum.fastconfigapi.examples.annotations.OneString;

public class AnnotationInnerClass {
    @NoParam
    public static class AnnotatedInnerClass {
        @OneString(str = "hello")
        public static class AnnotatedNestedInnerClass {
    
        }
    }
}
