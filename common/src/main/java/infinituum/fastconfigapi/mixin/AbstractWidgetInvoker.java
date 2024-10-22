package infinituum.fastconfigapi.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetInvoker {
    /**
     * AbstractWidget.renderScrollingString() invoker.
     *
     * @param guiGraphics {@link GuiGraphics}
     * @param font        {@link Font}
     * @param component   {@link Component} (text)
     * @param i           x1
     * @param j           y1
     * @param k           x2
     * @param l           y2
     * @param m           Hex color
     */
    @Invoker("renderScrollingString")
    static void invokeRenderScrollingString(GuiGraphics guiGraphics, Font font, Component component, int i, int j, int k, int l, int m) {
        throw new AssertionError();
    }
}
