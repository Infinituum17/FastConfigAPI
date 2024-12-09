package infinituum.fastconfigapi.tests;

import infinituum.fastconfigapi.api.annotations.FastConfig;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

@FastConfig(modId = MOD_ID, side = FastConfig.Side.COMMON)
public class CommonSideConfig {
    boolean isCommon = true;
}
