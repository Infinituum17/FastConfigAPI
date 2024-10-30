package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import infinituum.fastconfigapi.screens.utils.ExpansionListManager;
import infinituum.fastconfigapi.screens.utils.Refreshable;
import infinituum.fastconfigapi.screens.utils.Repositionable;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.FocusNavigationEvent.ArrowNavigation;
import net.minecraft.client.gui.navigation.FocusNavigationEvent.TabNavigation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ConfigSelectionList extends ObjectSelectionList<ConfigSelectionEntry> implements Refreshable, Repositionable {
    private final ExpansionListManager manager;
    private final ConfigSelectionModel model;

    public ConfigSelectionList(ExpansionListManager manager) {
        super(manager.getMinecraft(),
                manager.getListWidth(),
                manager.getListHeight(),
                manager.getTopPadding(),
                manager.getListItemHeight());

        this.manager = manager;
        this.model = manager.getModel();
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    public void setSelected(@Nullable ConfigSelectionEntry entry) {
        ConfigSelectionEntry previous = super.getSelected();
        super.setSelected(entry);

        if (entry != null && !entry.equals(previous)) {
            if (previous != null) {
                this.manager.saveCurrent();
            }
            this.model.setSelected(entry.getConfig());
        }

        this.manager.getOptions().setSelected(null);
        this.manager.getOptions().setFocused(null);
        this.manager.refreshOptions();
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        ConfigSelectionEntry selectedEntry = this.getSelected();
        ConfigSelectionEntry clickedEntry = getEntryAtPosition(d, e);
        boolean isProcessedClick = super.mouseClicked(d, e, i);

        if (isProcessedClick && selectedEntry != null && clickedEntry != null && !Objects.equals(selectedEntry, clickedEntry)) {
            this.manager.getOptions().setFocused(null);
            this.manager.getOptions().setSelected(null);
        }

        return isProcessedClick;
    }

    @Override
    public void refresh() {
        this.children().clear();
        this.setSelected(null);

        this.model.getConfigs().forEach(config -> {
            var entry = new ConfigSelectionEntry(this.manager, config, this);
            this.children().add(entry);

            if (config.equals(this.model.getSelected())) {
                this.setFocused(entry);
            }
        });
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (event instanceof TabNavigation tabNavigation) {
            if (tabNavigation.forward()) {
                return tabForward(tabNavigation);
            } else {
                return tabBackwards(tabNavigation);
            }
        } else if (event instanceof ArrowNavigation arrowNavigation) {
            return arrowNavigate(arrowNavigation);
        }

        return super.nextFocusPath(event);
    }

    private ComponentPath tabForward(TabNavigation navigation) {
        if (!this.isFocused() && !this.manager.getOptions().isFocused()) {
            this.manager.resetLists();

            return ComponentPath.path(this);
        }

        if (this.isFocused()) {
            ConfigOptionsList options = this.manager.getOptions();
            ComponentPath nextOptionsEntry = options.nextFocusPath(navigation);

            if (nextOptionsEntry != null) {
                this.setFocused(null);
                return nextOptionsEntry;
            }
        }

        ConfigSelectionEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            ConfigOptionsList options = this.manager.getOptions();

            if (options.isFocused()) {
                options.setFocused(null);
            }

            return ComponentPath.path(this, ComponentPath.leaf(nextEntry));
        }

        // No more configs, no more options

        this.setFocused(null);
        this.manager.getOptions().setSelected(null);

        return this.manager.getEscapePath();
    }

    private ComponentPath tabBackwards(TabNavigation navigation) {
        if (this.isFocused()) {
            ConfigOptionsList options = this.manager.getOptions();
            ComponentPath nextOptionsEntry = options.nextFocusPath(navigation);

            if (nextOptionsEntry != null) {
                this.setFocused(null);
                return nextOptionsEntry;
            }
        }

        ConfigSelectionEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            ConfigOptionsList options = this.manager.getOptions();

            if (!options.isFocused()) {
                options.setFocused(true);
                this.setSelected(nextEntry);

                return ComponentPath.path(this, ComponentPath.leaf(nextEntry));
            }

            return options.nextFocusPath(navigation);
        }

        this.setFocused(null);
        this.manager.getOptions().setSelected(null);

        return this.manager.getEscapePath();
    }

    private ComponentPath arrowNavigate(ArrowNavigation arrowNavigation) {
        return super.nextFocusPath(arrowNavigation);
    }

    @Override
    public void reposition(HeaderAndFooterLayout layout) {
        this.updateSize(this.manager.getListWidth(), layout);
        this.setX(this.manager.getListX());
    }
}
