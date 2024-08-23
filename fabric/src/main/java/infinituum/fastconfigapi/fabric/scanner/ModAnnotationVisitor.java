package infinituum.fastconfigapi.fabric.scanner;

import infinituum.fastconfigapi.fabric.scanner.ModAnnotationScanner.ModAnnotation;
import infinituum.fastconfigapi.fabric.scanner.asm.AnnotationClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModAnnotationVisitor extends SimpleFileVisitor<Path> {
    private final Path basePath;
    private final Map<String, List<ModAnnotation>> classAnnotations;
    private final String directory;

    public ModAnnotationVisitor(Path basePath) {
        this.basePath = basePath;
        this.directory = null;
        this.classAnnotations = new HashMap<>();
    }

    public ModAnnotationVisitor(Path basePath, String directory) {
        this.basePath = basePath;
        this.directory = directory;
        this.classAnnotations = new HashMap<>();
    }

    public Path getBasePath() {
        return basePath;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".class")) {
            getClassAnnotations(file);
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

    private void getClassAnnotations(Path file) {
        String relative = basePath.relativize(file).toString();
        String className = relative
                .substring(0, relative.length() - ".class".length())
                .replace(File.separatorChar, '.');

        if (directory != null && className.startsWith(directory)) {
            getClassAnnotations(className);
        }

        if (directory == null) {
            getClassAnnotations(className);
        }
    }

    private void getClassAnnotations(String className) {
        ClassReader classReader;

        try {
            classReader = new ClassReader(className);
        } catch (IOException ignored) {
            return;
        }

        List<ModAnnotation> annotations = new ArrayList<>();
        AnnotationClassVisitor classVisitor = new AnnotationClassVisitor(annotations, className);

        classReader.accept(classVisitor, 0);

        classAnnotations.put(className, annotations);
    }

    public Map<String, List<ModAnnotation>> getClassAnnotations() {
        return classAnnotations;
    }
}
