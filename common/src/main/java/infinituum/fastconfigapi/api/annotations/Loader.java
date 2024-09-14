package infinituum.fastconfigapi.api.annotations;

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
import java.util.concurrent.CompletableFuture;
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
     * Defines a target for a specific {@link Type}.
     * <p>
     * The {@link Type#DEFAULT default type} doesn't need to check this field, so by default it's empty.
     *
     * @return {@link String} - default: None.
     */
    String target() default ""; // TODO: Check target (filter available domains)

    /**
     * If this is set to {@code true}, all the error messages regarding the config loading phase will be suppressed.
     *
     * @return {@link Boolean} - default: {@code false}.
     */
    boolean silentlyFail() default false;

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
        DEFAULT(config -> {
            config.setDefaultClassInstance();

            config.save();
        }),

        /**
         * The URL loader type.
         * <p>
         * This loader attempts to load a config file from a URL (making an HTTP request).
         * <p>
         * The '{@link Loader#target() target}' field of a {@link Loader} annotation will contain the URL we're trying to load the config from.
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

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> futureResponse = httpClient
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());

            futureResponse.thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        Thread.currentThread().setName("HttpRequest thread");
                        config.setPendingRequest(null);

                        try {
                            config.loadStateUnsafely(response);
                            Global.LOGGER.info("Config '{}' was successfully loaded", config.getFileNameWithExtension());
                        } catch (Exception e) {
                            useDefaultLoaderAfterException(config, e);
                            Global.LOGGER.info("Config '{}' was successfully loaded", config.getFileNameWithExtension());
                            return;
                        }

                        config.save();
                    })
                    .exceptionally(e -> {
                        Thread.currentThread().setName("HttpRequest thread");
                        
                        useDefaultLoaderAfterException(config, e);
                        Global.LOGGER.info("Config '{}' was successfully loaded", config.getFileNameWithExtension());
                        return null;
                    });

            config.setPendingRequest(futureResponse);
        });


        private final Consumer<FastConfigFileImpl<?>> loaderFunction;

        Type(Consumer<FastConfigFileImpl<?>> loaderFunction) {
            this.loaderFunction = loaderFunction;
        }

        private static <T> void useDefaultLoaderAfterException(FastConfigFileImpl<T> config, Throwable e) {
            config.setPendingRequest(null);

            if (!config.isSilentlyFailing()) {
                Global.LOGGER.error("Could not load config '{}' from url '{}', falling back to default loader: {}", config.getFileNameWithExtension(), config.getLoaderTarget(), e.getMessage());
            }

            config.setLoaderType(DEFAULT);
            config.setLoaderTarget("");

            config.loadDefault();
            config.save();
        }

        public <T> void load(FastConfigFileImpl<T> configFile) {
            this.loaderFunction.accept(configFile);
        }
    }
}
