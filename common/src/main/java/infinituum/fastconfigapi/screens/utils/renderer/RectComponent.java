package infinituum.fastconfigapi.screens.utils.renderer;

import infinituum.fastconfigapi.screens.utils.Color;

public class RectComponent extends DrawableComponent {
    private final FastRenderer renderer;
    private final DrawableComponent parent;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color color;

    protected RectComponent(FastRenderer renderer, DrawableComponent parent) {
        this.renderer = renderer;
        this.parent = parent;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public RectComponent(FastRenderer renderer, DrawableComponent parent, int x1, int y1, int x2, int y2) {
        this.renderer = renderer;
        this.parent = parent;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.color = Color.white().withAlpha(0x55);
    }

    public RectComponent color(Color color) {
        this.color = color;

        return this;
    }

    @Override
    protected int getX() {
        return x1;
    }

    @Override
    protected int getY() {
        return y1;
    }

    @Override
    protected int getWidth() {
        return x2 - x1;
    }

    @Override
    protected int getHeight() {
        return y2 - y1;
    }

    @Override
    protected DrawableComponent getParent() {
        return parent;
    }

    @Override
    public DrawableComponent render() {
        this.renderer.get().fill(x1, y1, x2, y2, color.toDecimal());

        return this.parent;
    }

    protected void setX1(int x) {
        this.x1 = x;
    }

    protected void setX2(int x) {
        this.x2 = x;
    }

    protected void setY1(int y) {
        this.y1 = y;
    }

    protected void setY2(int y) {
        this.y2 = y;
    }
}
