package infinituum.fastconfigapi.screens.utils.renderer;

public class BoxComponent<T extends DrawableComponent & Renderable> extends DrawableComponent implements Renderable {
    private final RectComponent background;
    private final FastRenderer renderer;
    private final T parent;
    private int x2;
    private int y2;
    private int outlineColor;
    private int x1;
    private int y1;
    private int horizontalPadding;
    private int verticalPadding;

    public BoxComponent(FastRenderer renderer, int x1, int y1, int x2, int y2, int horizontalPadding, int verticalPadding) {
        this(renderer, null);

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
    }

    public BoxComponent(FastRenderer renderer, T parent) {
        this.renderer = renderer;
        this.parent = parent;

        if (parent != null) {
            this.x1 = this.parent.getX();
            this.y1 = this.parent.getY();
            this.x2 = x1 + this.parent.getWidth();
            this.y2 = y1 + this.parent.getHeight();
        }

        this.background = new RectComponent(renderer, x1, y1, x2, y2);
        this.horizontalPadding = 0;
        this.verticalPadding = 0;
        this.outlineColor = 0x99FFFFFF;
    }

    @Override
    public void render() {
        this.background.render();
        this.drawOutline();

        if (this.parent != null) {
            this.parent.render();
        }
    }

    private void drawOutline() {
        this.renderer.get().hLine(x1, x2 - 1, y1 - 1, outlineColor);
        this.renderer.get().hLine(x1, x2 - 1, y2 - 1, outlineColor);
        this.renderer.get().vLine(x1 - 1, y1 - 1, y2 - 1, outlineColor);
        this.renderer.get().vLine(x2, y1 - 1, y2 - 1, outlineColor);
    }

    public BoxComponent<T> padding(int horizontalPadding, int verticalPadding) {
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;

        this.x1 = this.parent.getX() - horizontalPadding;
        this.y1 = this.parent.getY() - verticalPadding;
        this.x2 = this.parent.getX() + this.parent.getWidth() + horizontalPadding;
        this.y2 = this.parent.getY() + this.parent.getHeight() + verticalPadding;

        this.background.setX1(x1);
        this.background.setX2(x2);
        this.background.setY1(y1);
        this.background.setY2(y2 - 1);

        return this;
    }

    public BoxComponent<T> horizontalPadding(int horizontalPadding) {
        this.horizontalPadding = horizontalPadding;

        this.x1 = this.parent.getX() - horizontalPadding;
        this.x2 = this.parent.getX() + this.parent.getWidth() + horizontalPadding;

        this.background.setX1(x1);
        this.background.setX2(x2);

        return this;
    }

    public BoxComponent<T> verticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;

        this.y1 = this.parent.getY() - verticalPadding;
        this.y2 = this.parent.getY() + this.parent.getHeight() + verticalPadding;

        this.background.setY1(y1);
        this.background.setY2(y2 - 1);

        return this;
    }

    public BoxComponent<T> outlineColor(int outlineColor) {
        this.outlineColor = outlineColor;

        return this;
    }

    public BoxComponent<T> backgroundColor(int backgroundColor) {
        this.background.color(backgroundColor);

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
}
