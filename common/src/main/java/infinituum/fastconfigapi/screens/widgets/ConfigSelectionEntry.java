package infinituum.fastconfigapi.screens.widgets;

import infinituum.fastconfigapi.api.FastConfigFile;
import infinituum.fastconfigapi.api.annotations.FastConfig;
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
        int itemHeight = this.manager.getItemHeight();
        int rowWidth = this.parent.getRowWidth();
        int leftPadding = k + itemHeight + 2;

        FastRenderer renderer = FastRenderer.startRender(guiGraphics, this.manager.getFont());

        renderer.sprite(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/config"),
                k,
                j - 2,
                itemHeight
        ).render();

        renderer.message(name, leftPadding, j + 2)
                .scrolling(rowWidth - 4)
                .render();

        renderer.message(modId, leftPadding, j + 2 + lineHeight + 4)
                .scrolling(rowWidth - 4)
                .color(0x5B97EF)
                .prefix("Mod id: ", 0xA0A0A0)
                .boxed()
                .outlineColor(0xFF5B97EF)
                .backgroundColor(0x995B97EF)
                .padding(2, 1)
                .render();

        renderer.message(side.toString(), leftPadding, j + 2 + (lineHeight + 4) * 2)
                .scrolling(rowWidth - 4)
                .color(0x84CF5D)
                .prefix("Side: ", 0xA0A0A0)
                .render();
    }

    public @NotNull FastConfigFile<?> getConfig() {
        return config;
    }
}
