package infinituum.fastconfigapi.forge.events;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.forge.utils.ConfigScanner;
import infinituum.fastconfigapi.utils.Global;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import static infinituum.fastconfigapi.utils.Global.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LifecycleEventHandlers {
    @SubscribeEvent
    public static void commonHandler(FMLCommonSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.COMMON));
        Global.LOGGER.info("FastConfigAPI-common was successfully loaded");
    }

    @SubscribeEvent
    public static void clientHandler(FMLClientSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.CLIENT));
        Global.LOGGER.info("FastConfigAPI-client was successfully loaded");
    }

    @SubscribeEvent
    public static void serverHandler(FMLDedicatedServerSetupEvent event) {
        FastConfigs.register(ConfigScanner.getSidedConfigs(FastConfig.Side.SERVER));
        Global.LOGGER.info("FastConfigAPI-server was successfully loaded");
    }
}
