package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class IntegerEditor extends GuardedEditBoxEditor<Integer> {
    public IntegerEditor(Font font, int i, int j, int k, int l, Component component, Integer initValue) {
        super(font, i, j, k, l, component, IntegerEditor::isValid, IntegerEditor::get);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private static boolean isValid(String oldValue, String newValue) {
        if (oldValue.isEmpty() && newValue.equals("-")) {
            return true;
        }

        try {
            Integer.parseInt(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer get(String value) {
        try {
            return Integer.parseInt(value);
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
            Integer.parseInt(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Integer.MIN_VALUE : Integer.MAX_VALUE));
        }
    }
}
