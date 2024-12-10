package infinituum.fastconfigapi;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigResourceManagerReloadListener;
import infinituum.void_lib.api.events.ClientReloadEvent;
import infinituum.void_lib.api.events.ServerReloadEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.LoggerFactory;

public class FastConfigAPI {
    public static final String MOD_ID = "fastconfigapi";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Fast Config API");

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientReloadEvent.EVENT.register(e -> {
            e.registerReloadableResource(new ConfigResourceManagerReloadListener(FastConfig.Side.CLIENT));
            e.registerReloadableResource(new ConfigResourceManagerReloadListener(FastConfig.Side.COMMON));
        });
    }

    public static void initServer() {
        ServerReloadEvent.EVENT.register(e -> e.registerReloadableResource(new ConfigResourceManagerReloadListener(FastConfig.Side.SERVER)));
    }

    public static void initCommon() {
        LOGGER.info("FastConfigAPI has been initialized");
    }
}
