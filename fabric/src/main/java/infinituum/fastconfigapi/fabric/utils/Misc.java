package infinituum.fastconfigapi.fabric.utils;

import java.io.File;

public class Misc {
    public static String jvmPathToJavaPath(String jvm) {
        return jvm.replace('/', '.');
    }

    public static String classPathStringToJavaPath(String path) {
        return path.substring(0, path.length() - ".class".length()).replace(File.separatorChar, '.');
    }
}
