package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.impl.ConfigMetadata;
import infinituum.fastconfigapi.screens.utils.renderer.Color;
import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import infinituum.fastconfigapi.screens.utils.renderer.widget.DynamicHeightObjectSelectionList;
import infinituum.fastconfigapi.utils.ListManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ConfigSelectionEntry extends DynamicHeightObjectSelectionList.Entry<ConfigSelectionEntry> {
    private final ListManager manager;
    private final FastConfigFile<?> config;
    private final ConfigSelectionList parent;
    private final String modId;
    private final String name;
    private final String description;
    private final String[] tooltip;
    private final FastConfig.Side side;
    private int itemHeight;
    private boolean isItemHeightComputed;

    public ConfigSelectionEntry(ListManager minecraft, @NotNull FastConfigFile<?> config, ConfigSelectionList parent) {
        this.manager = minecraft;
        this.config = config;
        this.parent = parent;
        this.modId = config.getModId();

        ConfigMetadata<?> metadata = config.getMetadata();

        this.name = metadata.getName();
        this.description = metadata.getDescription();
        this.tooltip = metadata.getTooltip();
        this.side = config.getSide();

        this.itemHeight = 0;
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
        int lineHeight = this.manager.getFont().lineHeight;
        int itemHeight = this.getItemHeight();
        int imageSize = this.manager.getImageSize();
        int rowWidth = this.parent.getRowWidth();
        int leftPadding = k + imageSize + 4;
        int horizontalPadding = 2;
        int verticalPadding = 1;
        int lineSpacing = lineHeight + 4;
        int maxScrollingDistance = rowWidth - (leftPadding + 8);
        int margin = 1;
        int alphaAttenuation = 0x55;
        double darkeningFactor = 0.8;

        Color secondaryTextColor = Color.of(0xA0A0A0);
        Color modIdColor = Color.of(0x5B97EF);

        FastRenderer renderer = FastRenderer.startRender(guiGraphics, this.manager.getFont());

        renderer.sprite(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/config"),
                k + 2,
                j + (itemHeight / 2) - (imageSize / 2) - 1,
                imageSize - 2
        ).render();

        int y = j + 4;

        renderer.message(name, leftPadding, y)
                .scrolling(maxScrollingDistance)
                .boxed((box) -> box.padding(horizontalPadding, verticalPadding)
                        .outlineColor(secondaryTextColor)
                        .render())
                .render();

        y += 1 + lineSpacing;

        if (!description.isEmpty()) {
            renderer.message(description, leftPadding, y)
                    .scrolling(maxScrollingDistance)
                    .render();

            y += 2 + lineSpacing;
        }

        renderer.message("Mod id: ", leftPadding, y)
                .color(secondaryTextColor)
                .append(modId, (message) -> message.leftMargin(margin)
                        .color(modIdColor)
                        .scrolling(maxScrollingDistance - this.manager.getFont().width("Mod id: ") - 1)
                        .boxed((box) -> box.outlineColor(modIdColor.withLuminance(darkeningFactor))
                                .backgroundColor(modIdColor.withAlpha(alphaAttenuation))
                                .padding(horizontalPadding, verticalPadding)
                                .render())
                        .render())
                .render();

        y += 1 + lineSpacing;

        renderer.message("Side: ", leftPadding, y)
                .color(secondaryTextColor)
                .append(side.toString(), (message) -> message.leftMargin(margin)
                        .color(side.getColor())
                        .boxed((box) -> box.outlineColor(side.getColor().withLuminance(darkeningFactor))
                                .backgroundColor(side.getColor().withAlpha(alphaAttenuation))
                                .padding(horizontalPadding, verticalPadding)
                                .render())
                        .render())
                .render();

        addHeight(y + lineSpacing + 3);
        addHeight(-j);

        computeItemHeight();
    }

    @Override
    public int getItemHeight() {
        return this.itemHeight;
    }

    @Override
    public int approximateHeight() {
        int verticalPadding = 4;
        int textHeight = this.manager.getFont().lineHeight;

        return verticalPadding * 2
                + ((this.description.isEmpty()) ? 0 : textHeight)
                + textHeight * 3;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
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

    public @NotNull FastConfigFile<?> getConfig() {
        return config;
    }

    public List<Component> getTooltip() {
        return stringsToComponents(this.tooltip);
    }

    private List<Component> stringsToComponents(String[] tooltip) {
        return Stream.of(tooltip).<Component>map(Component::literal).toList();
    }
}
