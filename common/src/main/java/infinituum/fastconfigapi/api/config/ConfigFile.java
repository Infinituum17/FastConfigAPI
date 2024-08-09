package infinituum.fastconfigapi.api.config;

public interface ConfigFile {
    void save();

    void load();

    void loadDefault();
}
