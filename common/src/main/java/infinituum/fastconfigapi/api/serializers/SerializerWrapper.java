package infinituum.fastconfigapi.api.serializers;

/**
 * Serializer wrapper for {@link ConfigSerializer}.
 * <p>
 * Manages a single instance of a {@link ConfigSerializer} (singleton).
 *
 * @param <T> The type of {@link ConfigSerializer}.
 */
public interface SerializerWrapper<T> {
    /**
     * Gets the only instance of a {@link ConfigSerializer} available.
     *
     * @return An instance of a {@link ConfigSerializer}.
     */
    ConfigSerializer<T> get();
}
