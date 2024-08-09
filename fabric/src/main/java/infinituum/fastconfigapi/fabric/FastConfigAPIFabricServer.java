package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.FastConfigs;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ModScanner;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FastConfigAPIFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FastConfigs.register(ModScanner.getSidedConfigs(FastConfig.Side.SERVER));
    }
}
