package infinituum.fastconfigapi.tests;

import infinituum.fastconfigapi.api.annotations.FastConfig;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

@FastConfig(modId = MOD_ID, side = FastConfig.Side.SERVER)
public class ServerSideConfig {
    boolean isServer = true;
}
