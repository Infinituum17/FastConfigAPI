package infinituum.fastconfigapi.screens.utils.renderer.widget;

import infinituum.fastconfigapi.screens.utils.renderer.type.GuardingFunction;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class GuardedEditBox extends EditBox {
    private final GuardingFunction guardingFunction;
    private Runnable postInsertionAction;
    private Runnable postSetAction;

    public GuardedEditBox(Font font, int i, int j, Component component, GuardingFunction guardingFunction) {
        super(font, i, j, component);

        this.guardingFunction = guardingFunction;
    }

    public GuardedEditBox(Font font, int i, int j, int k, int l, Component component, GuardingFunction guardingFunction) {
        super(font, i, j, k, l, component);

        this.guardingFunction = guardingFunction;
    }

    public GuardedEditBox(Font font, int i, int j, int k, int l, @Nullable EditBox editBox, Component component, GuardingFunction guardingFunction) {
        super(font, i, j, k, l, editBox, component);

        this.guardingFunction = guardingFunction;
    }

    public void addPostInsertionAction(Runnable postInsertionAction) {
        this.postInsertionAction = postInsertionAction;
    }

    public void addPostSetAction(Runnable postSetAction) {
        this.postSetAction = postSetAction;
    }

    @Override
    public void setValue(String string) {
        if (guardingFunction.isValid(string)) {
            super.setValue(string);

            if (postSetAction != null) {
                postSetAction.run();
            }
        }
    }

    @Override
    public void insertText(String string) {
        if (guardingFunction.isValid(string)) {
            super.insertText(string);

            if (postInsertionAction != null) {
                postInsertionAction.run();
            }
        }
    }
}
