package infinituum.fastconfigapi.screens.widgets.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class DynamicHeightObjectSelectionList<E extends DynamicHeightObjectSelectionList.Entry<E>> extends DynamicHeightAbstractSelectionList<E> {
    private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");

    public DynamicHeightObjectSelectionList(Minecraft minecraft, int i, int j, int k) {
        super(minecraft, i, j, k);
    }

    @Nullable
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        if (this.getItemCount() == 0) {
            return null;
        } else if (this.isFocused() && focusNavigationEvent instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
            E entry = this.nextEntry(arrowNavigation.direction());
            return entry != null ? ComponentPath.path(this, ComponentPath.leaf(entry)) : null;
        } else if (!this.isFocused()) {
            E entry2 = this.getSelected();
            if (entry2 == null) {
                entry2 = this.nextEntry(focusNavigationEvent.getVerticalDirectionForInitialFocus());
            }

            return entry2 == null ? null : ComponentPath.path(this, ComponentPath.leaf(entry2));
        } else {
            return null;
        }
    }

    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        E entry = this.getHovered();
        if (entry != null) {
            this.narrateListElementPosition(narrationElementOutput.nest(), entry);
            entry.updateNarration(narrationElementOutput);
        } else {
            E entry2 = this.getSelected();
            if (entry2 != null) {
                this.narrateListElementPosition(narrationElementOutput.nest(), entry2);
                entry2.updateNarration(narrationElementOutput);
            }
        }

        if (this.isFocused()) {
            narrationElementOutput.add(NarratedElementType.USAGE, USAGE_NARRATION);
        }

    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry<E extends DynamicHeightObjectSelectionList.Entry<E>> extends DynamicHeightAbstractSelectionList.Entry<E> implements NarrationSupplier {
        public Entry() {
        }

        public boolean mouseClicked(double d, double e, int i) {
            return true;
        }

        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, this.getNarration());
        }

        public abstract Component getNarration();
    }
}
