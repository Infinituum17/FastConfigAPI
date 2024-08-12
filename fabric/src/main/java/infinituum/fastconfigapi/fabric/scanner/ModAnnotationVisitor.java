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
import java.util.List;

public final class ModAnnotationVisitor extends SimpleFileVisitor<Path> {
    private final Path basePath;
    private final List<ModAnnotation> annotations;

    public ModAnnotationVisitor(Path basePath) {
        this.basePath = basePath;
        this.annotations = new ArrayList<>();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.toString().endsWith(".class")) {
            List<ModAnnotation> annotations = getClassAnnotations(file);

            if (annotations != null && !annotations.isEmpty()) {
                this.annotations.addAll(annotations);
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

    private List<ModAnnotation> getClassAnnotations(Path file) {
        String relative = basePath.relativize(file).toString();
        String className = relative
                .substring(0, relative.length() - ".class".length())
                .replace(File.separatorChar, '.');

        ClassReader classReader;

        try {
            classReader = new ClassReader(className);
        } catch (IOException ignored) {
            return null;
        }

        List<ModAnnotation> annotations = new ArrayList<>();
        AnnotationClassVisitor classVisitor = new AnnotationClassVisitor(annotations);

        classReader.accept(classVisitor, 0);

        return annotations;
    }

    public List<ModAnnotation> getAnnotations() {
        return annotations;
    }
}
