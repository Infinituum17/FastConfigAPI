package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.api.config.FastConfigs;
import infinituum.fastconfigapi.fabric.utils.ModScanner;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static infinituum.fastconfigapi.api.config.FastConfigs.MOD_ID;

public class FastConfigAPIFabricCommon implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        FastConfigs.register(ModScanner.getSidedConfigs(FastConfig.Side.COMMON));

        LOGGER.info("FastConfigAPI has been initialized!");
    }
}
