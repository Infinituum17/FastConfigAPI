package infinituum.fastconfigapi.utils;

import infinituum.fastconfigapi.screens.ConfigSelectionScreen;
import infinituum.fastconfigapi.screens.widgets.ConfigOptionsList;
import infinituum.fastconfigapi.screens.widgets.ConfigOptionsListEntry;
import infinituum.fastconfigapi.screens.widgets.ConfigSelectionList;
import infinituum.fastconfigapi.screens.widgets.ConfigSelectionListEntry;
import infinituum.fastconfigapi.screens.widgets.type.Refreshable;
import infinituum.fastconfigapi.screens.widgets.type.Repositionable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;

import java.util.List;

public final class ConfigScreenManager implements Refreshable, Repositionable {
    private final Minecraft minecraft;
    private final ConfigSelectionScreen parent;
    private final ConfigSelectionList list;
    private final ConfigOptionsList<?> options;
    private final ConfigSelectionModel model;

    public ConfigScreenManager(Minecraft minecraft, ConfigSelectionScreen parent) {
        this.minecraft = minecraft;
        this.parent = parent;

        this.model = new ConfigSelectionModel(minecraft);
        this.list = new ConfigSelectionList(this, this.model);
        this.options = new ConfigOptionsList<>(this, this.model);
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public Font getFont() {
        return this.minecraft.font;
    }

    public int getListX() {
        return 0;
    }

    public int getListWidth() {
        return 225;
    }

    public int getListHeight() {
        return parent.height - 70;
    }

    public int getImageSize() {
        return 50;
    }

    public int getOptionsWidth() {
        return parent.width - getOptionsX() - 10;
    }

    public int getOptionsX() {
        return 225 + 10;
    }

    public int getOptionsHeight() {
        return parent.height - 70;
    }

    public int getTopPadding() {
        return 33;
    }

    @Override
    public void reposition(HeaderAndFooterLayout layout) {
        this.repositionList(layout);
        this.repositionOptions(layout);
    }

    public void repositionList(HeaderAndFooterLayout layout) {
        this.list.reposition(layout);
    }

    public void repositionOptions(HeaderAndFooterLayout layout) {
        this.options.reposition(layout);
    }

    @Override
    public void refresh() {
        this.refreshList();
        this.refreshOptions();
    }

    public void refreshList() {
        this.list.refresh();
    }

    public void refreshOptions() {
        this.options.refresh();
    }

    public void saveCurrent() {
        List<? extends ConfigOptionsListEntry<?>> options = this.getOptions().children();

        for (ConfigOptionsListEntry<?> option : options) {
            option.save();
        }

        this.model.getSelected().save();
    }

    public ConfigOptionsList<?> getOptions() {
        return options;
    }

    public ConfigSelectionList getList() {
        return list;
    }

    public ComponentPath getEscapePath() {
        Button doneButton = this.parent.getDoneButton();

        doneButton.setFocused(true);

        return ComponentPath.path(this.parent, ComponentPath.leaf(doneButton));
    }

    public void resetLists() {
        ConfigSelectionListEntry listFirstElement = this.list.getFirstElement();

        this.list.setSelected(listFirstElement);
        this.list.setFocused(listFirstElement);

        this.options.setSelected(null);
        this.options.setFocused(null);
    }
}
