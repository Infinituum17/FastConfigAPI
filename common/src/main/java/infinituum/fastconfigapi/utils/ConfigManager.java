package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ConfigManager implements ResourceManagerReloadListener {
    public ConfigManager(FastConfig.Side side) {
        if (side.ordinal() != FastConfig.Side.COMMON.ordinal()) {
            FastConfigs.register(PlatformHelper.getSidedConfigs(FastConfig.Side.COMMON));
        }

        FastConfigs.register(PlatformHelper.getSidedConfigs(side));
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        FastConfigs.reloadAll();
    }
}
