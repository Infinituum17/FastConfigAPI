package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import infinituum.fastconfigapi.screens.widgets.type.CompoundEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class ArrayEditor<T> extends InputWidgetWrapper<T[]> implements CompoundEditor {
    private final WrappersList<T> wrapperList;
    private final int singleBoxHeight;
    private final int listWidth;
    private int lineSpacing;

    public ArrayEditor(Font font, int i, int j, int k, int l, Component component, T[] initValue, int width) {
        this.wrapperList = new WrappersList<>(font, initValue, component);
        this.singleBoxHeight = l;
        this.lineSpacing = 4;
        this.listWidth = width;
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
        this.wrapperList.setPosition(i, j);
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
        this.wrapperList.onClick(d, e);
    }

    @Override
    public int getTotalHeight() {
        int boxesHeight = this.wrapperList.size() * this.singleBoxHeight + (this.wrapperList.size() - 1) * this.lineSpacing - 2;
        int borderSize = 2;

        return borderSize + boxesHeight;
    }

    @Override
    public int getHeight() {
        return this.wrapperList.getHeight();
    }

    @Override
    public int getWidth() {
        return this.wrapperList.getWidth();
    }

    @Override
    public int getX() {
        return this.wrapperList.getX();
    }

    @Override
    public int getY() {
        return this.wrapperList.getY();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
        this.wrapperList.resize(minecraft, width, height, listWidth, listHeight, elementHeight);
    }

    private int getLineSpacing() {
        return this.lineSpacing;
    }

    @Override
    public boolean hasNextElement(FocusNavigationEvent.TabNavigation tabNavigation) {
        return this.wrapperList.setNextElement(tabNavigation);
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
                InputWidgetWrapper<S> widget = createWidgetWrapper(element, font, name, listWidth);

                widgets.add(widget);
            }

            return widgets;
        }

        public void render(GuiGraphics guiGraphics, int i, int j, float f) {
            for (int k = 0; k < list.size(); k++) {
                InputWidgetWrapper<S> el = list.get(k);
                el.render(guiGraphics, i, j, f);

                if (k + 1 < list.size()) {
                    j += ArrayEditor.this.getLineSpacing();
                }
            }
        }

        public S[] get() {
            List<S> values = new ArrayList<>();

            for (var element : list) {
                values.add(element.get());
            }

            return (S[]) values.toArray();
        }

        public void setPosition(int i, int j) {
            int padding = 0;

            for (var element : this.list) {
                element.setPosition(i, j + padding);

                padding += ArrayEditor.this.getLineSpacing() + 16;
            }
        }

        public boolean setNextElement(FocusNavigationEvent.TabNavigation tabNavigation) {
            if (this.getSelected() instanceof CompoundEditor editor) {
                if (editor.hasNextElement(tabNavigation)) {
                    return true;
                }
            }

            int i = tabNavigation.forward() ? this.selected + 1 : this.selected - 1;

            if (i >= 0 && i < this.size()) {
                this.setSelected(i);

                return true;
            }

            return false;
        }

        private InputWidgetWrapper<S> getSelected() {
            try {
                return this.list.get(this.selected);
            } catch (Exception e) {
                return null;
            }
        }

        public int size() {
            return this.list.size();
        }

        public void setSelected(int selected) {
            ArrayEditor.this.setFocused(false);

            if (selected >= 0 && selected < this.size()) {
                this.selected = selected;
            } else {
                throw new IndexOutOfBoundsException("WrappersList widget index is out of bounds");
            }
        }

        public void onClick(double d, double e) {
            if (!this.list.isEmpty()) {
                int height = getTotalHeight() - 2;
                int y = (int) (e - this.getY());

                if (y >= 0 && y <= height) {
                    for (int i = 0; i < this.list.size(); i++) {
                        InputWidgetWrapper<S> wrapper = this.list.get(i);
                        int wy = wrapper.getY();
                        int wHeight = wrapper.getHeight();

                        if (e >= wy && e <= wy + wHeight) {
                            this.setSelected(i);

                            InputWidgetWrapper<S> selectedElement = this.getSelected();

                            if (selectedElement != null) {
                                selectedElement.onClick(d, e);
                            }

                            return;
                        }
                    }
                }
            }
        }

        public int getY() {
            if (this.list.isEmpty()) {
                return 0;
            }

            return this.list.getFirst().getY();
        }

        public int getWidth() {
            if (this.list.isEmpty()) {
                return 0;
            }

            return this.list.getFirst().getWidth();
        }

        public int getX() {
            if (this.list.isEmpty()) {
                return 0;
            }

            return this.list.getFirst().getX();
        }

        public int getHeight() {
            if (this.list.isEmpty()) {
                return 0;
            }

            return this.list.getFirst().getHeight();
        }

        public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
            for (InputWidgetWrapper<S> wrapper : this.list) {
                wrapper.resize(minecraft, width, height, listWidth, listHeight, elementHeight);
            }
        }
    }
}
