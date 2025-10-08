package infinituum.fastconfigapi.neoforge.events;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigResourceManagerReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public class ReloadListeners {
    private static final ResourceLocation CLIENT_CONFIG_RESOURCE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "client_config_resource");
    private static final ResourceLocation SERVER_CONFIG_RESOURCE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "server_config_resource");

    @EventBusSubscriber(modid = MOD_ID, value = Dist.DEDICATED_SERVER)
    public static class Server {
        @SubscribeEvent
        public static void addReloadListeners(AddServerReloadListenersEvent event) {
            event.addListener(SERVER_CONFIG_RESOURCE, new ConfigResourceManagerReloadListener(FastConfig.Side.SERVER));
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void addReloadListeners(AddClientReloadListenersEvent event) {
            event.addListener(CLIENT_CONFIG_RESOURCE, new ConfigResourceManagerReloadListener(FastConfig.Side.CLIENT));
        }
    }
}
