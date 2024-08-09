package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.api.FastConfigs;
import infinituum.fastconfigapi.api.utils.Global;
import net.minecraftforge.fml.common.Mod;

@Mod(FastConfigs.MOD_ID)
public class FastConfigAPIForge {
    public FastConfigAPIForge() {
        Global.LOGGER.info("FastConfigAPI has been initialized!");
    }
}
