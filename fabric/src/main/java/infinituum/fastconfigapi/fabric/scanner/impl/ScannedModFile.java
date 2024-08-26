package infinituum.fastconfigapi.fabric.scanner.impl;

import infinituum.fastconfigapi.fabric.scanner.api.AnnotatedClass;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;

import java.util.Collection;
import java.util.Set;

public record ScannedModFile(ModContainer modContainer, Set<AnnotatedClass> classes) {
    public String getModId() {
        return modContainer.getMetadata().getId();
    }

    public Collection<ModDependency> getDependencies() {
        return modContainer.getMetadata().getDependencies();
    }

    public String getModDisplayName() {
        return modContainer.getMetadata().getName();
    }
}
