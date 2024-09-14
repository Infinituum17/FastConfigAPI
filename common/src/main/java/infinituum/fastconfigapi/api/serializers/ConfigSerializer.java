package infinituum.fastconfigapi.api.serializers;


import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.IOException;

/**
 * A Serializer interface that defines all methods that are used in the serialization / deserialization of a {@link FastConfigFileImpl}.
 *
 * @param <T> The type contained inside the {@link FastConfigFileImpl}.
 */
public interface ConfigSerializer<T> { // TODO: Implement serializer translation
    /**
     * Tries to serialize the class associated with the {@link FastConfigFileImpl} passed in.
     *
     * @param config The config to serialize.
     * @throws IOException Exception during serialization.
     */
    void serialize(FastConfigFileImpl<T> config) throws IOException;

    /**
     * Tries to deserialize the file associated with the {@link FastConfigFileImpl} passed in.
     * <p>
     * The class instance inside the {@link FastConfigFileImpl} is set to the result of the deserialization.
     *
     * @param config The config to deserialize.
     * @throws IOException Exception during deserialization.
     */
    void deserialize(FastConfigFileImpl<T> config) throws IOException;

    /**
     * Tries to deserialize the string passed in.
     * <p>
     * The class instance inside the {@link FastConfigFileImpl} is set to the result of the deserialization.
     *
     * @param config  The config to deserialize.
     * @param content The string that will be deserialized.
     * @throws IOException Exception during deserialization.
     */
    void deserialize(FastConfigFileImpl<T> config, String content) throws IOException;

    /**
     * Gets the file extension associated with this serializer.
     *
     * @return The file extension.
     */
    String getFileExtension();
}
