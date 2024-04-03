package infinituum.fastconfigapi.forge;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FastConfigAPIForge.MOD_ID)
public class FastConfigAPIForge {
    public static final String MOD_ID = "fastconfigapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public FastConfigAPIForge() {
        LOGGER.info("FastConfigAPI has been initialized!");
    }
}
