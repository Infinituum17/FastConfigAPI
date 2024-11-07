package infinituum.fastconfigapi.screens.utils.renderer;

import java.util.function.Consumer;

public class BoxComponent<T extends DrawableComponent> extends DrawableComponent {
    private final RectComponent background;
    private final FastRenderer renderer;
    private final T parent;
    private final int baseX1;
    private final int baseY1;
    private final int baseX2;
    private final int baseY2;
    private final boolean includeTree;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private Color outlineColor;
    private int horizontalPadding;
    private int verticalPadding;

    public BoxComponent(FastRenderer renderer, T parent, boolean includeTree) {
        this(
                renderer,
                parent,
                includeTree,
                parent.getX(),
                parent.getY(),
                parent.getX() + parent.getWidth(),
                parent.getY() + parent.getHeight(),
                0,
                0,
                Color.white()
        );

        this.background.color(Color.white().withAlpha(0x55));
    }

    public BoxComponent(FastRenderer renderer, T parent, boolean includeTree, int baseX1, int baseY1, int baseX2, int baseY2, int horizontalPadding, int verticalPadding, Color outlineColor) {
        this.renderer = renderer;
        this.parent = parent;
        this.baseX1 = baseX1;
        this.baseY1 = baseY1;
        this.baseX2 = baseX2;
        this.baseY2 = baseY2;
        this.background = new RectComponent(renderer, this);
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
        this.includeTree = includeTree;
        this.outlineColor = outlineColor;

        this.calculateCoords();
    }

    private void calculateCoords() {
        if (includeTree) {
            DrawableComponent parentComponent = parent;

            while (parentComponent.getParent() != null) {
                parentComponent = parentComponent.getParent();
            }

            this.x1 = parentComponent.getX() - horizontalPadding;
            this.y1 = parentComponent.getY() - verticalPadding;
        } else {
            this.x1 = baseX1 - horizontalPadding;
            this.y1 = baseY1 - verticalPadding;
        }

        this.background.setX1(x1);
        this.background.setY1(y1);

        this.x2 = baseX2 + horizontalPadding;
        this.y2 = baseY2 + verticalPadding;
        this.background.setX2(x2);
        this.background.setY2(y2);
    }

    public BoxComponent<T> padding(int horizontalPadding, int verticalPadding) {
        horizontalPadding(horizontalPadding);
        return verticalPadding(verticalPadding);
    }

    public BoxComponent<T> horizontalPadding(int horizontalPadding) {
        this.horizontalPadding = horizontalPadding;

        this.calculateCoords();

        return this;
    }

    public BoxComponent<T> verticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;

        this.calculateCoords();

        return this;
    }

    public BoxComponent<T> outlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;

        return this;
    }

    public BoxComponent<T> backgroundColor(Color backgroundColor) {
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

    @Override
    protected DrawableComponent getParent() {
        return parent;
    }

    @Override
    public DrawableComponent render() {
        this.calculateCoords();

        this.background.render();
        this.drawOutline();

        return this.parent;
    }

    private void drawOutline() {
        this.renderer.get().hLine(x1, x2 - 1, y1 - 1, outlineColor.toDecimal());
        this.renderer.get().hLine(x1, x2 - 1, y2 - 1, outlineColor.toDecimal());
        this.renderer.get().vLine(x1 - 1, y1 - 1, y2 - 1, outlineColor.toDecimal());
        this.renderer.get().vLine(x2, y1 - 1, y2 - 1, outlineColor.toDecimal());
    }

    public interface Boxed<T extends DrawableComponent> {
        T boxed(Consumer<BoxComponent<?>> consumer);

        T boxed(boolean includeTree, Consumer<BoxComponent<?>> consumer);
    }
}
