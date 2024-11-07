package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.api.FastConfigFile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ConfigSelectionModel {
    private final List<FastConfigFile<?>> configs;
    private FastConfigFile<?> selected;

    public ConfigSelectionModel() {
        this.configs = new ArrayList<>(FastConfigs.getAll());
        this.configs.sort(Comparator.comparing(FastConfigFile::getFileName));

        this.selected = !configs.isEmpty() ? this.configs.getFirst() : null;
    }

    public FastConfigFile<?> getSelected() {
        return selected;
    }

    public void setSelected(FastConfigFile<?> config) {
        this.selected = config;
    }

    public List<FastConfigFile<?>> getConfigs() {
        return configs;
    }
}
