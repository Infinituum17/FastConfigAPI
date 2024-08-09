package infinituum.fastconfigapi.apiV2.config;

public interface ConfigFile {
    void save();

    void load();

    void loadDefault();
}
