package infinituum.fastconfigapi.apiV2.serializers;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import infinituum.fastconfigapi.apiV2.config.FastConfigFile;

import java.io.IOException;


public final class TOMLSerializer<T> implements SerializerWrapper<T> {
    private static ConfigSerializer<?> INSTANCE;

    public infinituum.fastconfigapi.apiV2.serializers.ConfigSerializer<T> get() {
        if (INSTANCE == null) {
            INSTANCE = new TOML<T>();
        }

        return (ConfigSerializer<T>) INSTANCE;
    }

    protected static class TOML<T> implements ConfigSerializer<T> {
        private static final TomlWriter TOML_WRITER = new TomlWriter();

        @Override
        public void serialize(FastConfigFile<T> config) throws IOException {
            TOML_WRITER.write(config.getInstance(), config.getFullPath().toFile());
        }

        @Override
        public void deserialize(FastConfigFile<T> config) {
            Toml toml = new Toml();

            T instance = toml.read(config.getFullPath().toFile()).to(config.getConfigClass());

            config.setInstance(instance);
        }

        @Override
        public String getFileExtension() {
            return "toml";
        }
    }
}
