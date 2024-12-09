package infinituum.fastconfigapi;

import dev.architectury.networking.NetworkManager;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.network.ClientConfigRequest;
import infinituum.fastconfigapi.network.ServerConfigResponse;
import infinituum.fastconfigapi.screens.ConfigSelectionScreen;
import infinituum.fastconfigapi.utils.ConfigResourceManagerReloadListener;
import infinituum.fastconfigapi.utils.ConfigSelectionModel;
import infinituum.void_lib.api.events.ClientReloadEvent;
import infinituum.void_lib.api.events.ServerReloadEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class FastConfigAPI {
    public static final String MOD_ID = "fastconfigapi";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Fast Config API");

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientReloadEvent.EVENT.register(e -> e.registerReloadableResource(new ConfigResourceManagerReloadListener(FastConfig.Side.CLIENT)));

        NetworkManager.registerReceiver(NetworkManager.s2c(), ServerConfigResponse.TYPE, ServerConfigResponse.CODEC, (value, context) -> {
            ConfigSelectionModel.addServerConfigs(value.getConfigs());

            if (Minecraft.getInstance().screen instanceof ConfigSelectionScreen configScreen) {
                configScreen.reload();
            }
        });
    }

    public static void initServer() {
        ServerReloadEvent.EVENT.register(e -> e.registerReloadableResource(new ConfigResourceManagerReloadListener(FastConfig.Side.SERVER)));

        NetworkManager.registerS2CPayloadType(ServerConfigResponse.TYPE, ServerConfigResponse.CODEC);
    }

    public static void initCommon() {
        LOGGER.info("FastConfigAPI has been initialized");

        NetworkManager.registerReceiver(NetworkManager.c2s(), ClientConfigRequest.TYPE, ClientConfigRequest.CODEC, (value, context) -> {
            List<FastConfigFile<?>> configs = FastConfigs.getAll()
                    .stream()
                    .filter(config -> config.getSide() == FastConfig.Side.SERVER).toList();

            NetworkManager.sendToPlayer((ServerPlayer) context.getPlayer(), new ServerConfigResponse(configs));
        });
    }
}
