package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.utils.renderer.widget.DynamicHeightObjectSelectionList;
import infinituum.fastconfigapi.screens.widgets.type.Refreshable;
import infinituum.fastconfigapi.screens.widgets.type.Repositionable;
import infinituum.fastconfigapi.utils.ConfigScreenManager;
import infinituum.fastconfigapi.utils.ConfigSelectionModel;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.FocusNavigationEvent.ArrowNavigation;
import net.minecraft.client.gui.navigation.FocusNavigationEvent.TabNavigation;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ConfigSelectionList extends DynamicHeightObjectSelectionList<ConfigSelectionListEntry> implements Refreshable, Repositionable {
    private final ConfigScreenManager manager;
    private final ConfigSelectionModel model;

    public ConfigSelectionList(ConfigScreenManager manager, ConfigSelectionModel model) {
        super(manager.getMinecraft(),
                manager.getListWidth(),
                manager.getListHeight(),
                manager.getTopPadding());

        this.manager = manager;
        this.model = model;
    }

    @Override
    public void refresh() {
        this.children().clear();
        this.setSelected(null);

        this.model.getConfigs().forEach(config -> {
            var entry = new ConfigSelectionListEntry(this.manager, config, this);
            this.children().add(entry);

            if (config.equals(this.model.getSelected())) {
                this.setFocused(entry);
            }
        });
    }

    @Override
    public void setSelected(@Nullable ConfigSelectionListEntry entry) {
        ConfigSelectionListEntry previous = super.getSelected();
        super.setSelected(entry);

        if (entry != null && !entry.equals(previous)) {
            if (previous != null) {
                this.manager.saveCurrent();
            }
            this.model.setSelected(entry.getConfig());
            this.manager.getOptions().setScrollAmount(0);
        }

        this.manager.getOptions().setSelected(null);
        this.manager.getOptions().setFocused(null);
        this.manager.refreshOptions();
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        ConfigSelectionListEntry selectedEntry = this.getSelected();
        ConfigSelectionListEntry clickedEntry = getEntryAtPosition(d, e);
        boolean isProcessedClick = super.mouseClicked(d, e, i);

        if (isProcessedClick && selectedEntry != null && clickedEntry != null) {
            if (!Objects.equals(selectedEntry, clickedEntry)) {
                this.manager.getOptions().setFocused(null);
                this.manager.getOptions().setSelected(null);
            }

            this.manager.getOptions().setScrollAmount(0);
        }

        return isProcessedClick;
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
            ConfigOptionsList<?> options = this.manager.getOptions();
            ComponentPath nextOptionsEntry = options.nextFocusPath(navigation);

            if (nextOptionsEntry != null) {
                this.setFocused(null);
                return nextOptionsEntry;
            }
        }

        ConfigSelectionListEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            ConfigOptionsList<?> options = this.manager.getOptions();

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
            ConfigOptionsList<?> options = this.manager.getOptions();
            ComponentPath nextOptionsEntry = options.nextFocusPath(navigation);

            if (nextOptionsEntry != null) {
                this.setFocused(null);
                return nextOptionsEntry;
            }
        }

        ConfigSelectionListEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            ConfigOptionsList<?> options = this.manager.getOptions();

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

    public void renderTooltip(GuiGraphics guiGraphics, int i, int j, float f) {
        ConfigSelectionListEntry entry = this.getHovered();

        if (entry != null) {
            List<Component> tooltip = entry.getTooltip();

            if (!tooltip.isEmpty()) {
                guiGraphics.renderTooltip(this.minecraft.font, tooltip, Optional.empty(), i, j);
            }
        }
    }
}
