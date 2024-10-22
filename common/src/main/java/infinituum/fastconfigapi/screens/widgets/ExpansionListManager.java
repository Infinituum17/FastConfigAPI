package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.ConfigSelectionScreen;
import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import infinituum.fastconfigapi.screens.utils.Refreshable;
import infinituum.fastconfigapi.screens.utils.Repositionable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;

public final class ExpansionListManager implements Refreshable, Repositionable {
    private final Minecraft minecraft;
    private final ConfigSelectionScreen parent;
    private final ConfigSelectionList list;
    private final ConfigOptionsList options;
    private final ConfigSelectionModel model;

    public ExpansionListManager(Minecraft minecraft, ConfigSelectionScreen parent) {
        this.minecraft = minecraft;
        this.parent = parent;

        this.model = new ConfigSelectionModel();
        this.list = new ConfigSelectionList(this);
        this.options = new ConfigOptionsList(this);
    }

    Minecraft getMinecraft() {
        return this.minecraft;
    }

    Font getFont() {
        return this.minecraft.font;
    }

    ConfigSelectionModel getModel() {
        return this.model;
    }

    int getListX() {
        return 0;
    }

    int getListWidth() {
        return 175;
    }

    int getListHeight() {
        return parent.height - 70;
    }

    int getOptionsX() {
        return 180;
    }

    int getOptionsWidth() {
        return parent.width - 180;
    }

    int getOptionsHeight() {
        return parent.height - 70;
    }

    int getTopPadding() {
        return 33;
    }

    int getItemHeight() {
        return 43;
    }

    @Override
    public void reposition(HeaderAndFooterLayout layout) {
        this.repositionList(layout);
        this.repositionOptions(layout);
    }

    void repositionList(HeaderAndFooterLayout layout) {
        this.list.reposition(layout);
    }

    void repositionOptions(HeaderAndFooterLayout layout) {
        this.options.reposition(layout);
    }

    @Override
    public void refresh() {
        this.refreshList();
        this.refreshOptions();
    }

    void refreshList() {
        this.list.refresh();
    }

    void refreshOptions() {
        this.options.refresh();
    }

    public void save() {
        // TODO: Implement onClose resolution
    }

    public boolean isOptionsEmpty() {
        return this.options.children().isEmpty();
    }

    public boolean isListEmpty() {
        return this.list.children().isEmpty();
    }

    public ConfigSelectionList getList() {
        return list;
    }

    public ConfigOptionsList getOptions() {
        return options;
    }

    public ConfigSelectionScreen getScreen() {
        return parent;
    }

    public ComponentPath getEscapePath() {
        Button doneButton = this.parent.getDoneButton();

        doneButton.setFocused(true);

        return ComponentPath.path(this.parent, ComponentPath.leaf(doneButton));
    }

    public void resetLists() {
        ConfigSelectionEntry listFirstElement = this.list.getFirstElement();

        this.list.setSelected(listFirstElement);
        this.list.setFocused(listFirstElement);

        this.options.setSelected(null);
        this.options.setFocused(null);
    }
}
