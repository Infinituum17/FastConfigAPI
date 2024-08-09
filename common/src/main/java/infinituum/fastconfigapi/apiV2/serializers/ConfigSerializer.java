package infinituum.fastconfigapi.apiV2.serializers;


import infinituum.fastconfigapi.apiV2.config.FastConfigFile;

import java.io.IOException;

public interface ConfigSerializer<T> {
    void serialize(FastConfigFile<T> config) throws IOException;

    void deserialize(FastConfigFile<T> config) throws IOException;

    String getFileExtension();
}
