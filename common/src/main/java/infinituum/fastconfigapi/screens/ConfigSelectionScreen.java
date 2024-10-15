package infinituum.fastconfigapi.screens;

import infinituum.fastconfigapi.screens.models.ConfigSelectionModel;
import infinituum.fastconfigapi.screens.widgets.ConfigOptionsList;
import infinituum.fastconfigapi.screens.widgets.ConfigSelectionList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class ConfigSelectionScreen extends Screen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 30, 33);
    private final Screen parent;
    private final ConfigSelectionModel model;
    private ConfigSelectionList configList;
    private ConfigOptionsList configOptions;
    private Button doneButton;

    public ConfigSelectionScreen(Screen parent) {
        super(Component.translatable("menu.fastconfigapi.config"));

        this.parent = parent;
        this.model = new ConfigSelectionModel();
    }

    @Override
    protected void init() {
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(5));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.getTitle(), this.font));

        ConfigSelectionList list = new ConfigSelectionList(this.minecraft, this, 1, this.height - 70, 33, 30, this.model);
        ConfigOptionsList options = new ConfigOptionsList(this.minecraft, this, 1, this.height - 70, 33, 30, this.model);

        list.setConfigOptions(options);
        options.setConfigList(list);

        this.configList = this.layout.addToContents(list);
        this.configOptions = this.layout.addToContents(options);

        this.doneButton = this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);

        this.repositionElements();

        if (!this.configList.children().isEmpty()) {
            this.setFocused(this.configList);
        } else {
            this.setFocused(this.doneButton);
        }
    }

    @Override
    public void onClose() {
        if (this.configList.getSelected() != null) {
            this.configList.getSelected().getConfig().save();
        }

        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
            this.parent.clearFocus();
        }
    }

    @Override
    protected void repositionElements() {
        this.configList.refresh();
        this.configOptions.refresh();

        this.layout.arrangeElements();
        this.configList.updateSize(175, this.layout);
        this.configList.setX(0);

        this.configOptions.updateSize(this.width - 180, this.layout);
        this.configOptions.setX(180);
    }

    public Button getDoneButton() {
        return this.doneButton;
    }
}
