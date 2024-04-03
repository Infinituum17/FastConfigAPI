package infinituum.fastconfigapi.fabric;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastConfigAPIFabric implements ModInitializer {
    public static final String MOD_ID = "fastconfigapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("FastConfigAPI has been initialized!");
    }
}
