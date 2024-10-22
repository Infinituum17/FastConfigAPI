package infinituum.fastconfigapi.screens.utils.renderer;

import infinituum.fastconfigapi.mixin.AbstractWidgetInvoker;
import net.minecraft.network.chat.Component;

public class MessageComponent extends DrawableComponent implements Renderable {
    private final FastRenderer renderer;
    private final String message;
    private int x;
    private int y;
    private String prefix;
    private int prefixColor;
    private int messageColor;
    private boolean isScrolling;
    private int scrollingMaxWidth;

    public MessageComponent(FastRenderer renderer, String message, int x, int y) {
        this.renderer = renderer;
        this.message = message;
        this.x = x;
        this.y = y;
        this.isScrolling = false;
        this.prefix = "";
        this.prefixColor = 0xFFFFFF;
        this.messageColor = 0xFFFFFF;
        this.scrollingMaxWidth = 20;
    }

    @Override
    public void render() {
        if (!this.prefix.isEmpty()) {
            drawString(prefix, prefixColor);
            x += this.renderer.font().width(prefix);
        }

        if (!isScrolling) {
            drawString(message, messageColor);
        } else {
            drawScrollingString(message, messageColor);
        }
    }

    private void drawString(String message, int color) {
        this.renderer.get().drawString(this.renderer.font(), message, x, y, color);
    }

    private void drawScrollingString(String message, int color) {
        int maxWidth = scrollingMaxWidth;
        int textWidth = this.renderer.font().width(message) + x;

        if (textWidth < maxWidth) {
            maxWidth = textWidth;
        }

        AbstractWidgetInvoker.invokeRenderScrollingString(
                this.renderer.get(),
                this.renderer.font(),
                Component.nullToEmpty(message),
                x,
                y - 1,
                maxWidth,
                y + getHeight(),
                color);
    }

    @Override
    protected int getX() {
        return x;
    }

    @Override
    protected int getY() {
        return y;
    }

    @Override
    protected int getWidth() {
        int width = this.renderer.font().width(message);

        if (!prefix.isEmpty()) {
            width += this.renderer.font().width(prefix);
        }

        return width;
    }

    @Override
    protected int getHeight() {
        return this.renderer.font().lineHeight;
    }

    public MessageComponent scrolling(int maxWidth) {
        this.isScrolling = true;
        this.scrollingMaxWidth = maxWidth;

        return this;
    }

    public MessageComponent color(int color) {
        this.messageColor = color;

        return this;
    }

    public MessageComponent prefix(String prefix, int color) {
        if (prefix.isEmpty()) {
            return this;
        }

        this.prefix = prefix;
        this.prefixColor = color;

        return this;
    }

    public BoxComponent<MessageComponent> boxed() {
        return new BoxComponent<>(renderer, this);
    }
}
