package infinituum.fastconfigapi.fabric.utils;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigResourceManagerReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public class IdentifiableConfigResourceManagerReloadListener extends ConfigResourceManagerReloadListener implements IdentifiableResourceReloadListener {
    public IdentifiableConfigResourceManagerReloadListener(FastConfig.Side side) {
        super(side);
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "config_resource");
    }
}
