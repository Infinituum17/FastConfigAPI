package infinituum.fastconfigapi.api.config.annotations;

import infinituum.fastconfigapi.api.serializers.SerializerWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FastConfig {
    String fileName() default "";

    Class<? extends SerializerWrapper> serializer() default SerializerWrapper.class;

    Side side() default Side.COMMON;

    enum Side {
        CLIENT("client"),
        SERVER("server"),
        COMMON("common");

        private final String fileModifier;

        Side(String fileModifier) {
            this.fileModifier = fileModifier;
        }

        public String markFileName(String fileName) {
            return fileName + "-" + fileModifier;
        }
    }
}
