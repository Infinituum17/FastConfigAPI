package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.FastConfigs;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import net.fabricmc.api.ClientModInitializer;

public class FastConfigAPIFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.CLIENT));
    }
}
