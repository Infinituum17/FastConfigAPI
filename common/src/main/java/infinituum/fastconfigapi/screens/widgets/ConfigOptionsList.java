package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import infinituum.fastconfigapi.screens.utils.Refreshable;
import infinituum.fastconfigapi.screens.utils.Repositionable;
import infinituum.fastconfigapi.utils.ConfigOption;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigOptionsList extends ObjectSelectionList<ConfigOptionsEntry> implements Refreshable, Repositionable {
    private final ExpansionListManager manager;
    private final ConfigSelectionModel model;

    public ConfigOptionsList(ExpansionListManager manager) {
        super(manager.getMinecraft(),
                manager.getOptionsWidth(),
                manager.getOptionsHeight(),
                manager.getTopPadding(),
                manager.getItemHeight());

        this.manager = manager;
        this.model = manager.getModel();
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    public void setSelected(@Nullable ConfigOptionsEntry entry) {
        super.setSelected(entry);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        boolean isProcessedClick = super.mouseClicked(d, e, i);

        if (isProcessedClick) {
            this.manager.getList().setFocused(null);
        }

        return isProcessedClick;
    }

    @Override
    public void refresh() {
        this.children().clear();

        List<ConfigOptionsEntry> entries = translateSelected();

        if (!entries.isEmpty()) {
            this.children().addAll(entries);
        }
    }

    private <T> List<ConfigOptionsEntry> translateSelected() {
        List<ConfigOptionsEntry> configOptionEntries = new ArrayList<>();
        T instance = (T) this.model.getSelected().getInstance();

        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigOptionsEntry entry = translateSelected(field, instance);

            if (entry != null) {
                configOptionEntries.add(entry);
            }
        }

        return Collections.unmodifiableList(configOptionEntries);
    }

    private <T, U> ConfigOptionsEntry translateSelected(Field field, T instance) {
        field.setAccessible(true);

        if (Modifier.isTransient(field.getModifiers())) {
            return null;
        }

        Supplier<U> getter = () -> {
            try {
                return (U) field.get(instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        Consumer<U> setter = (v) -> {
            try {
                field.set(instance, v);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        return new ConfigOptionsEntry(this.manager, new ConfigOption<>(field.getName(), getter, setter));
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (event instanceof FocusNavigationEvent.TabNavigation tabNavigation) {
            if (tabNavigation.forward()) {
                return tabForward(tabNavigation);
            } else {
                return tabBackwards(tabNavigation);
            }
        } else if (event instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
            return arrowNavigate(arrowNavigation);
        }

        return super.nextFocusPath(event);
    }

    private ComponentPath tabForward(FocusNavigationEvent.TabNavigation navigation) {
        ConfigOptionsEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            return ComponentPath.path(this, ComponentPath.leaf(nextEntry));
        }

        ConfigSelectionList list = this.manager.getList();
        ComponentPath nextListEntry = list.nextFocusPath(navigation);

        if (nextListEntry != null) {
            this.setFocused(null);
            return nextListEntry;
        }

        return null;
    }

    private ComponentPath tabBackwards(FocusNavigationEvent.TabNavigation navigation) {
        ConfigSelectionList list = this.manager.getList();
        
        if (!this.isFocused() && !list.isFocused()) {
            return ComponentPath.path(list, ComponentPath.leaf(list.children().getLast()));
        }

        ConfigOptionsEntry nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            return ComponentPath.path(this, ComponentPath.leaf(nextEntry));
        }

        this.setSelected(null);
        this.setFocused(null);

        return list.nextFocusPath(navigation);
    }

    private ComponentPath arrowNavigate(FocusNavigationEvent.ArrowNavigation arrowNavigation) {
        return super.nextFocusPath(arrowNavigation);
    }

    @Override
    public void reposition(HeaderAndFooterLayout layout) {
        this.updateSize(this.manager.getOptionsWidth(), layout);
        this.setX(this.manager.getOptionsX());
    }
}
