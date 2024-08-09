package infinituum.fastconfigapi.api.serializers;

public interface SerializerWrapper<T> {
    ConfigSerializer<T> get();
}
