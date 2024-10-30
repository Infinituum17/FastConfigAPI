package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class ArrayEditorWrapper<T> extends InputWidgetWrapper<T[]> {
    private final WrappersList<T> wrapperList;
    private final int singleBoxHeight;
    private int lineSpacing;

    public ArrayEditorWrapper(Font font, int i, int j, int k, int l, Component component, T[] initValue) {
        this.wrapperList = new WrappersList<>(font, initValue, component);
        this.singleBoxHeight = l;
        this.lineSpacing = 4;
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            return selected.keyPressed(i, j, k);
        }

        return false;
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            return selected.keyReleased(i, j, k);
        }

        return false;
    }

    @Override
    public boolean charTyped(char c, int i) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            return selected.charTyped(c, i);
        }

        return false;
    }

    @Override
    public void setFocused(boolean bl) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            selected.setFocused(bl);
        }
    }

    @Override
    public boolean isVisible() {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            return selected.isVisible();
        }

        return false;
    }

    @Override
    public void setVisible(boolean b) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            selected.setVisible(b);
        }
    }

    @Override
    public void setPosition(int i, int j) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            selected.setPosition(i, j);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.wrapperList.render(guiGraphics, i, j, f);
    }

    @Override
    public T[] get() {
        return this.wrapperList.get();
    }

    @Override
    public void onClick(double d, double e) {
        InputWidgetWrapper<T> selected = this.wrapperList.getSelected();

        if (selected != null) {
            selected.onClick(d, e);
        }
    }

    @Override
    public int requiredHeight() {
        int boxesHeight = this.wrapperList.size() * this.singleBoxHeight;
        int borderSize = 2;

        return borderSize + boxesHeight;
    }

    private int getLineSpacing() {
        return this.lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public class WrappersList<S> {
        private final List<InputWidgetWrapper<S>> list;
        private int selected;

        public WrappersList(Font font, S[] initValue, Component parentName) {
            this.selected = (initValue.length > 0) ? 0 : -1;
            this.list = this.composeList(font, initValue, parentName);
        }

        private List<InputWidgetWrapper<S>> composeList(Font font, S[] initValue, Component parentName) {
            List<InputWidgetWrapper<S>> widgets = new ArrayList<>();

            for (int i = 0; i < initValue.length; i++) {
                S element = initValue[i];
                String name = parentName.getString() + i;
                InputWidgetWrapper<S> widget = InputWidgetWrapper.createWidgetWrapper(element, font, name);

                widgets.add(widget);
            }

            return widgets;
        }

        public void render(GuiGraphics guiGraphics, int i, int j, float f) {
            for (int k = 0; k < list.size(); k++) {
                InputWidgetWrapper<S> el = list.get(k);
                el.render(guiGraphics, i, j, f);

                if (k + 1 < list.size()) {
                    j += ArrayEditorWrapper.this.getLineSpacing();
                }
            }
        }

        private InputWidgetWrapper<S> getSelected() {
            try {
                return this.list.get(this.selected);
            } catch (Exception e) {
                return null;
            }
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

        public S[] get() {
            List<S> values = new ArrayList<>();

            for (var element : list) {
                values.add(element.get());
            }

            return (S[]) values.toArray();
        }

        public int size() {
            return this.list.size();
        }
    }
}
