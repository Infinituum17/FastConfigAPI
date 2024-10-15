package infinituum.fastconfigapi.mixin;

import infinituum.fastconfigapi.screens.ConfigSelectionScreen;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Unique
    private final int CONFIG_BUTTON_PADDING = 5;
    @Shadow
    @Final
    private HeaderAndFooterLayout layout;

    protected OptionsScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;visitWidgets(Ljava/util/function/Consumer;)V"))
    private void init(CallbackInfo ci) {
        SpriteIconButton configButtonWidget = SpriteIconButton.builder(
                        Component.translatable("options.config"),
                        button -> Objects.requireNonNull(this.minecraft).setScreen(new ConfigSelectionScreen(this)), true)
                .width(20)
                .sprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/config"), 15, 15)
                .build();

        this.layout.addToFooter(configButtonWidget, settings -> {
            settings.paddingRight(CONFIG_BUTTON_PADDING);
            settings.alignHorizontallyRight();
        });
    }
}
