package infinituum.fastconfigapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FastConfigAPI {
    public static final String MOD_ID = "fastconfigapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("FastConfigAPI has been initialized!");
    }
}
