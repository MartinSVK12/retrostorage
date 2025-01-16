package sunsetsatellite.retrostorage.screen;


import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Vec2f;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.interfaces.mixin.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.screen.handler.RequestTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestTerminalScreen extends ReSScreen implements IExtendedScreenDraw {

    //public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2f> slots = new ArrayList<>();
    public RequestTerminalBlockEntity tile;
    public PlayerEntity player;

    public RequestTerminalScreen(PlayerInventory invPlayer, RequestTerminalBlockEntity tile) {
        super(new RequestTerminalScreenHandler(invPlayer, tile));
        backgroundHeight = 220;
        this.tile = tile;
        this.player = invPlayer.player;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2f(x,y));
            }
        }
    }

    protected void drawForeground() {
        textRenderer.draw("Request Terminal", 50, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        textRenderer.draw("Page: " + tile.page + "/" + tile.pages, 65, 93, 0x404040);
        if(tile.getController() != null){
            drawCenteredString(String.valueOf(tile.getController().getCraftables().size()), 88, 112, 0xFFFFFFFF);
        }
    }

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
        buttons.add(new ButtonWidget(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        buttons.add(new ButtonWidget(2, Math.round(width / 2 - 50), Math.round(height / 2 - 5), 20, 20, "Q"));
        buttons.add(new ButtonWidget(3, Math.round(width / 2 + 30), Math.round(height / 2 - 5), 20, 20, "X"));
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 0) {
            if (tile.network != null) {
                if (tile.page < tile.pages) {
                    tile.page++;
                }
            }
        }
        if (guibutton.id == 1) {
            if (tile.network != null) {
                if (tile.page > 0) {
                    tile.page--;
                }
            }
        }
        if (guibutton.id == 2) {
            minecraft.setScreen(new RequestQueueScreen(tile.getController(), this));
            //((IOpenGUI) player).displayGUI(new RequestQueueScreen(tile.getController(), this));
        }
        if (guibutton.id == 3) {
            if (tile.getController() != null) {
                tile.getController().clearRequestQueue();
                player.sendMessage(TranslationStorage.getInstance().getClientTranslation("action.retrostorage.clearTaskQueue"));
            }
        }
    }

    public boolean mouseHoveringOverSlot(final Vec2f slot, int x, int y)
    {
        final int k = (width - backgroundWidth) / 2;
        final int l = (height - backgroundHeight) / 2;
        x -= k;
        y -= l;
        return x >= slot.x - 1 && x < slot.x + 16 + 1 && y >= slot.y - 1 && y < slot.y + 16 + 1;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        NetworkController controller = tile.getController();
        if(controller != null) {
            for (int i = 0; i < slots.size(); i++) {
                Vec2f slot = slots.get(i);
                int id = i + (tile.page * 36);
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                if (mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                    if (mouseButton == 0) {
                        if (id >= stacks.size()) break;
                        ItemStack stack = stacks.get(id).getFirst();
                        NetworkCraftable craftable = stacks.get(id).getSecond();
                        if (stack == null) break;
                        minecraft.setScreen(new TaskRequestScreen(tile, stack, craftable));
                    }
                }
            }
        }
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - backgroundWidth) / 2;
        final int centerY = (height - backgroundHeight) / 2;

        GL11.glPushMatrix();
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if(controller != null) {
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 36);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2f slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getFirst();
                    if(stack == null) continue;
                    renderDigitalItem.render(stack, (int) slot.x, (int) slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2f slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getFirst();
                    if(stack == null) continue;
                    final PlayerInventory inventoryPlayer = minecraft.player.inventory;
                    if (inventoryPlayer.getCursorStack() == null && mouseHoveringOverSlot(slot,mouseX,mouseY)) {
                        String var13 = (TranslationStorage.getInstance().getClientTranslation(stack.getTranslationKey())).trim();
                        if (!var13.isEmpty()) {
                            int var14 = mouseX - centerX + 12;
                            int y = mouseY - centerY - 12;
                            int w = this.textRenderer.getWidth(var13);
                            this.fillGradient(var14 - 3, y - 3, var14 + w + 3, y + 8 + 3, -1073741824, -1073741824);
                            this.textRenderer.drawWithShadow(var13, var14, y, -1);
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
    }

    public @UnmodifiableView List<Pair<ItemStack,NetworkCraftable>> getFilteredStacks() {
        NetworkController controller = tile.getController();
        if(controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack,NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if(NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(),NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            return stacks;
        }
        return Collections.emptyList();
    }
}
