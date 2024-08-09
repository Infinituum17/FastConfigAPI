package infinituum.fastconfigapi.apiV2.serializers;

public interface SerializerWrapper<T> {
    ConfigSerializer<T> get();
}
