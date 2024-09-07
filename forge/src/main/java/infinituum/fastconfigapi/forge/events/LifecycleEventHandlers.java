package infinituum.fastconfigapi.forge.events;

import infinituum.fastconfigapi.api.FastConfigs;
import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.forge.utils.ConfigScanner;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import static infinituum.fastconfigapi.api.FastConfigs.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LifecycleEventHandlers {
    @SubscribeEvent
    public static void commonHandler(FMLCommonSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.COMMON));
    }

    @SubscribeEvent
    public static void clientHandler(FMLClientSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.CLIENT));
    }

    @SubscribeEvent
    public static void serverHandler(FMLDedicatedServerSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.SERVER));
    }
}
