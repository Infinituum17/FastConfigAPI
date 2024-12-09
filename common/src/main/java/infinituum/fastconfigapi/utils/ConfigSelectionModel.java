package infinituum.fastconfigapi.utils;

import dev.architectury.networking.NetworkManager;
import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.network.ClientConfigRequest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ConfigSelectionModel {
    private static List<FastConfigFile<?>> configs;
    private final Minecraft minecraft;
    private FastConfigFile<?> selected;

    public ConfigSelectionModel(Minecraft minecraft) {
        this.minecraft = minecraft;
        configs = new ArrayList<>(FastConfigs.getAll());

        NetworkManager.sendToServer(new ClientConfigRequest());

        this.selected = !configs.isEmpty() ? configs.getFirst() : null;
    }

    public static void addServerConfigs(List<FastConfigFile<?>> configs) {
        ConfigSelectionModel.configs.addAll(configs);

        configs.sort(Comparator.comparing(FastConfigFile::getFileName));
    }

    public FastConfigFile<?> getSelected() {
        return selected;
    }

    public void setSelected(FastConfigFile<?> config) {
        this.selected = config;
    }

    public List<FastConfigFile<?>> getConfigs() {
        if (this.minecraft.isSingleplayer()) {
            return configs;
        }

        return configs; // TODO: Change with appropriate config selection
    }
}
