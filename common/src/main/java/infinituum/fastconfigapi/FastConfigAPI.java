package infinituum.fastconfigapi;

import org.slf4j.LoggerFactory;

public class FastConfigAPI {
    public static final String MOD_ID = "fastconfigapi";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Fast Config API");

    public static void initCommon() {
        LOGGER.info("FastConfigAPI has been initialized");
    }
}
