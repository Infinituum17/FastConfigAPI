package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigAPI;
import net.fabricmc.api.*;

public final class FastConfigAPIFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        FastConfigAPI.initCommon();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        FastConfigAPI.initClient();
    }

    @Override
    public void onInitializeServer() {
        FastConfigAPI.initServer();
    }
}
