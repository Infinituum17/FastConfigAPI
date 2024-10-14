package infinituum.fastconfigapi.api.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;


public final class JSONSerializer<T> implements SerializerWrapper<T> {
    private static ConfigSerializer<?> INSTANCE;

    public ConfigSerializer<T> get() {
        if (INSTANCE == null) {
            INSTANCE = new JSON<T>();
        }

        return (ConfigSerializer<T>) INSTANCE;
    }

    protected static class JSON<T> implements ConfigSerializer<T> {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        @Override
        public void serialize(FastConfigFileImpl<T> config) throws RuntimeException {
            Path path = config.getFullFilePath();

            if (Files.exists(path) && !Files.isWritable(path)) {
                throw new RuntimeException("Could not write Config to path: '" + path + "': missing 'writing' permissions");
            }

            try (FileWriter writer = new FileWriter(path.toFile())) {
                String json = GSON.toJson(config.getInstance());

                if (json == null) {
                    throw new RuntimeException("Could not serialize Config to JSON");
                }

                writer.write(json);
            } catch (Exception e) {
                throw new RuntimeException("Could not write Config to path: '" + path + "': " + e);
            }
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config) throws RuntimeException {
            Path path = config.getFullFilePath();

            if (!Files.exists(path)) {
                throw new RuntimeException("Trying to deserialize a nonexistent file from '" + path + "'");
            }

            if (!Files.isReadable(path)) {
                throw new RuntimeException("Can't read Config from path: '" + path + "': missing 'reading' permissions");
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                this.deserialize(config, reader);
            } catch (Exception e) {
                throw new RuntimeException("Could not read Config from path: '" + path + "': " + e);
            }
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config, Reader reader) {
            Class<T> configClass = config.getConfigClass();
            T instance = GSON.fromJson(reader, configClass);

            if (instance == null) {
                throw new RuntimeException("Could not set Config instance value for class '"
                        + configClass.getSimpleName()
                        + "': deserialization produced a 'null' value");
            }

            config.setInstance(instance);
        }

        @Override
        public String getFileExtension() {
            return "json";
        }
    }
}
