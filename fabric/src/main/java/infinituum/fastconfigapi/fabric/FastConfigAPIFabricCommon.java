package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.ConfigScanner;
import infinituum.fastconfigapi.utils.Global;
import net.fabricmc.api.ModInitializer;

public final class FastConfigAPIFabricCommon implements ModInitializer {
    @Override
    public void onInitialize() {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.COMMON));
        Global.LOGGER.info("FastConfigAPI-common was successfully loaded");
    }
}
