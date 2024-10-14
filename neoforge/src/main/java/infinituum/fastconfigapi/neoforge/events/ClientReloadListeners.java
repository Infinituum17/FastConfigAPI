package infinituum.fastconfigapi.neoforge.events;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ClientReloadListeners {
    @SubscribeEvent
    public static void addListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ConfigManager(FastConfig.Side.CLIENT));
    }
}