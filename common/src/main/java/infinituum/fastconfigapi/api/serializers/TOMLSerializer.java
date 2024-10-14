package infinituum.fastconfigapi.api.serializers;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;


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
            Path path = config.getFullFilePath();

            if (Files.exists(path) && !Files.isWritable(path)) {
                throw new RuntimeException("Could not write Config to path: '" + path + "': missing 'writing' permissions");
            }

            TOML_WRITER.write(config.getInstance(), path.toFile());
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config) {
            Toml toml = new Toml();
            Path path = config.getFullFilePath();

            if (!Files.exists(path)) {
                throw new RuntimeException("Trying to deserialize a nonexistent file from '" + path + "'");
            }

            if (!Files.isReadable(path)) {
                throw new RuntimeException("Can't read Config from path: '" + path + "': missing 'reading' permissions");
            }

            Class<T> configClass = config.getConfigClass();
            T instance = toml.read(path.toFile()).to(configClass);

            if (instance == null) {
                throw new RuntimeException("Could not set Config instance value for class '"
                        + configClass.getSimpleName()
                        + "': deserialization produced a 'null' value");
            }

            config.setInstance(instance);
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config, Reader reader) throws RuntimeException {
            Toml toml = new Toml();
            Path path = config.getFullFilePath();

            if (!Files.exists(path)) {
                throw new RuntimeException("Trying to deserialize a nonexistent file from '" + path + "'");
            }

            if (!Files.isReadable(path)) {
                throw new RuntimeException("Can't read Config from path: '" + path + "': missing 'reading' permissions");
            }

            Class<T> configClass = config.getConfigClass();
            T instance = toml.read(reader).to(config.getConfigClass());

            if (instance == null) {
                throw new RuntimeException("Could not set Config instance value for class '"
                        + configClass.getSimpleName()
                        + "': deserialization produced a 'null' value");
            }

            config.setInstance(instance);
        }

        @Override
        public String getFileExtension() {
            return "toml";
        }
    }
}
