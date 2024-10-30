package infinituum.fastconfigapi.screens.utils.renderer;

public abstract class DrawableComponent {

    protected abstract int getX();

    protected abstract int getY();

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract DrawableComponent getParent();

    public abstract DrawableComponent render();
}
