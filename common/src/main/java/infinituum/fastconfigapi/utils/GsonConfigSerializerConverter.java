package infinituum.fastconfigapi.utils;

import com.google.gson.*;
import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.void_lib.api.utils.UnsafeLoader;

import java.lang.reflect.Type;

public class GsonConfigSerializerConverter implements JsonSerializer<ConfigSerializer<?>>, JsonDeserializer<ConfigSerializer<?>> {

    @Override
    public ConfigSerializer<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String clazzName = json.getAsString();
        Class<?> clazz = UnsafeLoader.loadClassNoInit(clazzName, Thread.currentThread().getContextClassLoader());
        Class<?> wrapperClazz = clazz.getEnclosingClass();

        if (wrapperClazz != null) {
            SerializerWrapper<?> wrapper = (SerializerWrapper<?>) UnsafeLoader.loadInstance(wrapperClazz);

            return wrapper.get();
        }

        return UnsafeLoader.loadInstance((Class<? extends ConfigSerializer<?>>) PlatformHelper.getDefaultSerializer());
    }

    @Override
    public JsonElement serialize(ConfigSerializer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getClass().getName());
    }
}
