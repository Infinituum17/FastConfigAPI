package infinituum.fastconfigapi.api.annotations;

import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.fastconfigapi.utils.Global;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * Auto-Registered Loader annotation.
 * <p>
 * Annotation that is used to define a default state loader for a config class (see {@link FastConfig @FastConfig}).
 * <p>
 * This loader is checked when the config needs to be created.
 * <p>
 * By default, the config's default {@link Type} is used (see {@link Type#DEFAULT Type.DEFAULT}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Loader {
    /**
     * Defines the {@link Type} that will be used to initially load the config.
     *
     * @return {@link Type} - default: {@link Type#DEFAULT Type.DEFAULT}.
     */
    Type type();

    /**
     * If this is set to {@code true}, all the error messages regarding the config loading phase will be suppressed.
     *
     * @return {@link Boolean} - default: {@code false}.
     */
    boolean silentlyFail() default false;

    /**
     * Defines a target for a specific {@link Type}.
     * <p>
     * The {@link Type#DEFAULT default type} doesn't need to check this field, so by default it's empty.
     *
     * @return {@link String} - default: None.
     */
    String target() default "";

    /**
     * Defines a deserializer used to parse the content loaded by this {@link Loader}.
     * <p>
     * The {@link Type#DEFAULT default type} doesn't need to check this field, so by default it's empty.
     *
     * @return {@link Class Class<? extends SerializeWrapper>} - default: None ({@link FastConfig}'s serializer will be used).
     */
    Class<? extends SerializerWrapper> deserializer() default SerializerWrapper.class;

    /**
     * Type enum.
     * <p>
     * Defines all available loader types.
     */
    enum Type {
        /**
         * The default loader type.
         * <p>
         * Files are normally loaded from a class template (see {@link FastConfig}).
         * <p>
         * The '{@link Loader#target() target}' field of a {@link Loader} annotation will always be ignored.
         */
        DEFAULT(FastConfigFileImpl::setDefaultClassInstance),

        /**
         * The URL loader type.
         * <p>
         * This loader attempts to load a config file from a URL (making an HTTP request).
         * <p>
         * The '{@link Loader#target() target}' field of a {@link Loader} annotation will contain the URL we're trying to load the config from.
         * This loader runs synchronously, so it's recommended to run it in a separate thread.
         * <p>
         * If the resource we're trying to access with this URL isn't available, the config will be initialized with the {@link Type#DEFAULT default} loader.
         */
        URL(config -> {
            URI uri;

            try {
                uri = new URL(config.getLoaderTarget()).toURI();
            } catch (Exception e) {
                useDefaultLoaderAfterException(config, e);
                return;
            }

            if (!config.getLoaderTarget().startsWith("https://")) {
                useDefaultLoaderAfterException(config, new RuntimeException("Only \"https://\" domains are valid"));
                return;
            }

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<?> response;

            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                useDefaultLoaderAfterException(config, e);
                return;
            }

            String body = (String) response.body();

            try {
                config.loadStateUnsafely(body);
            } catch (Exception e) {
                useDefaultLoaderAfterException(config, e);
            }
        });

        private final Consumer<FastConfigFileImpl<?>> loaderFunction;

        Type(Consumer<FastConfigFileImpl<?>> loaderFunction) {
            this.loaderFunction = loaderFunction;
        }

        private static <T> void useDefaultLoaderAfterException(FastConfigFileImpl<T> config, Throwable e) {
            if (!config.isSilentlyFailing()) {
                Global.LOGGER.error("Could not load config '{}' from url '{}', using default loader: {}", config.getFileNameWithExtension(), config.getLoaderTarget(), e.getMessage());
            }

            config.setDefaultLoader();
            config.loadDefault();
        }

        public <T> void load(FastConfigFileImpl<T> configFile) {
            this.loaderFunction.accept(configFile);
        }
    }
}
