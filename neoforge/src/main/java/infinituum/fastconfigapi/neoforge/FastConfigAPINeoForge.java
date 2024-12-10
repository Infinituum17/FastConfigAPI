package infinituum.fastconfigapi.neoforge;

import infinituum.fastconfigapi.FastConfigAPI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

public final class FastConfigAPINeoForge {
    @Mod(FastConfigAPI.MOD_ID)
    public static class CommonInitializer {
        public CommonInitializer() {
            FastConfigAPI.initCommon();
        }
    }

    @Mod(value = FastConfigAPI.MOD_ID, dist = Dist.CLIENT)
    public static class ClientInitializer {
        public ClientInitializer() {
            FastConfigAPI.initClient();
        }
    }

    @Mod(value = FastConfigAPI.MOD_ID, dist = Dist.DEDICATED_SERVER)
    public static class ServerInitializer {
        public ServerInitializer() {
            FastConfigAPI.initServer();
        }
    }
}
