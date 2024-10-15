package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.screens.ConfigSelectionScreen;
import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ConfigSelectionList extends ObjectSelectionList<ConfigSelectionList.FastConfigEntry> implements Refreshable {
    private final ConfigSelectionScreen parent;
    private final ConfigSelectionModel model;
    private ConfigOptionsList configOptions;

    /**
     * {@code x} = 0 (default, unless set via setX)<br/>
     * {@code k} -> y<br/>
     * {@code i} -> width<br/>
     * {@code j} -> height<br/>
     * {@code l} -> itemHeight
     */
    public ConfigSelectionList(Minecraft minecraft, ConfigSelectionScreen parent, int i, int j, int k, int l, ConfigSelectionModel model) {
        super(minecraft, i, j, k, l);

        this.parent = parent;
        this.model = model;
    }

    /* Size of one list element (width) from center */
    @Override
    public int getRowWidth() {
        return this.width; // this.width -> width of the whole list
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }

    @Override
    public void setSelected(@Nullable ConfigSelectionList.FastConfigEntry entry) {
        super.setSelected(entry);

        if (entry != null) {
            this.model.setSelected(entry.getConfig());
        }

        configOptions.refresh();
    }

    @Override
    public void refresh() {
        this.children().clear();
        this.setSelected(null);

        this.model.getConfigs().forEach(config -> {
            var entry = new FastConfigEntry(this.minecraft, config, this);
            this.children().add(entry);

            if (config.equals(this.model.getSelected())) {
                this.setFocused(entry);
            }
        });
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        if (focusNavigationEvent instanceof FocusNavigationEvent.TabNavigation tabNavigation) {
            if (tabNavigation.forward()) {
                if (this.getItemCount() == 0) {
                    return ComponentPath.leaf(this.parent.getDoneButton());
                }

                if (this.isFocused()) {
                    ComponentPath componentPath = this.configOptions.nextFocusPath(focusNavigationEvent);

                    if (componentPath != null) {
                        return componentPath;
                    }
                }

                if (!this.isFocused()) {
                    FastConfigEntry next = this.nextEntry(focusNavigationEvent.getVerticalDirectionForInitialFocus());

                    if (next != null) {
                        return ComponentPath.path(this, ComponentPath.leaf(next));
                    }

                    return ComponentPath.leaf(this.parent.getDoneButton());
                }

                return super.nextFocusPath(focusNavigationEvent);
            }
        }

        return null;
    }

    public void setConfigOptions(ConfigOptionsList configOptions) {
        this.configOptions = configOptions;
    }

    public static class FastConfigEntry extends ObjectSelectionList.Entry<FastConfigEntry> {
        private final Minecraft minecraft;
        private final FastConfigFile<?> config;
        private final ConfigSelectionList parent;
        private final String modId;
        private final String name;

        public FastConfigEntry(Minecraft minecraft, @NotNull FastConfigFile<?> config, ConfigSelectionList parent) {
            this.minecraft = minecraft;
            this.config = config;
            this.parent = parent;

            this.modId = config.getModId();
            this.name = config.getHumanReadableName();
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.config.getFileName());
        }

        /**
         * @param i  -> x
         * @param j  -> y
         * @param k
         * @param l
         * @param m
         * @param n  -> mx
         * @param o  -> my
         * @param bl
         * @param f
         */
        @Override
        public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            int itemHeight = this.parent.itemHeight;
            int leftPadding = k + itemHeight;

            guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/config"), k, j - 2, itemHeight, itemHeight);

            guiGraphics.drawString(
                    this.minecraft.font,
                    this.minecraft.font.plainSubstrByWidth(name, this.parent.getRowWidth() - 2 - leftPadding),
                    leftPadding,
                    j + 2,
                    0xFFFFFF);

            guiGraphics.drawString(
                    this.minecraft.font,
                    this.minecraft.font.plainSubstrByWidth(modId, this.parent.getRowWidth() - 2 - leftPadding),
                    leftPadding,
                    j + 2 + this.minecraft.font.lineHeight + 4,
                    0xA0A0A0);
        }

        public @NotNull FastConfigFile<?> getConfig() {
            return config;
        }
    }
}
