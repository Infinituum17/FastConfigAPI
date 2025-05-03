package infinituum.fastconfigapi.fabric;

import infinituum.fastconfigapi.FastConfigAPI;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.fabric.utils.IdentifiableConfigResourceManagerReloadListener;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public final class FastConfigAPIFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        FastConfigAPI.initCommon();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableConfigResourceManagerReloadListener(FastConfig.Side.CLIENT));
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableConfigResourceManagerReloadListener(FastConfig.Side.SERVER));
    }

    @Override
    public void onInitializeServer() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableConfigResourceManagerReloadListener(FastConfig.Side.SERVER));
    }
}