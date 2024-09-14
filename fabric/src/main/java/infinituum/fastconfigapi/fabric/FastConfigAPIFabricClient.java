package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import net.fabricmc.api.ClientModInitializer;

public final class FastConfigAPIFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.CLIENT));
    }
}
