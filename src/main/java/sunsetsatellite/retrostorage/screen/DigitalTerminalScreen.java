package sunsetsatellite.retrostorage.screen;

import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ItemStackElement;
import net.glasslauncher.mods.alwaysmoreitems.util.StringUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ExtendedScreenDraw;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalInteractionPacket;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalRequestContentsPacket;
import sunsetsatellite.retrostorage.screen.handler.DigitalTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.DigitalItemRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class DigitalTerminalScreen extends HandledScreen implements ExtendedScreenDraw {
    private final PlayerInventory playerInv;
    private final DigitalTerminalBlockEntity tile;
    private final DigitalItemRenderer digitalItemRenderer = new DigitalItemRenderer(16, 16, HandledScreen.itemRenderer);

    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public boolean searching = false;
    public String searchQuery = "";
    public int lastVirtualSlotClicked = -1;

    public TickTimer requestTimer = new TickTimer(this, () -> PacketHelper.send(new TerminalRequestContentsPacket(searchQuery)), 10, true);

    public DigitalTerminalScreen(PlayerInventory playerInv, DigitalTerminalBlockEntity tile) {
        super(new DigitalTerminalScreenHandler(playerInv, tile));
        this.playerInv = playerInv;
        this.tile = tile;
        this.backgroundHeight = 220;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2i(x, y));
            }
        }
    }

    @Override
    public void init() {
        super.init();
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        buttons.add(new ButtonWidget(0, j - 26 + 6, k + 27 + 8 + 29, 20, 20, "+"));
        buttons.add(new ButtonWidget(1, j - 26 + 6, k + 27 + 8, 20, 20, "-"));
    }

    @Override
    public void tick() {
        super.tick();
        int scrollDelta = Mouse.getDWheel();
        if (scrollDelta != 0) {
            handlePageScroll(scrollDelta > 0);
        }
        requestTimer.tick();
    }

    public void handlePageScroll(boolean scrollUp) {
        if (tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                if (scrollUp) {
                    if (tile.page > 0) {
                        tile.page--;
                    } else {
                        tile.page = tile.pages;
                    }
                } else {
                    // Scrolling down
                    if (tile.page < tile.pages) {
                        tile.page++;
                    } else {
                        tile.page = 0;
                    }
                }
            }
        }
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 8, 6, 0x404040);
        if (searching)
            StringUtil.drawCenteredString(textRenderer, "<< Searching using AMI >>", (backgroundWidth / 2) + 42, -8, 0xFFFFFF, false);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        if (tile.page > tile.pages) tile.page = 0;

        String pageText = "Page: " + tile.page + "/" + tile.pages;
        int pageTextWidth = textRenderer.getWidth(pageText);
        textRenderer.draw(pageText, backgroundWidth - 8 - pageTextWidth, 6, 0x404040);
        if (tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getAmount() >= controller.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                String stackText = controller.getStackAmount() + "/" + controller.getStackCapacity();
                int stackTextWidth = textRenderer.getWidth(stackText);
                textRenderer.drawWithShadow(stackText, backgroundWidth - 8 - stackTextWidth, (backgroundHeight - 95) + 2, color);
            }
        }
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("digital_terminal"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawTexture(x - 27, y + 27, 177, 0, 27, 65);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        if (!button.active) return;
        if (button.id == 0) {
            if (tile.page == tile.pages) tile.page = 0;
            else tile.page = Math.min(tile.pages, tile.page + 1);
        }
        if (button.id == 1) {
            if (tile.page == 0) tile.page = tile.pages;
            else tile.page = Math.max(0, tile.page - 1);
        }
    }

    public boolean mouseHoveringOverSlot(final Vec2i slot, int x, int y) {
        final int k = (width - backgroundWidth) / 2;
        final int l = (height - backgroundHeight) / 2;
        x -= k;
        y -= l;
        return x >= slot.x - 1 && x < slot.x + 16 + 1 && y >= slot.y - 1 && y < slot.y + 16 + 1;
    }

    public int getVirtualSlotAtPosition(int x, int y) {
        for (int i = 0; i < slots.size(); i++) {
            Vec2i slot = slots.get(i);
            if (mouseHoveringOverSlot(slot, x, y)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        Slot invSlot = getSlotAt(mouseX, mouseY);
        int slotId = -1;
        int vSlotId = getVirtualSlotAtPosition(mouseX, mouseY);
        if (invSlot != null) slotId = invSlot.index;

        PacketHelper.send(new TerminalInteractionPacket(searchQuery, slotId, vSlotId, tile.page, button, shift));

        lastVirtualSlotClicked = vSlotId;

        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - backgroundWidth) / 2;
        final int centerY = (height - backgroundHeight) / 2;

        if (tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                List<ItemStack> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 54);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if (id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if (stack == null) continue;
                    digitalItemRenderer.render(stack, slot.x, slot.y, mouseHoveringOverSlot(slot, mouseX, mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if (id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if (stack == null) continue;
                    final PlayerInventory inventoryPlayer = minecraft.player.inventory;
                    if (inventoryPlayer.getCursorStack() == null && mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                        GL11.glTranslatef(-centerX, -centerY, 0.0F);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        List<Object> tooltip = new ArrayList<>(TooltipHelper.getTooltipForItemStack(TranslationStorage.getInstance().getClientTranslation(stack.getTranslationKey()), stack, inventoryPlayer, this));
                        tooltip.add(Formatting.GRAY + String.valueOf(stack.count));
                        Tooltip.INSTANCE.setTooltip(tooltip, mouseX + 8, mouseY + 8);
                        break;
                    }
                }
            }
        }
    }

    public @UnmodifiableView List<ItemStack> getFilteredStacks() {
        if (tile.world.isRemote) {
            return ((DigitalTerminalScreenHandler) handler).networkStacks;
        }

        searching = false;
        searchQuery = "";

        NetworkController controller = tile.getController();
        if (controller != null) {
            if (!AlwaysMoreItems.getItemFilter().getFilterText().isBlank()) {
                List<ItemStack> stacks = controller.getAllItems();
                searching = true;
                searchQuery = AlwaysMoreItems.getItemFilter().getFilterText();
                List<ItemStack> searchList = AlwaysMoreItems.getItemFilter().getItemList().stream().map(ItemStackElement::getItemStack).toList();
                stacks = stacks.stream().filter((S) -> Catalyst.listContains(searchList, S, (checkedStack, listStack) -> listStack.isItemEqual(checkedStack))).toList();
                return stacks;
            } else {
                return controller.getAllItems();
            }
        }

        return Collections.emptyList();
    }
}
