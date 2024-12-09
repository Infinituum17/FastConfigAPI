package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.utils.renderer.widget.DynamicHeightObjectSelectionList;
import infinituum.fastconfigapi.screens.widgets.type.Refreshable;
import infinituum.fastconfigapi.screens.widgets.type.Repositionable;
import infinituum.fastconfigapi.screens.widgets.type.Resizable;
import infinituum.fastconfigapi.utils.ConfigOption;
import infinituum.fastconfigapi.utils.ConfigScreenManager;
import infinituum.fastconfigapi.utils.ConfigSelectionModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigOptionsList<T> extends DynamicHeightObjectSelectionList<ConfigOptionsListEntry<T>> implements Resizable, Refreshable, Repositionable {
    private final ConfigScreenManager manager;
    private final ConfigSelectionModel model;

    public ConfigOptionsList(ConfigScreenManager manager, ConfigSelectionModel model) {
        super(manager.getMinecraft(),
                manager.getOptionsWidth(),
                manager.getOptionsHeight(),
                manager.getTopPadding());

        this.manager = manager;
        this.model = model;
    }

    @Override
    public void refresh() {
        this.children().clear();

        List<ConfigOptionsListEntry<T>> entries = translateSelected();

        if (!entries.isEmpty()) {
            this.children().addAll(entries);
        }
    }

    private <U> List<ConfigOptionsListEntry<T>> translateSelected() {
        List<ConfigOptionsListEntry<T>> configOptionEntries = new ArrayList<>();
        U instance = (U) this.model.getSelected().getInstance();

        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigOptionsListEntry<T> entry = translateSelected(field, instance);

            if (entry != null) {
                configOptionEntries.add(entry);
            }
        }

        return Collections.unmodifiableList(configOptionEntries);
    }

    private <U> ConfigOptionsListEntry<T> translateSelected(Field field, U instance) {
        field.setAccessible(true);

        if (Modifier.isTransient(field.getModifiers())) {
            return null;
        }

        Supplier<T> getter = () -> {
            try {
                return (T) field.get(instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        Consumer<T> setter = (v) -> {
            try {
                if (isUnboxableArray(v)) {
                    field.set(instance, ArrayUtils.toPrimitive(v));
                } else {
                    field.set(instance, v);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        if (getter.get() instanceof List<?> list && list.isEmpty()) {
            return null;
        }

        return new ConfigOptionsListEntry<>(
                this.manager,
                new ConfigOption<>(getter, setter, this.manager.getFont()),
                this.model.getSelected().getMetadata().getField(field),
                this
        );
    }

    private boolean isUnboxableArray(T v) {
        return v instanceof Byte[]
                || v instanceof Short[]
                || v instanceof Integer[]
                || v instanceof Long[]
                || v instanceof Float[]
                || v instanceof Double[]
                || v instanceof Character[]
                || v instanceof Boolean[];
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
        ConfigOptionsListEntry<T> selected = this.getSelected();

        if (selected != null) {
            ComponentPath componentPath = selected.nextFocusPath(navigation);

            if (componentPath != null) {
                return componentPath;
            }
        }

        ConfigOptionsListEntry<T> nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            this.manager.getList().setFocused(null);
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
            this.setScrollAmount(this.getMaxScroll());
            return ComponentPath.path(list, ComponentPath.leaf(list.children().getLast()));
        }

        ConfigOptionsListEntry<T> selected = this.getSelected();

        if (selected != null) {
            ComponentPath componentPath = selected.nextFocusPath(navigation);

            if (componentPath != null) {
                return componentPath;
            }
        }

        ConfigOptionsListEntry<T> nextEntry = this.nextEntry(navigation.getVerticalDirectionForInitialFocus());

        if (nextEntry != null) {
            this.setScrollAmount(this.getMaxScroll());
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
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        boolean isProcessedClick = super.mouseClicked(d, e, i);

        if (isProcessedClick && this.manager.getList().getFocused() != null) {
            this.manager.getList().setFocused(null);
        }

        return isProcessedClick;
    }

    @Override
    public void reposition(HeaderAndFooterLayout layout) {
        this.updateSize(this.manager.getOptionsWidth(), layout);
        this.setX(this.manager.getOptionsX());
    }

    public void renderTooltip(GuiGraphics guiGraphics, int i, int j, float f) {
        ConfigOptionsListEntry<?> entry = this.getHovered();

        if (entry != null) {
            List<Component> tooltip = entry.getTooltip();

            if (!tooltip.isEmpty()) {
                guiGraphics.renderTooltip(this.minecraft.font, tooltip, Optional.empty(), i, j);
            }
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
        for (ConfigOptionsListEntry<T> child : this.children()) {
            child.resize(minecraft, width, height, this.getWidth(), this.getHeight(), elementHeight);
        }
    }
}
