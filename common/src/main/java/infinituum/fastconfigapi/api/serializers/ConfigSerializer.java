package infinituum.fastconfigapi.api.serializers;


import infinituum.fastconfigapi.api.config.FastConfigFile;

import java.io.IOException;

public interface ConfigSerializer<T> {
    void serialize(FastConfigFile<T> config) throws IOException;

    void deserialize(FastConfigFile<T> config) throws IOException;

    String getFileExtension();
}
