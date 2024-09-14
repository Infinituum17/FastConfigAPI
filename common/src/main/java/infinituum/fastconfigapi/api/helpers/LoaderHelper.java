package infinituum.fastconfigapi.api.helpers;

import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.annotations.Loader;

import java.util.Map;

public final class LoaderHelper {
    private static Map<String, Object> getAnnotationFields(Map<String, Object> data) {
        if (data.containsKey("loader")) {
            return PlatformHelper.getPlatformLoaderData(data.get("loader"));
        }

        return null;
    }

    public static Loader.Type getLoaderOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("type")) {
            return PlatformHelper.getPlatformLoaderType(innerData.get("type"));
        }

        return Loader.Type.DEFAULT;
    }

    public static String getTargetOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("target") && innerData.get("target") instanceof String target) {
            return target;
        }

        return "";
    }

    public static boolean getSilentlyFailOrDefault(Map<String, Object> data) {
        Map<String, Object> innerData = getAnnotationFields(data);

        if (innerData != null && innerData.containsKey("silentlyFail") && innerData.get("silentlyFail") instanceof Boolean silentlyFail) {
            return silentlyFail;
        }

        return false;
    }
}
