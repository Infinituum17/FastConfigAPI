package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import infinituum.fastconfigapi.utils.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigOptionsList extends AbstractSelectionList<ConfigOptionsList.ConfigOptionEntry> implements Refreshable {
    private final Screen parent;
    private final ConfigSelectionModel model;
    private ConfigSelectionList configList;

    public ConfigOptionsList(Minecraft minecraft, Screen parent, int i, int j, int k, int l, ConfigSelectionModel model) {
        super(minecraft, i, j, k, l);

        this.parent = parent;
        this.model = model;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        if (focusNavigationEvent instanceof FocusNavigationEvent.TabNavigation tabNavigation) {
            if (tabNavigation.forward()) {
                Entry<ConfigOptionEntry> next = this.nextEntry(focusNavigationEvent.getVerticalDirectionForInitialFocus());

                if (next != null) {
                    return ComponentPath.path(this, ComponentPath.leaf(next));
                }

                return configList.nextFocusPath(focusNavigationEvent);
            }
        }

        return super.nextFocusPath(focusNavigationEvent);
    }

    private <T, U> ConfigOptionEntry translateSelected(Field field, T instance) {
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

        return new ConfigOptionEntry(new ConfigOption<>(field.getName(), getter, setter), this.minecraft, this);
    }

    private <T> List<ConfigOptionEntry> translateSelected() {
        List<ConfigOptionEntry> configOptionEntries = new ArrayList<>();
        T instance = (T) this.model.getSelected().getInstance();

        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigOptionEntry entry = translateSelected(field, instance);

            if (entry != null) {
                configOptionEntries.add(entry);
            }
        }

        return Collections.unmodifiableList(configOptionEntries);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        ConfigOptionEntry entry = this.getHovered();

        if (entry != null) {
            this.narrateListElementPosition(narrationElementOutput.nest(), entry);
            entry.updateNarration(narrationElementOutput);
        }
    }

    @Override
    public void refresh() {
        this.children().clear();

        List<ConfigOptionEntry> entries = translateSelected();

        if (!entries.isEmpty()) {
            this.children().addAll(entries);
        }
    }

    public void setConfigList(ConfigSelectionList list) {
        this.configList = list;
    }

    public static class ConfigOptionEntry extends ObjectSelectionList.Entry<ConfigOptionEntry> {
        private final ConfigOption<?> option;
        private final Minecraft minecraft;
        private final EditBox editBox;
        private final AbstractSelectionList<ConfigOptionEntry> parent;

        public ConfigOptionEntry(ConfigOption<?> option, Minecraft minecraft, AbstractSelectionList<ConfigOptionsList.ConfigOptionEntry> parent) {
            this.option = option;
            this.minecraft = minecraft;
            this.parent = parent;

            this.editBox = new EditBox(this.minecraft.font, 0, 0, 96, 16, Component.literal(option.getValue().toString()));
            this.editBox.setVisible(false);
            this.editBox.setEditable(true);
            this.editBox.setCanLoseFocus(true);
            this.editBox.setBordered(true);
            this.editBox.setMaxLength(32);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            guiGraphics.drawString(this.minecraft.font, Component.literal(this.option.getFieldName()), k, j, 0xFFFFFF);

            int padding = 5;
            int offset = this.minecraft.font.width(this.option.getFieldName());

            if (!this.editBox.isVisible()) {
                this.editBox.setVisible(true);
                this.editBox.setPosition(k + padding + offset, j);
            }

            this.editBox.render(guiGraphics, i, j, f);
        }

        @Override
        public void setFocused(boolean bl) {
            this.editBox.setFocused(bl);
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(option.getFieldName() + ": '" + option.getValue() + "'");
        }
    }
}
