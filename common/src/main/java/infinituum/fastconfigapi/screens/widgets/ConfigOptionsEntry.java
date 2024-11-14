package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.impl.ConfigMetadata;
import infinituum.fastconfigapi.screens.utils.renderer.Color;
import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import infinituum.fastconfigapi.screens.utils.renderer.widget.DynamicHeightObjectSelectionList;
import infinituum.fastconfigapi.screens.widgets.type.Resizable;
import infinituum.fastconfigapi.screens.widgets.wrappers.ArrayEditor;
import infinituum.fastconfigapi.screens.widgets.wrappers.ObjectEditor;
import infinituum.fastconfigapi.utils.ConfigOption;
import infinituum.fastconfigapi.utils.ListManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ConfigOptionsEntry<T> extends DynamicHeightObjectSelectionList.Entry<ConfigOptionsEntry<T>> implements Resizable {
    private final ListManager manager;
    private final ConfigOption<T> option;
    private final InputWidgetWrapper<T> inputWrapper;
    private final String name;
    private final String description;
    private final String[] tooltip;
    private final ConfigOptionsList<T> parent;
    private int itemHeight;

    public ConfigOptionsEntry(ListManager manager, ConfigOption<T> option, ConfigMetadata.ConfigFieldMetadata metadata, ConfigOptionsList<T> parent) {
        this.manager = manager;
        this.option = option;
        this.parent = parent;

        this.itemHeight = 0;

        this.name = metadata.name();
        this.description = metadata.description();
        this.tooltip = metadata.tooltip();

        this.inputWrapper = option.createWidgetWrapper(name, this.parent.getRowWidth());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        Color secondaryTextColor = Color.of(0xA0A0A0);
        boolean hasDescription = !Objects.equals(this.description, "");
        int rowWidth = this.parent.getRowWidth();
        int horizontalPadding = 8;
        int verticalPadding = 6;
        int inputX = k + rowWidth - (horizontalPadding * 2) - this.inputWrapper.getWidth();
        int maxTitleWidth = rowWidth - (horizontalPadding * 3) - 6 - this.inputWrapper.getWidth();
        int x = k + horizontalPadding;
        int y = j + verticalPadding;

        FastRenderer renderer = FastRenderer.startRender(guiGraphics, this.manager.getFont());

        renderer.message(name, x, y)
                .color(Color.white())
                .scrolling(maxTitleWidth)
                .boxed(component -> component.padding(2, 1)
                        .outlineColor(secondaryTextColor)
                        .render())
                .render();

        if (!this.inputWrapper.isVisible()) {
            this.inputWrapper.setVisible(true);
        }

        this.inputWrapper.setPosition(inputX, y - 4);
        this.inputWrapper.render(guiGraphics, i, j, f);

        y += this.inputWrapper.getTotalHeight();

        if (hasDescription) {
            int maxMultilineX = rowWidth - (horizontalPadding * 2) - 6;

            if (inputWrapper instanceof ArrayEditor<?>) {
                y -= this.inputWrapper.getTotalHeight();
                y += this.inputWrapper.getHeight();
                maxMultilineX = maxMultilineX - horizontalPadding - this.inputWrapper.getWidth();
            }

            if (inputWrapper instanceof ObjectEditor) {
                y -= this.inputWrapper.getTotalHeight();
                y += this.inputWrapper.getHeight();
                maxMultilineX = maxMultilineX - horizontalPadding - this.inputWrapper.getWidth();
            }

            int maxMultilineWidth = maxMultilineX - x;

            renderer.message(this.description, x, y)
                    .color(secondaryTextColor)
                    .multiline(maxMultilineWidth)
                    .render();

            y += FastRenderer.multilineStringHeight(this.manager.getFont(), this.description, maxMultilineX) + 8;
        }

        y += verticalPadding - 5;

        if (itemHeight == 0 || itemHeight != y - j) {
            this.itemHeight = y - j;
        }

        guiGraphics.hLine(k - 2, k + rowWidth, y - 2, 0x40FFFFFF);
    }

    @Override
    public int getItemHeight() {
        return this.itemHeight;
    }

    @Override
    public int approximateHeight() {
        int verticalPadding = 6;
        int widgetHeight = this.inputWrapper.getTotalHeight();
        int descriptionHeight = (this.description.isEmpty()) ? 0 : this.manager.getFont().lineHeight;

        return verticalPadding * 2 + widgetHeight + descriptionHeight;
    }

    @Override
    public void setFocused(boolean bl) {
        this.inputWrapper.setFocused(bl);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        boolean isProcessed = super.mouseClicked(d, e, i);

        if (isProcessed) {
            this.inputWrapper.onClick(d, e);
        }

        return isProcessed;
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.literal(name + ": '" + option.getValue() + "'");
    }

    public void save() {
        T value = this.inputWrapper.get();

        if (value != null) {
            this.option.setValue(value);
        }
    }

    public List<Component> getTooltip() {
        return stringsToComponents(this.tooltip);
    }

    private List<Component> stringsToComponents(String[] tooltip) {
        return Stream.of(tooltip).<Component>map(Component::literal).toList();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
        this.inputWrapper.resize(minecraft, width, height, listWidth, listHeight, this.getItemHeight());
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return this.inputWrapper.keyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return this.inputWrapper.keyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.inputWrapper.charTyped(c, i);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (inputWrapper instanceof ArrayEditor<?> arrayWrapper) {
            if (event instanceof FocusNavigationEvent.TabNavigation tabNavigation) {
                if (arrayWrapper.hasNextElement(tabNavigation)) {
                    return ComponentPath.path(this.parent, ComponentPath.leaf(this));
                }
            }
        }

        if (inputWrapper instanceof ObjectEditor objectEditor) {
            if (event instanceof FocusNavigationEvent.TabNavigation tabNavigation) {
                if (objectEditor.hasNextElement(tabNavigation)) {
                    return ComponentPath.path(this.parent, ComponentPath.leaf(this));
                }
            }
        }

        return super.nextFocusPath(event);
    }
}
