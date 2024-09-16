package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import infinituum.fastconfigapi.utils.Global;
import net.fabricmc.api.DedicatedServerModInitializer;

public final class FastConfigAPIFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.SERVER));
        Global.LOGGER.info("FastConfigAPI-server was successfully loaded");
    }
}
