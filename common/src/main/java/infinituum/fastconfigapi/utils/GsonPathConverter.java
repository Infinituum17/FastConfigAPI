package infinituum.fastconfigapi.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GsonPathConverter implements JsonSerializer<Path>, JsonDeserializer<Path> {
    @Override
    public Path deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Paths.get(json.getAsString());
    }

    @Override
    public JsonElement serialize(Path src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
