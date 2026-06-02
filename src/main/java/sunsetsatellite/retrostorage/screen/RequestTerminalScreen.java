package sunsetsatellite.retrostorage.screen;

import com.mojang.datafixers.util.Pair;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ItemStackElement;
import net.glasslauncher.mods.alwaysmoreitems.util.StringUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.packet.ClearRequestQueuePacket;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestTerminalRequestContentsPacket;
import sunsetsatellite.retrostorage.screen.handler.RequestTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.DigitalItemRenderer;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class RequestTerminalScreen extends HandledScreen implements ExtendedScreenDraw {
    private final PlayerInventory playerInv;
    private final RequestTerminalBlockEntity tile;
    private final DigitalItemRenderer digitalItemRenderer = new DigitalItemRenderer(16, 16, HandledScreen.itemRenderer);

    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public boolean searching = false;
    public String searchQuery = "";

    public TickTimer requestTimer = new TickTimer(this, () -> PacketHelper.send(new RequestTerminalRequestContentsPacket(searchQuery)), 10, true);

    public RequestTerminalScreen(PlayerInventory playerInv, RequestTerminalBlockEntity tile) {
        super(new RequestTerminalScreenHandler(playerInv, tile));
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
        buttons.add(new ButtonWidget(0, j - 27 + 6, k + 8 + 8 + 29, 21, 21, "+"));
        buttons.add(new ButtonWidget(1, j - 27 + 6, k + 8 + 8, 21, 21, "-"));
        buttons.add(new ButtonWidget(2, j - 27 + 6, k + 8 + 8 + 29 * 2, 21, 21, "Q"));
        buttons.add(new ButtonWidget(3, j - 27 + 6, k + 8 + 8 + 29 * 3, 21, 21, "X"));
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

    @Override
    protected void drawForeground() {
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 8, 6, 0x404040);
        if (searching)
            StringUtil.drawCenteredString(textRenderer, "<< Searching using AMI >>", (backgroundWidth / 2) - 42, -8, 0xFFFFFF, false);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        if (tile.page > tile.pages) tile.page = 0;

        String pageText = "Page: " + tile.page + "/" + tile.pages;
        int pageTextWidth = textRenderer.getWidth(pageText);
        textRenderer.draw(pageText, backgroundWidth - 8 - pageTextWidth, 6, 0x404040);
        NetworkController controller = tile.getController();
        if (controller != null) {
            int color = 0xFFFFFF;
            String craftableSize = String.valueOf(tile.getController().getCraftables().size());
            int stackTextWidth = textRenderer.getWidth(craftableSize);
            textRenderer.drawWithShadow(craftableSize, backgroundWidth - 8 - stackTextWidth, (backgroundHeight - 95) + 2, color);
        }
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("request_terminal"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawTexture(x - 27, y + 8, 177, 0, 27, 123);
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
        if (button.id == 2) {
            minecraft.setScreen(new RequestQueueScreen(tile.getController(), this));
        }
        if (button.id == 3) {
            NetworkController c = tile.getController();
            if (c != null) {
                c.clearRequestQueue();
                Vec3i pos = c.getPosition();
                if (tile.world.isRemote) {
                    PacketHelper.send(new ClearRequestQueuePacket(pos.x, pos.y, pos.z));
                }
                playerInv.player.sendMessage("action.retrostorage.clearTaskQueue.name");
            }
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

        NetworkController controller = tile.getController();
        if (controller != null) {
            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                int id = i + (tile.page * 54);
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                if (mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                    if (button == 0) {
                        if (id >= stacks.size()) break;
                        ItemStack stack = stacks.get(id).getFirst();
                        NetworkCraftable craftable = stacks.get(id).getSecond();
                        if (stack == null) break;
                        List<NetworkCraftable> craftables = getAllStacks().stream().map(Pair::getSecond).collect(Collectors.toList());
                        minecraft.setScreen(new TaskRequestScreen(tile, stack, craftable, craftables));
                    }
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - backgroundWidth) / 2;
        final int centerY = (height - backgroundHeight) / 2;

        NetworkController controller = tile.getController();
        if (controller != null) {
            @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
            this.tile.pages = (int) (double) (stacks.size() / 54);
            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                ItemStack stack;
                int id = i + (tile.page * 54);
                if (id >= stacks.size()) break;
                stack = stacks.get(id).getFirst();
                if (stack == null) continue;
                digitalItemRenderer.render(stack, slot.x, slot.y, mouseHoveringOverSlot(slot, mouseX, mouseY));
            }
            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                ItemStack stack;
                int id = i + (tile.page * 54);
                if (id >= stacks.size()) break;
                stack = stacks.get(id).getFirst();
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

    public @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> getAllStacks() {
        if (tile.world.isRemote) {
            return ((RequestTerminalScreenHandler) handler).networkCraftables;
        }

        NetworkController controller = tile.getController();
        if (controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack, NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if (NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(), NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            return stacks;
        }

        return Collections.emptyList();
    }

    public @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> getFilteredStacks() {
        if (tile.world.isRemote) {
            return ((RequestTerminalScreenHandler) handler).networkCraftables;
        }

        searching = false;
        searchQuery = "";

        NetworkController controller = tile.getController();
        if (controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack, NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if (NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(), NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            if (!AlwaysMoreItems.getItemFilter().getFilterText().isBlank()) {
                searching = true;
                searchQuery = AlwaysMoreItems.getItemFilter().getFilterText();
                List<ItemStack> searchList = AlwaysMoreItems.getItemFilter().getItemList().stream().map(ItemStackElement::getItemStack).toList();
                stacks = stacks.stream().filter((P) -> Catalyst.listContains(searchList, P.getFirst(), (checkedStack, listStack) -> listStack.isItemEqual(checkedStack))).toList();
                return stacks;
            } else {
                return stacks;
            }
        }

        return Collections.emptyList();
    }
}
