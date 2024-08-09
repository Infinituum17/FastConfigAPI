package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.config.FastConfigs;
import infinituum.fastconfigapi.fabric.utils.ModScanner;
import net.fabricmc.api.ClientModInitializer;

public class FastConfigAPIFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FastConfigs.register(ModScanner.getSidedConfigs(FastConfig.Side.CLIENT));
    }
}
