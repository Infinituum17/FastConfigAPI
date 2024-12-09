package infinituum.fastconfigapi.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public record ClientConfigRequest() implements CustomPacketPayload {
    public static final Type<ClientConfigRequest> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "client_config_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientConfigRequest> CODEC = StreamCodec.of(ClientConfigRequest::encode, ClientConfigRequest::new);

    public ClientConfigRequest(RegistryFriendlyByteBuf buf) {
        this();
    }

    public static void encode(ByteBuf buf, ClientConfigRequest request) {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
