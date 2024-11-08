package infinituum.fastconfigapi.screens.widgets.type;

import net.minecraft.client.Minecraft;

public interface Resizable {
    void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight);
}
