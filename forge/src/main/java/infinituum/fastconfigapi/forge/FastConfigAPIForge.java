package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.FastConfigAPI;
import net.minecraftforge.fml.common.Mod;

@Mod(FastConfigAPI.MOD_ID)
public class FastConfigAPIForge {
    public FastConfigAPIForge() {
        FastConfigAPI.init();
    }
}
