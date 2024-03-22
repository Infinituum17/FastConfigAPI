package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigAPI;
import net.fabricmc.api.ModInitializer;

public class FastConfigAPIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FastConfigAPI.init();
    }
}
