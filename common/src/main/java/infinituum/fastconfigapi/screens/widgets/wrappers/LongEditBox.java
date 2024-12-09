package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class LongEditBox extends GuardedEditBoxEditor<Long> {
    public LongEditBox(Font font, int i, int j, int k, int l, Component component, Long initValue) {
        super(font, i, j, k, l, component, LongEditBox::isValid, LongEditBox::get);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private static boolean isValid(String oldValue, String newValue) {
        if (oldValue.isEmpty() && newValue.equals("-")) {
            return true;
        }

        try {
            Long.parseLong(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long get(String value) {
        try {
            return Long.parseLong(value);
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
            Long.parseLong(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Long.MIN_VALUE : Long.MAX_VALUE));
        }
    }
}
