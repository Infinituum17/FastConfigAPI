package infinituum.fastconfigapi.forge;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FastConfigAPINeoForge.MOD_ID)
public class FastConfigAPINeoForge {
    public static final String MOD_ID = "fastconfigapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public FastConfigAPINeoForge() {
        LOGGER.info("FastConfigAPI has been initialized!");
    }
}