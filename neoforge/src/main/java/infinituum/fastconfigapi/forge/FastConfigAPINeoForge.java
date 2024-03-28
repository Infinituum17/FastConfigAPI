package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.FastConfigAPI;
import net.neoforged.fml.common.Mod;

@Mod(FastConfigAPI.MOD_ID)
public class FastConfigAPINeoForge {
    public FastConfigAPINeoForge() {
        FastConfigAPI.init();
    }
}
