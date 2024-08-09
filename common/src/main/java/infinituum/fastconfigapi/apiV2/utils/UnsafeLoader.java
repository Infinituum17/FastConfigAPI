package infinituum.fastconfigapi.apiV2.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public final class UnsafeLoader {
    public static <T> T loadInstance(Class<T> clazz) {
        String className = clazz.getName();

        try {
            Constructor<T> constructor = clazz.getConstructor();

            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            if (clazz.getConstructors().length > 1) {
                throw new RuntimeException("Unsafely loaded class '" + className + "' can't define more than one constructor, only the default / parameterless constructor is accepted", e);
            }

            throw new RuntimeException("Unsafely loaded class '" + className + "' can't define a constructor that accepts more than 0 parameters, only the default / parameterless constructor is accepted", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate '" + className + "'", e);
        }
    }

    public static <T> Class<T> loadClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> Class<T> loadClass(String className, ClassLoader classLoader) {
        try {
            return (Class<T>) Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
