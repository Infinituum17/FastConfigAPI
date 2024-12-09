package infinituum.fastconfigapi.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import infinituum.void_lib.api.utils.UnsafeLoader;

import java.io.IOException;

public class ClassTypeAdapterFactory implements TypeAdapterFactory {
    private static final ClassTypeAdapterFactory factory = new ClassTypeAdapterFactory();

    private ClassTypeAdapterFactory() {
    }

    public static TypeAdapterFactory get() {
        return factory;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().equals(Class.class)) {
            return (TypeAdapter<T>) new ClassTypeAdapter<>();
        }

        return null;
    }

    public static class ClassTypeAdapter<T> extends TypeAdapter<Class<T>> {
        @Override
        public void write(JsonWriter out, Class<T> value) throws IOException {
            out.value(value.getName());
        }

        @Override
        public Class<T> read(JsonReader in) throws IOException {
            return UnsafeLoader.loadClassNoInit(in.nextString(), Thread.currentThread().getContextClassLoader());
        }
    }

}
