package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class FloatEditBox extends GuardedEditBoxEditor<Float> {
    public FloatEditBox(Font font, int i, int j, int k, int l, Component component, Float initValue) {
        super(font, i, j, k, l, component, FloatEditBox::isValid, FloatEditBox::get);

        this.editBox.setValue(String.valueOf(initValue));
        this.editBox.addPostInsertionAction(this::postInsertion);
    }

    private static boolean isValid(String oldValue, String newValue) {
        if (oldValue.isEmpty() && newValue.equals("-")) {
            return true;
        }

        if (!oldValue.contains(".") && newValue.equals(".")) {
            return true;
        }

        try {
            Float.parseFloat(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Float get(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return null;
        }
    }

    private void postInsertion() {
        String str = this.editBox.getValue();

        if (str.equals("-")) {
            return;
        }

        if (str.equals(".")) {
            return;
        }

        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Float.MIN_VALUE : Float.MAX_VALUE));
        }
    }
}
