package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class StringEditBox extends GuardedEditBoxEditor<String> {
    public StringEditBox(Font font, int i, int j, int k, int l, Component component, String initValue) {
        super(font, i, j, k, l, component, (o, n) -> true, (s) -> s);

        this.editBox.setValue(initValue);
    }
}
