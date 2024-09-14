package infinituum.fastconfigapi.api.serializers;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.IOException;


public final class TOMLSerializer<T> implements SerializerWrapper<T> {
    private static ConfigSerializer<?> INSTANCE;

    public ConfigSerializer<T> get() {
        if (INSTANCE == null) {
            INSTANCE = new TOML<T>();
        }

        return (ConfigSerializer<T>) INSTANCE;
    }

    protected static class TOML<T> implements ConfigSerializer<T> {
        private static final TomlWriter TOML_WRITER = new TomlWriter();

        @Override
        public void serialize(FastConfigFileImpl<T> config) throws IOException {
            TOML_WRITER.write(config.getInstance(), config.getFullFilePath().toFile());
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config) {
            Toml toml = new Toml();

            T instance = toml.read(config.getFullFilePath().toFile()).to(config.getConfigClass());

            config.setInstance(instance);
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config, String content) throws IOException {
            Toml toml = new Toml();

            T instance = toml.read(content).to(config.getConfigClass());

            config.setInstance(instance);
        }

        @Override
        public String getFileExtension() {
            return "toml";
        }
    }
}
