package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import infinituum.fastconfigapi.screens.widgets.type.CompoundEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ObjectEditor extends InputWidgetWrapper<Object> implements CompoundEditor {
    private final ObjectManager manager;
    private final int listWidth;
    private int lineSpacing;
    private int horizontalSpacing;

    public ObjectEditor(Font font, int i, int j, int k, int l, Component name, Object initValue, int width) {
        this.manager = new ObjectManager(initValue, font);
        this.lineSpacing = 4;
        this.horizontalSpacing = 4;
        this.listWidth = width;
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            return selected.keyPressed(i, j, k);
        }

        return false;
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            return selected.keyReleased(i, j, k);
        }

        return false;
    }

    @Override
    public boolean charTyped(char c, int i) {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            return selected.charTyped(c, i);
        }

        return false;
    }

    @Override
    public void setFocused(boolean bl) {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            selected.setFocused(bl);
        }
    }

    @Override
    public boolean isVisible() {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            return selected.isVisible();
        }

        return false;
    }

    @Override
    public void setVisible(boolean b) {
        InputWidgetWrapper<?> selected = this.manager.getSelected();

        if (selected != null) {
            selected.setVisible(b);
        }
    }

    @Override
    public void setPosition(int i, int j) {
        this.manager.setPosition(i, j);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.manager.render(guiGraphics, i, j, f);
    }

    @Override
    public Object get() {
        return this.manager.get();
    }

    @Override
    public void onClick(double d, double e) {
        this.manager.onClick(d, e, this.getTotalHeight());
    }

    @Override
    public int getTotalHeight() {
        return this.manager.getTotalHeight();
    }

    @Override
    public int getHeight() {
        return this.manager.getHeight();
    }

    @Override
    public int getWidth() {
        return this.manager.getWidth();
    }

    @Override
    public int getX() {
        return this.manager.getX();
    }

    @Override
    public int getY() {
        return this.manager.getY();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
        this.manager.resize(minecraft, width, height, listWidth, listHeight, elementHeight);
    }

    private int getLineSpacing() {
        return this.lineSpacing;
    }

    private int getHorizontalSpacing() {
        return this.horizontalSpacing;
    }

    @Override
    public boolean hasNextElement(FocusNavigationEvent.TabNavigation tabNavigation) {
        return this.manager.setNextElement(tabNavigation);
    }

    private record ObjectField(Field field, String name, InputWidgetWrapper<?> wrapper) {
    }

    public class ObjectManager {
        private final Font font;
        private final Object object;
        private final List<ObjectField> fields;
        private final int textMaxWidth;
        private int height;
        private int selected;

        public ObjectManager(Object obj, Font font) {
            this.font = font;
            this.object = obj;
            this.height = 0;
            Field[] fields = obj.getClass().getDeclaredFields();

            if (fields.length == 0) {
                this.fields = List.of();
                this.selected = -1;
                this.textMaxWidth = 0;

                return;
            }

            List<ObjectField> objectFields = new ArrayList<>();
            int width = 0;

            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value;

                try {
                    value = field.get(obj);
                } catch (Exception e) {
                    throw new RuntimeException("Could not retrieve wrapper of field '" + name + "' from object '" + obj.getClass().getName() + "'");
                }

                InputWidgetWrapper<?> wrapper = createWidgetWrapper(value, font, name, listWidth);

                if (!(wrapper instanceof ObjectEditor)) {
                    if (font.width(name + ":") > width) {
                        width = font.width(name + ":") + 2;
                    }

                    objectFields.add(new ObjectField(field, name, wrapper));
                }
            }

            this.textMaxWidth = width;
            this.fields = objectFields;
            this.selected = 0;
        }

        public int getWidth() {
            if (this.fields.isEmpty()) {
                return 0;
            }

            return textMaxWidth + horizontalSpacing + this.fields.getFirst().wrapper().getWidth();
        }

        public int getX() {
            if (this.fields.isEmpty()) {
                return 0;
            }

            return this.fields.getFirst().wrapper().getX();
        }

        public void onClick(double d, double e, int totalHeight) {
            if (!this.fields.isEmpty()) {
                int height = totalHeight - 2;
                int y = (int) (e - this.getY());

                if (y >= 0 && y <= height) {
                    for (int i = 0; i < this.fields.size(); i++) {
                        InputWidgetWrapper<?> wrapper = this.fields.get(i).wrapper();
                        int wy = wrapper.getY();
                        int wHeight = wrapper.getTotalHeight();

                        if (e >= wy && e <= wy + wHeight) {
                            this.setSelected(i);

                            InputWidgetWrapper<?> selectedElement = this.getSelected();

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
            if (this.fields.isEmpty()) {
                return 0;
            }

            return this.fields.getFirst().wrapper().getY();
        }

        private InputWidgetWrapper<?> getSelected() {
            try {
                return this.fields.get(this.selected).wrapper();
            } catch (Exception e) {
                return null;
            }
        }

        public void setSelected(int selected) {
            ObjectEditor.this.setFocused(false);

            if (selected >= 0 && selected < this.size()) {
                this.selected = selected;
            } else {
                throw new IndexOutOfBoundsException("ObjectManager widget index is out of bounds");
            }
        }

        public int size() {
            return this.fields.size();
        }

        public void render(GuiGraphics guiGraphics, int i, int j, float f) {
            if (!fields.isEmpty()) {
                j -= 2;
                for (int k = 0; k < fields.size(); k++) {
                    String name = StringUtils.capitalize(fields.get(k).name() + ":");
                    InputWidgetWrapper<?> wrapper = fields.get(k).wrapper();
                    int wx2 = wrapper.getX() + textMaxWidth + getHorizontalSpacing();

                    FastRenderer renderer = FastRenderer.startRender(guiGraphics, font);

                    int x = wx2 - getHorizontalSpacing() - font.width(name);

                    renderer.message(name, x, j + (getLineSpacing() * 2))
                            .render();

                    wrapper.setPosition(wrapper.getX() + textMaxWidth + getHorizontalSpacing(), j + getLineSpacing());
                    wrapper.render(guiGraphics, i, j, f);

                    if (k + 1 < fields.size()) {
                        j += ObjectEditor.this.getLineSpacing() + wrapper.getTotalHeight();
                    }
                }
            }
        }

        public void setPosition(int i, int j) {
            int padding = 0;

            for (var field : this.fields) {
                field.wrapper().setPosition(i, j + padding);

                padding += ObjectEditor.this.getLineSpacing() + 16;
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

        public Object get() {
            for (ObjectField objField : fields) {
                objField.field().setAccessible(true);

                try {
                    objField.field().set(this.object, objField.wrapper().get());
                } catch (Exception e) {
                    throw new RuntimeException("Could not set field '" + objField.name() + "' of class '" + object.getClass().getSimpleName() + "'");
                }
            }

            return this.object;
        }

        public int getTotalHeight() {
            if (!this.fields.isEmpty() && height == 0) {
                for (int i = 0; i < this.fields.size(); i++) {
                    ObjectField field = this.fields.get(i);
                    height += field.wrapper().getTotalHeight();

                    if (i + 1 < this.fields.size()) {
                        height += getLineSpacing();
                    }
                }
            }

            return height;
        }

        public int getHeight() {
            if (this.fields.isEmpty()) {
                return 0;
            }

            return this.fields.getFirst().wrapper().getHeight();
        }

        public void resize(Minecraft minecraft, int width, int height, int listWidth, int listHeight, int elementHeight) {
            for (ObjectField field : this.fields) {
                field.wrapper().resize(minecraft, width, height, listWidth, listHeight, elementHeight);
            }
        }
    }
}
