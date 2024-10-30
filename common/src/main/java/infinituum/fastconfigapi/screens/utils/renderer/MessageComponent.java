package infinituum.fastconfigapi.screens.utils.renderer;

import infinituum.fastconfigapi.mixin.AbstractWidgetInvoker;
import infinituum.fastconfigapi.screens.utils.Color;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class MessageComponent extends DrawableComponent implements BoxComponent.Boxed<MessageComponent> {
    private final FastRenderer renderer;
    private final String message;
    private final MessageComponent parent;
    private final int x;
    private final int y;
    private int leftMargin;
    private Color messageColor;
    private boolean isScrolling;
    private int scrollingMaxWidth;

    public MessageComponent(FastRenderer renderer, MessageComponent parent, String message, int x, int y) {
        this.renderer = renderer;
        this.message = message;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.leftMargin = 0;
        this.isScrolling = false;
        this.messageColor = Color.white();
        this.scrollingMaxWidth = 100;
    }

    @Override
    protected int getX() {
        return x + leftMargin;
    }

    @Override
    protected int getY() {
        return y;
    }

    @Override
    protected int getWidth() {
        int textWidth = this.renderer.font().width(message);

        if (!isScrolling) {
            return textWidth;
        }

        int maxWidth = scrollingMaxWidth;

        if (textWidth + x + leftMargin < maxWidth) {
            maxWidth = textWidth;
        }

        return maxWidth;
    }

    @Override
    protected int getHeight() {
        return this.renderer.font().lineHeight;
    }

    @Override
    protected DrawableComponent getParent() {
        return parent;
    }

    @Override
    public DrawableComponent render() {
        if (!isScrolling) {
            drawString(message, messageColor);
        } else {
            drawScrollingString(message, messageColor);
        }

        return this.parent;
    }

    private void drawString(String message, Color color) {
        this.renderer.get().drawString(this.renderer.font(), message, x + leftMargin, y, color.toDecimal());
    }

    private void drawScrollingString(String message, Color color) {
        AbstractWidgetInvoker.invokeRenderScrollingString(
                this.renderer.get(),
                this.renderer.font(),
                Component.nullToEmpty(message),
                x + leftMargin,
                y - 1,
                getWidth() + x + leftMargin,
                y + getHeight(),
                color.toDecimal()
        );
    }

    public MessageComponent scrolling(int maxWidth) {
        this.isScrolling = true;
        this.scrollingMaxWidth = maxWidth;

        return this;
    }

    public MessageComponent color(Color color) {
        this.messageColor = color;

        return this;
    }

    @Override
    public MessageComponent boxed(Consumer<BoxComponent<?>> consumer) {
        consumer.accept(new BoxComponent<>(renderer, this, false));

        return this;
    }

    @Override
    public MessageComponent boxed(boolean includeTree, Consumer<BoxComponent<?>> consumer) {
        consumer.accept(new BoxComponent<>(renderer, this, includeTree));

        return this;
    }

    public MessageComponent append(String message, Consumer<MessageComponent> consumer) {
        consumer.accept(new MessageComponent(renderer, this, message, x + this.getWidth() + leftMargin, y));

        return this;
    }

    public MessageComponent leftMargin(int leftMargin) {
        this.leftMargin = leftMargin;

        return this;
    }
}
