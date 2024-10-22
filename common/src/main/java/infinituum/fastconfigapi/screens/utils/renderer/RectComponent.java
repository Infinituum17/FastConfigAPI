package infinituum.fastconfigapi.screens.utils.renderer;

public class RectComponent extends DrawableComponent implements Renderable {
    private final FastRenderer renderer;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int color;

    public RectComponent(FastRenderer renderer, int x1, int y1, int x2, int y2) {
        this.renderer = renderer;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.color = 0x55FFFFFF;
    }

    public RectComponent color(int color) {
        this.color = color;

        return this;
    }

    @Override
    public void render() {
        this.renderer.get().fill(x1, y1, x2, y2, color);
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
