package infinituum.fastconfigapi.screens.utils.renderer;

import net.minecraft.resources.ResourceLocation;

public class SpriteComponent extends DrawableComponent implements Renderable {
    private final FastRenderer renderer;
    private final ResourceLocation spriteLocation;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public SpriteComponent(FastRenderer renderer, ResourceLocation spriteLocation, int x, int y, int width, int height) {
        this.renderer = renderer;
        this.spriteLocation = spriteLocation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render() {
        this.renderer.get().blitSprite(spriteLocation, x, y, width, height);
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
        return width;
    }

    @Override
    protected int getHeight() {
        return height;
    }
}
