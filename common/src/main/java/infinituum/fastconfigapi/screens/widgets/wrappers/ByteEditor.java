package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class ByteEditor extends GuardedEditBoxEditor<Byte> {
    public ByteEditor(Font font, int i, int j, int k, int l, Component component, Byte initValue) {
        super(font, i, j, k, l, component, ByteEditor::isValid, ByteEditor::get);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private static boolean isValid(String oldValue, String newValue) {
        if (oldValue.isEmpty() && newValue.equals("-")) {
            return true;
        }

        try {
            Byte.parseByte(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Byte get(String value) {
        try {
            return Byte.parseByte(value);
        } catch (Exception e) {
            return null;
        }
    }

    private void postInsertion() {
        String str = this.editBox.getValue();

        if (str.equals("-")) {
            return;
        }

        try {
            Byte.parseByte(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Byte.MIN_VALUE : Byte.MAX_VALUE));
        }
    }
}
