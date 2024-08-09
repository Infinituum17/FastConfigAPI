package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.apiV2.config.FastConfigs;
import infinituum.fastconfigapi.apiV2.utils.Global;
import net.minecraftforge.fml.common.Mod;

@Mod(FastConfigs.MOD_ID)
public class FastConfigAPIForge {
    public FastConfigAPIForge() {
        Global.LOGGER.info("FastConfigAPI has been initialized!");
    }
}
