package infinituum.fastconfigapi.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.utils.ConfigManager;
import infinituum.fastconfigapi.utils.ConfigManager.ConfigResource;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin implements ConfigResource {
    private ConfigManager configManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(RegistryAccess.Frozen frozen, FeatureFlagSet featureFlagSet, Commands.CommandSelection commandSelection, int i, CallbackInfo ci) {
        this.configManager = new ConfigManager(FastConfig.Side.SERVER);
    }

    @ModifyReturnValue(method = "listeners", at = @At("RETURN"))
    private List<PreparableReloadListener> listeners(List<PreparableReloadListener> original) {
        List<PreparableReloadListener> list = new ArrayList<>(original);

        list.add(this.configManager);

        return Collections.unmodifiableList(list);
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
