package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
import infinituum.fastconfigapi.screens.utils.Color;
import infinituum.fastconfigapi.screens.utils.ExpansionListManager;
import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ConfigSelectionEntry extends ObjectSelectionList.Entry<ConfigSelectionEntry> {
    private final ExpansionListManager manager;
    private final FastConfigFile<?> config;
    private final ConfigSelectionList parent;
    private final String modId;
    private final String name;
    private final FastConfig.Side side;

    public ConfigSelectionEntry(ExpansionListManager minecraft, @NotNull FastConfigFile<?> config, ConfigSelectionList parent) {
        this.manager = minecraft;
        this.config = config;
        this.parent = parent;

        this.modId = config.getModId();
        this.name = config.getHumanReadableName();
        this.side = config.getSide();
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
        int itemHeight = this.manager.getListItemHeight();
        int rowWidth = this.parent.getRowWidth();
        int leftPadding = k + itemHeight + 4;
        int horizontalPadding = 2;
        int verticalPadding = 1;
        int lineSpacing = lineHeight + 4;
        int maxScrollingDistance = rowWidth - 4;
        int margin = 1;
        int alphaAttenuation = 0x55;
        double darkeningFactor = 0.8;

        Color secondaryTextColor = Color.of(0xA0A0A0);
        Color modIdColor = Color.of(0x5B97EF);

        FastRenderer renderer = FastRenderer.startRender(guiGraphics, this.manager.getFont());

        renderer.sprite(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/config"),
                k + 2,
                j - 1,
                itemHeight - 2
        ).render();

        int y = j + 4;

        renderer.message(name, leftPadding, y)
                .scrolling(maxScrollingDistance)
                .boxed((box) -> box.padding(horizontalPadding, verticalPadding)
                        .outlineColor(secondaryTextColor)
                        .render())
                .render();

        y += 3 + lineSpacing;

        renderer.message("Mod id: ", leftPadding, y)
                .color(secondaryTextColor)
                .append(modId, (message) -> message.leftMargin(margin)
                        .color(modIdColor)
                        .scrolling(maxScrollingDistance)
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
    }

    public @NotNull FastConfigFile<?> getConfig() {
        return config;
    }
}
