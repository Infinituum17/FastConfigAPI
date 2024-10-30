package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.screens.utils.Color;
import infinituum.fastconfigapi.screens.utils.ExpansionListManager;
import infinituum.fastconfigapi.screens.utils.InputWidgetWrapper;
import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import infinituum.fastconfigapi.screens.widgets.custom.DynamicHeightObjectSelectionList;
import infinituum.fastconfigapi.utils.ConfigOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ConfigOptionsEntry<T> extends DynamicHeightObjectSelectionList.Entry<ConfigOptionsEntry<T>> {
    private final ExpansionListManager manager;
    private final ConfigOption<T> option;
    private final InputWidgetWrapper<T> inputWrapper;
    private boolean isItemHeightComputed;
    private int itemHeight;

    public ConfigOptionsEntry(ExpansionListManager manager, ConfigOption<T> option) {
        this.manager = manager;
        this.option = option;
        this.inputWrapper = (InputWidgetWrapper<T>) option.getWidgetWrapper();

        this.itemHeight = 0;
        this.isItemHeightComputed = false;
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

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        int horizontalPadding = 4;
        int verticalPadding = 4;
        int x = k + horizontalPadding;
        int y = j + verticalPadding;
        int lineHeight = this.manager.getFont().lineHeight;
        Color secondaryTextColor = Color.of(0xA0A0A0);

        FastRenderer renderer = FastRenderer.startRender(guiGraphics, this.manager.getFont());

        renderer.message(this.option.getFieldName(), x, y)
                .color(Color.white())
                .boxed(component -> component.padding(2, 1)
                        .outlineColor(secondaryTextColor)
                        .render())
                .render();

        y += lineHeight + verticalPadding;

        if (!this.inputWrapper.isVisible()) {
            this.inputWrapper.setVisible(true);
        }

        this.inputWrapper.setPosition(x, y);
        this.inputWrapper.render(guiGraphics, i, j, f);

        addHeight(this.inputWrapper.requiredHeight());

        addHeight(y + verticalPadding);
        addHeight(-j);

        computeItemHeight();
    }

    @Override
    public int getItemHeight() {
        return this.itemHeight;
    }

    @Override
    public void setFocused(boolean bl) {
        this.inputWrapper.setFocused(bl);
    }

    private void addHeight(int height) {
        if (!isItemHeightComputed) {
            itemHeight += height;
        }
    }

    private void computeItemHeight() {
        if (!isItemHeightComputed) {
            isItemHeightComputed = true;
        }
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
        return Component.literal(option.getFieldName() + ": '" + option.getValue() + "'");
    }

    public void save() {
        T value = this.inputWrapper.get();

        if (value != null) {
            this.option.setValue(value);
        }
    }
}
