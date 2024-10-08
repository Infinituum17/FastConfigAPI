package infinituum.fastconfigapi.api.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;


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
        public void serialize(FastConfigFileImpl<T> config) throws IOException {
            try (FileWriter writer = new FileWriter(config.getFullFilePath().toFile())) {
                writer.write(GSON.toJson(config.getInstance()));
            }
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config) throws IOException {
            Reader reader = Files.newBufferedReader(config.getFullFilePath());

            config.setInstance(GSON.fromJson(reader, config.getConfigClass()));
        }

        @Override
        public void deserialize(FastConfigFileImpl<T> config, Reader reader) {
            config.setInstance(GSON.fromJson(reader, config.getConfigClass()));
        }

        @Override
        public String getFileExtension() {
            return "json";
        }
    }
}
