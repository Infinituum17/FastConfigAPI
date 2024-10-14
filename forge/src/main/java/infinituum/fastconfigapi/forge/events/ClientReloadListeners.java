package infinituum.fastconfigapi.forge.events;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManager;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientReloadListeners {
    @SubscribeEvent
    public static void addListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ConfigManager(FastConfig.Side.CLIENT));
    }
}
