package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.FastConfigs;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FastConfigAPIFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.SERVER));
    }
}
