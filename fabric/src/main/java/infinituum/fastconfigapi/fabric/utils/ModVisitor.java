package infinituum.fastconfigapi.fabric.utils;

import infinituum.fastconfigapi.api.config.annotations.FastConfig;
import infinituum.fastconfigapi.api.utils.UnsafeLoader;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static infinituum.fastconfigapi.fabric.utils.Misc.classPathStringToJavaPath;

public class ModVisitor<T> extends SimpleFileVisitor<Path> {
    private final Path basePath;
    private final List<Class<T>> classes;

    public ModVisitor(Path basePath) {
        this.basePath = basePath;
        this.classes = new ArrayList<>();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".class")) {
            Class<T> clazz = pathToClass(file);

            if (clazz != null) {
                classes.add(clazz);
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    private Class<T> pathToClass(Path file) {
        Path relative = basePath.relativize(file);
        String className = classPathStringToJavaPath(relative.toString());

        return UnsafeLoader.loadClass(className, FastConfig.class.getClassLoader());
    }

    public List<Class<T>> getClasses() {
        return classes;
    }
}
