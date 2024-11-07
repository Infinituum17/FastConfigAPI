package infinituum.fastconfigapi.screens.widgets.wrappers;

import infinituum.fastconfigapi.screens.utils.renderer.FastRenderer;
import infinituum.fastconfigapi.screens.widgets.InputWidgetWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ObjectEditor extends InputWidgetWrapper<Object> {
    private final ObjectManager manager;
    private int lineSpacing;
    private int horizontalSpacing;

    public ObjectEditor(Font font, int i, int j, int k, int l, Component name, Object initValue) {
        this.manager = new ObjectManager(initValue, font);
        this.lineSpacing = 4;
        this.horizontalSpacing = 4;
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

    private int getLineSpacing() {
        return this.lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    private int getHorizontalSpacing() {
        return this.horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public boolean hasNextElement(FocusNavigationEvent.TabNavigation tabNavigation) {
        return this.manager.setNextElement(tabNavigation);
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

                InputWidgetWrapper<?> wrapper = createWidgetWrapper(value, font, name);

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
                        int wHeight = wrapper.getHeight();

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
            this.selected = selected;
        }

        public void render(GuiGraphics guiGraphics, int i, int j, float f) {
            if (!fields.isEmpty()) {
                j -= 2;
                for (int k = 0; k < fields.size(); k++) {
                    String name = StringUtils.capitalize(fields.get(k).name() + ":");
                    InputWidgetWrapper<?> wrapper = fields.get(k).wrapper();
                    int wx2 = wrapper.getX() + textMaxWidth + horizontalSpacing;

                    FastRenderer renderer = FastRenderer.startRender(guiGraphics, font);

                    int x = wx2 - horizontalSpacing - font.width(name);

                    renderer.message(name, x, j + (lineSpacing * 2))
                            .render();

                    wrapper.setPosition(wrapper.getX() + textMaxWidth + horizontalSpacing, j + lineSpacing);
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
            int i = tabNavigation.forward() ? this.selected + 1 : this.selected - 1;

            if (i >= 0 && i < this.size()) {
                this.setSelected(i);

                return true;
            }

            return false;
        }

        public int size() {
            return this.fields.size();
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
                        height += lineSpacing;
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

        private record ObjectField(Field field, String name, InputWidgetWrapper<?> wrapper) {
        }
    }
}
