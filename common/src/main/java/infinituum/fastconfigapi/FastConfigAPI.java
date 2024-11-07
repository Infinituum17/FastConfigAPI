package infinituum.fastconfigapi;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManagerReloadListener;
import infinituum.void_lib.api.events.ClientReloadEvent;
import infinituum.void_lib.api.events.ServerReloadEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.LoggerFactory;

public final class FastConfigAPI {
    public static final String MOD_ID = "fastconfigapi";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Fast Config API");

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientReloadEvent.EVENT.register(e -> e.registerReloadableResource(new ConfigManagerReloadListener(FastConfig.Side.CLIENT)));
    }

    public static void initServer() {
        ServerReloadEvent.EVENT.register(e -> e.registerReloadableResource(new ConfigManagerReloadListener(FastConfig.Side.SERVER)));
    }

    public static void initCommon() {
        LOGGER.info("FastConfigAPI has been initialized");
    }
}
