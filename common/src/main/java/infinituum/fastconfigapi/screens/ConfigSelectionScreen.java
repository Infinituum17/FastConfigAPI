package infinituum.fastconfigapi.screens;

import infinituum.fastconfigapi.screens.utils.ExpansionListManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class ConfigSelectionScreen extends Screen {
    private final HeaderAndFooterLayout layout;
    private final Screen parent;
    private ExpansionListManager manager;
    private Button doneButton;

    public ConfigSelectionScreen(Screen parent) {
        super(Component.translatable("menu.fastconfigapi.config"));

        this.layout = new HeaderAndFooterLayout(this, 30, 33);
        this.parent = parent;
    }

    public Button getDoneButton() {
        return doneButton;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        if (this.manager.getList().isHovered()) {
            this.manager.getList().renderTooltip(guiGraphics, i, j, f);
        }

        if (this.manager.getOptions().isHovered()) {
            this.manager.getOptions().renderTooltip(guiGraphics, i, j, f);
        }
    }

    @Override
    public void onClose() {
        this.manager.saveCurrent();

        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
            this.parent.clearFocus();
        }
    }

    @Override
    protected void init() {
        this.manager = new ExpansionListManager(this.minecraft, this);

        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(5));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.getTitle(), this.font));

        this.addRenderableWidget(this.manager.getList());
        this.addRenderableWidget(this.manager.getOptions());

        this.doneButton = this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose())
                .width(200).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();

        this.setFocused(this.manager.getList());
    }

    @Override
    protected void repositionElements() {
        this.manager.refresh();
        this.layout.arrangeElements();
        this.manager.reposition(this.layout);
    }
}
