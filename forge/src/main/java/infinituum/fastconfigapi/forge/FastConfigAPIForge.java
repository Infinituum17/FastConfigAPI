package infinituum.fastconfigapi.forge;

import infinituum.fastconfigapi.utils.Global;
import net.minecraftforge.fml.common.Mod;

@Mod(Global.MOD_ID)
public final class FastConfigAPIForge {
    public FastConfigAPIForge() {
        Global.LOGGER.info("FastConfigAPI has been initialized");
    }
}
