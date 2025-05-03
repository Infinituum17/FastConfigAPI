package infinituum.fastconfigapi.neoforge;

import infinituum.fastconfigapi.FastConfigAPI;
import net.neoforged.fml.common.Mod;

public final class FastConfigAPINeoForge {
    @Mod(FastConfigAPI.MOD_ID)
    public static class CommonInitializer {
        public CommonInitializer() {
            FastConfigAPI.initCommon();
        }
    }
}
