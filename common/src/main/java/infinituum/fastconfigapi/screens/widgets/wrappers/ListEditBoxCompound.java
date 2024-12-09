package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import infinituum.fastconfigapi.screens.widgets.type.CompoundEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

public final class ListEditBoxCompound<T> extends InputWidgetWrapper<List<T>> implements CompoundEditor {
    private final ElementsList<T> wrapperList;
    private final int singleBoxHeight;
    private final int lineSpacing;

    public ListEditBoxCompound(Font font, int i, int j, int k, int l, Component component, List<T> initValue) {
        this.wrapperList = new ElementsList<>(font, initValue, component, k);
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
        this.wrapperList.setPosition(i, j);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.wrapperList.render(guiGraphics, i, j, f);
    }

    @Override
    public List<T> get() {
        return this.wrapperList.get();
    }

    @Override
    public void onClick(double d, double e) {
        this.wrapperList.onClick(d, e);
    }

    @Override
    public int getTotalHeight() {
        int newBoxHeight = this.singleBoxHeight + this.lineSpacing;
        int boxesHeight = (this.wrapperList.size() - 1) * this.singleBoxHeight + (this.wrapperList.size() - 2) * this.lineSpacing - 2;
        int borderSize = 2;

        return newBoxHeight + borderSize + boxesHeight + 1;
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

    public class ElementsList<S> {
        private final SortedMap<Integer, WidgetPair<S>> map;
        private int listWidth;
        private int selected;
        private int index;

        public ElementsList(Font font, List<S> initValue, Component parentName, int listWidth) {
            this.listWidth = listWidth;
            this.selected = 0;
            this.map = new TreeMap<>();
            this.index = this.composeList(font, initValue, parentName);
        }

        private int composeList(Font font, List<S> initValue, Component parentName) {
            for (S element : initValue) {
                this.createNewElement(element, font, parentName.getString());
            }

            int currentIndex = this.map.size();

            InputWidgetWrapper<S> wrapper = InputWidgetWrapper.createWidgetWrapper(this.map.get(0).widget().get(), font, "new_element", listWidth, false);
            SpriteIconButton newElementButton = SpriteIconButton.builder(Component.literal("add_element"),
                            b -> this.createNewElement(wrapper.get(), font, parentName.getString()), true)
                    .size(18, 18)
                    .sprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/add"), 15, 15)
                    .build();

            wrapper.setValue(null);

            this.map.put(Integer.MAX_VALUE, new WidgetPair<>(wrapper, newElementButton));

            return currentIndex;
        }

        private void createNewElement(S value, Font font, String name) {
            InputWidgetWrapper<S> widget = createWidgetWrapper(value, font, name, listWidth, false);

            int i = newIndex();

            SpriteIconButton button = SpriteIconButton.builder(Component.literal("button_" + name), b -> this.map.remove(i), true)
                    .size(18, 18)
                    .sprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/cross"), 15, 15)
                    .build();

            this.map.put(i, new WidgetPair<>(widget, button));
        }

        private int newIndex() {
            if (index == Integer.MAX_VALUE) {
                throw new RuntimeException("Reached max number of elements in List Editor");
            }

            return index++;
        }

        public void render(GuiGraphics guiGraphics, int i, int j, float f) {
            for (int k : map.keySet()) {
                map.get(k).widget().render(guiGraphics, i, j, f);
                map.get(k).button().render(guiGraphics, i, j, f);
            }
        }

        public List<S> get() {
            return this.map.entrySet()
                    .stream()
                    .filter(pair -> pair.getKey() != Integer.MAX_VALUE)
                    .sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .map(e -> e.getValue().widget().get())
                    .toList();
        }

        public void setPosition(int i, int j) {
            int padding = 0;

            for (var element : this.map.values()) {
                InputWidgetWrapper<S> widget = element.widget();
                SpriteIconButton button = element.button();
                widget.setPosition(i + button.getWidth() + 2, j + padding);
                button.setPosition(widget.getX() - button.getWidth() - 2 - 1, widget.getY() - 1);

                padding += ListEditBoxCompound.this.getLineSpacing() + 16;
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
                return this.map.get(this.selected).widget();
            } catch (Exception e) {
                return null;
            }
        }

        public int size() {
            return this.map.size();
        }

        public void setSelected(int selected) {
            ListEditBoxCompound.this.setFocused(false);

            if (selected == -1) {
                return;
            }

            if (this.map.get(selected) != null) {
                this.selected = selected;
            } else {
                throw new IndexOutOfBoundsException("ElementsList widget index is out of bounds");
            }
        }

        public void onClick(double d, double e) {
            if (!this.map.isEmpty()) {
                int height = getTotalHeight() - 2;
                int y = (int) (e - this.getY());

                if (y < 0 || y > height) {
                    return;
                }

                for (int i : map.keySet()) {
                    InputWidgetWrapper<S> wrapper = this.map.get(i).widget();
                    SpriteIconButton button = this.map.get(i).button();
                    int wy = wrapper.getY();

                    if (e >= wy && e <= wy + wrapper.getHeight()) {
                        int bx = button.getX();

                        if (d >= bx && d <= bx + button.getWidth()) {
                            button.onClick(d, e);

                            return;
                        }

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

        public int getY() {
            return this.map.get(firstElementKey()).widget().getY();
        }

        private int firstElementKey() {
            return this.map.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getKey)).orElseThrow().getKey();
        }

        public int getWidth() {
            return this.map.get(Integer.MAX_VALUE).widget().getWidth() + 2 + this.map.get(Integer.MAX_VALUE).button().getWidth();
        }

        public int getX() {
            return this.map.get(firstElementKey()).button().getX();
        }

        public int getHeight() {
            return this.map.get(firstElementKey()).widget().getHeight();
        }

        public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
            for (WidgetPair<S> widgetPair : this.map.values()) {
                widgetPair.widget().resize(minecraft, width, height, listWidth, listHeight, elementHeight);
            }

            this.listWidth = listWidth;
        }

        public record WidgetPair<T>(InputWidgetWrapper<T> widget, SpriteIconButton button) {
        }
    }
}
