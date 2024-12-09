package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.type.GuardedEditBoxEditor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class DoubleEditBox extends GuardedEditBoxEditor<Double> {
    public DoubleEditBox(Font font, int i, int j, int k, int l, Component component, Double initValue) {
        super(font, i, j, k, l, component, DoubleEditBox::isValid, DoubleEditBox::get);

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
            Double.parseDouble(newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Double get(String value) {
        try {
            return Double.parseDouble(value);
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
            Double.parseDouble(str);
        } catch (Exception e) {
            this.editBox.setValue(String.valueOf((str.charAt(0) == '-') ? Double.MIN_VALUE : Double.MAX_VALUE));
        }
    }
}
