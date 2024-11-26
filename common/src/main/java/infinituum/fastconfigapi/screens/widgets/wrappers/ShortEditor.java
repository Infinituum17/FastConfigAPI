package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class ShortEditor extends GuardedEditBoxEditor<Short> {
    public ShortEditor(Font font, int i, int j, int k, int l, Component component, Short initValue) {
        super(font, i, j, k, l, component, ShortEditor::isValid, ShortEditor::get);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private static boolean isValid(String oldValue, String newValue) {
        if (oldValue.isEmpty() && newValue.equals("-")) {
            return true;
        }

        try {
            Short.parseShort(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Short get(String value) {
        try {
            return Short.parseShort(value);
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
            Short.parseShort(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Short.MIN_VALUE : Short.MAX_VALUE));
        }
    }
}
