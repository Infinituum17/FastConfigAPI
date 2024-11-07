package infinituum.fastconfigapi.screens.utils.renderer.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public abstract class DynamicHeightAbstractSelectionList<E extends DynamicHeightAbstractSelectionList.Entry<E>> extends AbstractContainerWidget {
    protected static final int SCROLLBAR_WIDTH = 6;
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller");
    private static final ResourceLocation SCROLLER_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller_background");
    private static final ResourceLocation MENU_LIST_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
    private static final ResourceLocation INWORLD_MENU_LIST_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    protected final Minecraft minecraft;
    private final List<E> children = new DynamicHeightAbstractSelectionList<E>.TrackedList();
    private final double scrollingMultiplier = 20;
    protected boolean centerListVertically = true;
    protected int headerHeight;
    private double scrollAmount;
    private boolean renderHeader;
    private boolean scrolling;
    @Nullable
    private E selected;
    @Nullable
    private E hovered;

    public DynamicHeightAbstractSelectionList(Minecraft arg, int i, int j, int k) {
        super(0, k, i, j, CommonComponents.EMPTY);
        this.minecraft = arg;
    }

    protected void setRenderHeader(boolean bl, int i) {
        this.renderHeader = bl;
        this.headerHeight = i;
        if (!bl) {
            this.headerHeight = 0;
        }

    }

    public E getFirstElement() {
        return this.children.getFirst();
    }

    protected void replaceEntries(Collection<E> collection) {
        this.clearEntries();
        this.children.addAll(collection);
    }

    protected void clearEntries() {
        this.children.clear();
        this.selected = null;
    }

    protected E getEntry(int i) {
        return this.children().get(i);
    }

    protected int addEntry(E arg) {
        this.children.add(arg);
        return this.children.size() - 1;
    }

    protected void addEntryToTop(E arg) {
        double d = (double) this.getMaxScroll() - this.getScrollAmount();
        this.children.addFirst(arg);
        this.setScrollAmount((double) this.getMaxScroll() - d);
    }

    protected boolean removeEntryFromTop(E arg) {
        double d = (double) this.getMaxScroll() - this.getScrollAmount();
        boolean bl = this.removeEntry(arg);
        this.setScrollAmount((double) this.getMaxScroll() - d);
        return bl;
    }

    protected boolean isSelectedItem(int i) {
        return Objects.equals(this.getSelected(), this.children().get(i));
    }

    public void updateSize(int i, HeaderAndFooterLayout arg) {
        this.updateSizeAndPosition(i, arg.getContentHeight(), arg.getHeaderHeight());
    }

    public void updateSizeAndPosition(int i, int j, int k) {
        this.setSize(i, j);
        this.setPosition(0, k);
        this.clampScrollAmount();
    }

    public void clampScrollAmount() {
        this.setClampedScrollAmount(this.getScrollAmount());
    }

    public void setClampedScrollAmount(double d) {
        this.scrollAmount = Mth.clamp(d, 0.0, this.getMaxScroll());
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double d) {
        this.setClampedScrollAmount(d);
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.height - 4));
    }

    protected int getMaxPosition() {
        return getItemsHeightDefaultUntil(getItemCount()) + this.headerHeight;
    }

    private int getItemsHeightDefaultUntil(int index) {
        int heights = 0;

        for (int j = 0; j < index; j++) {
            E entry = this.children.get(j);
            int height = entry.getItemHeight();

            heights += height != 0 ? height : entry.approximateHeight();
        }

        return heights;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    public final @NotNull List<E> children() {
        return this.children;
    }

    public boolean mouseScrolled(double d, double e, double f, double g) {
        this.setScrollAmount(this.getScrollAmount() - g * (this.scrollingMultiplier) / 2);
        return true;
    }

    protected void renderHeader(GuiGraphics arg, int i, int j) {
    }

    protected void renderDecorations(GuiGraphics arg, int i, int j) {
    }

    public void renderWidget(GuiGraphics arg, int i, int j, float f) {
        this.hovered = this.isMouseOver(i, j) ? this.getEntryAtPosition(i, j) : null;
        this.renderListBackground(arg);
        this.enableScissor(arg);
        int k;
        int l;
        if (this.renderHeader) {
            k = this.getRowLeft();
            l = this.getY() + 4 - (int) this.getScrollAmount();
            this.renderHeader(arg, k, l);
        }

        this.renderListItems(arg, i, j, f);
        arg.disableScissor();
        this.renderListSeparators(arg);
        if (this.scrollbarVisible()) {
            k = this.getScrollbarPosition();
            l = (int) ((float) (this.height * this.height) / (float) this.getMaxPosition());
            l = Mth.clamp(l, 32, this.height - 8);
            int m = (int) this.getScrollAmount() * (this.height - l) / this.getMaxScroll() + this.getY();
            if (m < this.getY()) {
                m = this.getY();
            }

            RenderSystem.enableBlend();
            arg.blitSprite(SCROLLER_BACKGROUND_SPRITE, k, this.getY(), SCROLLBAR_WIDTH, this.getHeight());
            arg.blitSprite(SCROLLER_SPRITE, k, m, SCROLLBAR_WIDTH, l);
            RenderSystem.disableBlend();
        }

        this.renderDecorations(arg, i, j);
        RenderSystem.disableBlend();
    }

    public boolean isMouseOver(double d, double e) {
        return e >= (double) this.getY() && e <= (double) this.getBottom() && d >= (double) this.getX() && d <= (double) this.getRight();
    }

    public NarratableEntry.NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.hovered != null ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    protected boolean scrollbarVisible() {
        return this.getMaxScroll() > 0;
    }

    protected void renderListSeparators(GuiGraphics arg) {
        RenderSystem.enableBlend();
        ResourceLocation resourceLocation = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        ResourceLocation resourceLocation2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        arg.blit(resourceLocation, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        arg.blit(resourceLocation2, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        RenderSystem.disableBlend();
    }

    protected void renderListBackground(GuiGraphics arg) {
        RenderSystem.enableBlend();
        ResourceLocation resourceLocation = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
        arg.blit(resourceLocation, this.getX(), this.getY(), (float) this.getRight(), (float) (this.getBottom() + (int) this.getScrollAmount()), this.getWidth(), this.getHeight(), 32, 32);
        RenderSystem.disableBlend();
    }

    protected void enableScissor(GuiGraphics arg) {
        arg.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
    }

    protected void centerScrollOn(E arg) {
        this.setScrollAmount(
                getItemsHeightUntil(this.children.indexOf(arg))
                        + (double) arg.getItemHeight() / 2
                        - (double) this.height / 2
        );
    }    @Nullable
    protected final E getEntryAtPosition(double d, double e) {
        int i = this.getRowWidth() / 2;
        int j = this.getX() + this.width / 2;
        int k = j - i;
        int l = j + i;

        if (d < k || d > l) {
            return null;
        }

        int m = Mth.floor(e - (double) this.getY()) - this.headerHeight + (int) this.getScrollAmount() - 4;

        if (m < 0) {
            return null;
        }

        int baseY = 0;

        for (E current : this.children) {
            if (m >= baseY && m <= baseY + current.getItemHeight()) {
                return current;
            }

            baseY += current.getItemHeight();
        }

        return null;
    }

    protected int getItemsHeightUntil(int index) {
        int heights = 0;

        for (int j = 0; j < index; j++) {
            heights += this.children.get(j).getItemHeight();
        }

        return heights;
    }

    @Nullable
    protected E nextEntry(ScreenDirection arg) {
        return this.nextEntry(arg, (a) -> true);
    }

    @Nullable
    protected E nextEntry(ScreenDirection arg, Predicate<E> predicate) {
        return this.nextEntry(arg, predicate, this.getSelected());
    }

    @Nullable
    protected E nextEntry(ScreenDirection arg, Predicate<E> predicate, @Nullable E arg2) {
        int i = switch (arg) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
            default -> throw new MatchException(null, null);
        };

        if (!this.children().isEmpty() && i != 0) {
            int j;
            if (arg2 == null) {
                j = i > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(arg2) + i;
            }

            for (int k = j; k >= 0 && k < this.children.size(); k += i) {
                E entry = this.children().get(k);
                if (predicate.test(entry)) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelected(@Nullable E arg) {
        this.selected = arg;
    }

    protected void renderListItems(GuiGraphics arg, int i, int j, float f) {
        int k = this.getRowLeft();
        int l = this.getRowWidth();
        int n = this.getItemCount();

        for (int o = 0; o < n; ++o) {
            int p = this.getRowTop(o);
            int q = this.getRowBottom(o);
            if (q >= this.getY() && p <= this.getBottom()) {
                this.renderItem(arg, i, j, f, o, k, p, l);
            }
        }

    }

    protected void renderItem(GuiGraphics arg, int i, int j, float f, int k, int l, int m, int n) {
        E entry = this.getEntry(k);
        int o = entry.getItemHeight() - 4;

        entry.renderBack(arg, k, m, l, n, o, i, j, Objects.equals(this.hovered, entry), f);
        if (this.isSelectedItem(k)) {
            int p = this.isFocused() ? -1 : -8355712;
            this.renderSelection(arg, m, n, o, p, -16777216);
        }

        entry.render(arg, k, m, l, n, o, i, j, Objects.equals(this.hovered, entry), f);
    }

    protected void renderSelection(GuiGraphics arg, int i, int j, int k, int l, int m) {
        int n = this.getX() + (this.width - j) / 2;
        int o = this.getX() + (this.width + j) / 2;
        arg.fill(n, i - 2, o, i + k + 2, l);
        arg.fill(n + 1, i - 1, o - 1, i + k + 1, m);
    }

    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2 + 2;
    }

    public int getRowWidth() {
        return 220;
    }

    public double getScrollingMultiplier() {
        return this.scrollingMultiplier;
    }

    protected int getRowBottom(int i) {
        return this.getRowTop(i) + this.children.get(i).getItemHeight();
    }

    @Nullable
    protected E remove(int i) {
        E entry = this.children.get(i);
        return this.removeEntry(this.children.get(i)) ? entry : null;
    }

    protected boolean removeEntry(E arg) {
        boolean bl = this.children.remove(arg);
        if (bl && arg == this.getSelected()) {
            this.setSelected(null);
        }

        return bl;
    }

    @Nullable
    protected E getHovered() {
        return this.hovered;
    }

    void bindEntryToSelf(DynamicHeightAbstractSelectionList.Entry<E> arg) {
        arg.list = this;
    }

    protected void narrateListElementPosition(NarrationElementOutput arg, E arg2) {
        List<E> list = this.children();
        if (list.size() > 1) {
            int i = list.indexOf(arg2);
            if (i != -1) {
                arg.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", i + 1, list.size()));
            }
        }

    }

    @Environment(EnvType.CLIENT)
    protected abstract static class Entry<E extends DynamicHeightAbstractSelectionList.Entry<E>> implements GuiEventListener {
        /**
         * @deprecated
         */
        @Deprecated
        protected DynamicHeightAbstractSelectionList<E> list;

        protected Entry() {
        }

        public abstract void render(GuiGraphics arg, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f);

        public void renderBack(GuiGraphics arg, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        }

        public boolean isMouseOver(double d, double e) {
            return Objects.equals(this.list.getEntryAtPosition(d, e), this);
        }

        public abstract int getItemHeight();

        public abstract int approximateHeight();

        public boolean isFocused() {
            return this.list.getFocused() == this;
        }

        public void setFocused(boolean bl) {
        }
    }

    @Environment(EnvType.CLIENT)
    private class TrackedList extends AbstractList<E> {
        private final List<E> delegate = Lists.newArrayList();

        TrackedList() {
        }

        public E get(int i) {
            return this.delegate.get(i);
        }

        public E set(int i, E arg) {
            E entry = this.delegate.set(i, arg);
            DynamicHeightAbstractSelectionList.this.bindEntryToSelf(arg);
            return entry;
        }

        public void add(int i, E arg) {
            this.delegate.add(i, arg);
            DynamicHeightAbstractSelectionList.this.bindEntryToSelf(arg);
        }

        public E remove(int i) {
            return this.delegate.remove(i);
        }

        public int size() {
            return this.delegate.size();
        }
    }




    protected boolean clickedHeader(int i, int j) {
        return false;
    }


    protected void ensureVisible(E arg) {
        int i = this.getRowTop(this.children().indexOf(arg));
        int j = i - this.getY() - 4 - arg.getItemHeight();
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.getBottom() - i - arg.getItemHeight() - arg.getItemHeight();
        if (k < 0) {
            this.scroll(-k);
        }

    }

    private void scroll(int i) {
        this.setScrollAmount(this.getScrollAmount() + (double) i);
    }

    protected void updateScrollingState(double d, double e, int i) {
        this.scrolling = i == 0 && d >= (double) this.getScrollbarPosition() && d < (double) (this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.getDefaultScrollbarPosition();
    }

    protected int getDefaultScrollbarPosition() {
        return this.getRealRowRight() + this.getListOutlinePadding();
    }

    private int getListOutlinePadding() {
        return 10;
    }

    protected boolean isValidMouseClick(int i) {
        return i == 0;
    }

    private int getRealRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2;
    }

    private int getRealRowRight() {
        return this.getRealRowLeft() + this.getRowWidth();
    }

    protected int getRowTop(int i) {
        return this.getY() + 4 - (int) this.getScrollAmount() + getItemsHeightUntil(i) + this.headerHeight;
    }

    @Nullable
    public E getFocused() {
        return (E) super.getFocused();
    }

    public void setFocused(@Nullable GuiEventListener arg) {
        super.setFocused(arg);
        int i = this.children.indexOf(arg);
        if (i >= 0) {
            E entry = this.children.get(i);
            this.setSelected(entry);
            if (this.minecraft.getLastInputType().isKeyboard()) {
                this.ensureVisible(entry);
            }
        }

    }

    public boolean mouseClicked(double d, double e, int i) {
        if (!this.isValidMouseClick(i)) {
            return false;
        } else {
            this.updateScrollingState(d, e, i);
            if (!this.isMouseOver(d, e)) {
                return false;
            } else {
                E entry = this.getEntryAtPosition(d, e);
                if (entry != null) {
                    if (entry.mouseClicked(d, e, i)) {
                        E entry2 = this.getFocused();
                        if (entry2 != entry && entry2 instanceof ContainerEventHandler containerEventHandler) {
                            containerEventHandler.setFocused(null);
                        }

                        this.setFocused(entry);
                        this.setDragging(true);
                        return true;
                    }
                } else if (this.clickedHeader((int) (d - (double) (this.getX() + this.width / 2 - this.getRowWidth() / 2)), (int) (e - (double) this.getY()) + (int) this.getScrollAmount() - 4)) {
                    return true;
                }

                return this.scrolling;
            }
        }
    }

    public boolean mouseReleased(double d, double e, int i) {
        return this.getFocused() != null && this.getFocused().mouseReleased(d, e, i);
    }

    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (super.mouseDragged(d, e, i, f, g)) {
            return true;
        } else if (i == 0 && this.scrolling) {
            if (e < (double) this.getY()) {
                this.setScrollAmount(0.0);
            } else if (e > (double) this.getBottom()) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double h = Math.max(1, this.getMaxScroll());
                int j = this.height;
                int k = Mth.clamp((int) ((float) (j * j) / (float) this.getMaxPosition()), 32, j - 8);
                double l = Math.max(1.0, h / (double) (j - k));
                this.setScrollAmount(this.getScrollAmount() + g * l);
            }

            return true;
        } else {
            return false;
        }
    }


}
