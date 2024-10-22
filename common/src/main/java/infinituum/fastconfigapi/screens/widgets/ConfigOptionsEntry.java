package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.utils.ConfigOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ConfigOptionsEntry extends ObjectSelectionList.Entry<ConfigOptionsEntry> {
    private final ExpansionListManager manager;
    private final ConfigOption<?> option;
    private final EditBox editBox;

    public ConfigOptionsEntry(ExpansionListManager manager, ConfigOption<?> option) {
        this.manager = manager;
        this.option = option;

        this.editBox = new EditBox(this.manager.getFont(), 0, 0, 96, 16, Component.literal(option.getValue().toString()));
        this.editBox.setVisible(false);
        this.editBox.setEditable(true);
        this.editBox.setCanLoseFocus(true);
        this.editBox.setBordered(true);
        this.editBox.setMaxLength(32);
    }

    @Override
    public void setFocused(boolean bl) {
        this.editBox.setFocused(bl);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        guiGraphics.drawString(this.manager.getFont(), Component.literal(this.option.getFieldName()), k, j, 0xFFFFFF);

        int padding = 5;
        int offset = this.manager.getFont().width(this.option.getFieldName());

        if (!this.editBox.isVisible()) {
            this.editBox.setVisible(true);
            this.editBox.setPosition(k + padding + offset, j);
        }

        this.editBox.render(guiGraphics, i, j, f);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return this.editBox.keyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return this.editBox.keyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.editBox.charTyped(c, i);
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.literal(option.getFieldName() + ": '" + option.getValue() + "'");
    }
}
