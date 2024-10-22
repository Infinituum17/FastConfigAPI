package infinituum.fastconfigapi.screens.utils.renderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class FastRenderer {
    private final GuiGraphics guiGraphics;
    private final Font font;

    private FastRenderer(GuiGraphics guiGraphics, Font font) {
        this.guiGraphics = guiGraphics;
        this.font = font;
    }

    public static FastRenderer startRender(GuiGraphics guiGraphics, Font font) {
        return new FastRenderer(guiGraphics, font);
    }

    protected Font font() {
        return font;
    }

    protected GuiGraphics get() {
        return guiGraphics;
    }

    public MessageComponent message(String message, int x, int y) {
        return new MessageComponent(this, message, x, y);
    }

    public RectComponent rect(int x1, int y1, int x2, int y2) {
        return new RectComponent(this, x1, y1, x2, y2);
    }

    public SpriteComponent sprite(ResourceLocation spriteLocation, int x, int y, int size) {
        return this.sprite(spriteLocation, x, y, size, size);
    }

    public SpriteComponent sprite(ResourceLocation spriteLocation, int x, int y, int width, int height) {
        return new SpriteComponent(this, spriteLocation, x, y, width, height);
    }

    public BoxComponent<?> box(int x1, int y1, int x2, int y2, int horizontalPadding, int verticalPadding) {
        return new BoxComponent<>(this, x1, y1, x2, y2, horizontalPadding, verticalPadding);
    }
}
