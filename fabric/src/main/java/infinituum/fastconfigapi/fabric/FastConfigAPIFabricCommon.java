package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.utils.Global;
import net.fabricmc.api.ModInitializer;

public final class FastConfigAPIFabricCommon implements ModInitializer {
    @Override
    public void onInitialize() {
        Global.LOGGER.info("FastConfigAPI has been initialized");
    }
}
