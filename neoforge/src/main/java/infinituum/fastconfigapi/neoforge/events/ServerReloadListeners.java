package infinituum.fastconfigapi.neoforge.events;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public final class ServerReloadListeners {
    @SubscribeEvent
    public static void addListener(AddReloadListenerEvent event) {
        event.addListener(new ConfigManager(FastConfig.Side.SERVER));
    }
}