package infinituum.fastconfigapi.fabric.tests;

import infinituum.fastconfigapi.api.annotations.FastConfig;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

@FastConfig(modId = MOD_ID)
public class ConfigTest1 {
    int x = 0;
    int y = 1;
}
