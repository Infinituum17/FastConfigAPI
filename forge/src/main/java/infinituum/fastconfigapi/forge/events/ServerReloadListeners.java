package infinituum.fastconfigapi.forge.events;

import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class ServerReloadListeners {
    @SubscribeEvent
    public static void addListener(AddReloadListenerEvent event) {
        event.addListener(new ConfigManager(FastConfig.Side.SERVER));
    }
}
