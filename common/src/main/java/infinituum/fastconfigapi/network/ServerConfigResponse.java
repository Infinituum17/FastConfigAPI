package infinituum.fastconfigapi.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.serializers.ConfigSerializer;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;
import infinituum.fastconfigapi.utils.ClassTypeAdapterFactory;
import infinituum.fastconfigapi.utils.GsonConfigSerializerConverter;
import infinituum.fastconfigapi.utils.GsonPathConverter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ServerConfigResponse implements CustomPacketPayload {
    public static final Type<ServerConfigResponse> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "server_config_response"));
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(ClassTypeAdapterFactory.get())
            .registerTypeHierarchyAdapter(ConfigSerializer.class, new GsonConfigSerializerConverter())
            .registerTypeHierarchyAdapter(Path.class, new GsonPathConverter())
            .create();
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerConfigResponse> CODEC = StreamCodec.of(ServerConfigResponse::encode, ServerConfigResponse::decode);
    private final List<FastConfigFile<?>> configs;

    public ServerConfigResponse(List<FastConfigFile<?>> configs) {
        this.configs = configs;
    }

    private static void encode(RegistryFriendlyByteBuf buf, ServerConfigResponse data) {
        buf.writeInt(data.configs.size());

        for (FastConfigFile<?> config : data.configs) {
            String json = gson.toJson(config);

            buf.writeInt(json.length());
            buf.writeCharSequence(json, Charset.defaultCharset());
        }
    }

    private static @NotNull ServerConfigResponse decode(RegistryFriendlyByteBuf buf) {
        final int configCount = buf.readInt();
        List<FastConfigFile<?>> configs = new ArrayList<>();

        for (int i = 0; i < configCount; ++i) {
            int length = buf.readInt();
            String json = buf.readCharSequence(length, Charset.defaultCharset()).toString();

            configs.add(gson.fromJson(json, FastConfigFileImpl.class));
        }

        return new ServerConfigResponse(configs);
    }

    public List<FastConfigFile<?>> getConfigs() {
        return configs;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
