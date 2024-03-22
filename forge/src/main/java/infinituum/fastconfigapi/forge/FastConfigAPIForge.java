package infinituum.fastconfigapi.forge;

import dev.architectury.platform.forge.EventBuses;
import infinituum.fastconfigapi.FastConfigAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FastConfigAPI.MOD_ID)
public class FastConfigAPIForge {
    public FastConfigAPIForge() {
        EventBuses.registerModEventBus(FastConfigAPI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FastConfigAPI.init();
    }
}
