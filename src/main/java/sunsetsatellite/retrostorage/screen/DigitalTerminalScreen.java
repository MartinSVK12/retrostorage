package sunsetsatellite.retrostorage.screen;



import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.util.math.Vec2f;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.interfaces.mixin.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.screen.handler.DigitalTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DigitalTerminalScreen extends ReSScreen implements IExtendedScreenDraw {

    public final DigitalTerminalBlockEntity tile;
    //public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2f> slots = new ArrayList<>();
    public final PlayerInventory inventoryPlayer;

    public DigitalTerminalScreen(PlayerInventory inventoryplayer, DigitalTerminalBlockEntity tile) {
        super(new DigitalTerminalScreenHandler(inventoryplayer, tile));
        backgroundHeight = 220;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2f(x,y));
            }
        }

    }

    protected void drawForeground() {
        textRenderer.draw("Digital Terminal", 50, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        if(tile.page > tile.pages) tile.page = 0;
        textRenderer.draw("Page: " + tile.page + "/" + tile.pages, 63, 93, 0x404040);
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getAmount() >= controller.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                drawCenteredString(controller.getStackAmount() + "/" + controller.getStackCapacity(), 90, 112, color);
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

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, ">"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        //buttons.add(new ButtonWidget(2, Math.round((float) width / 2 - 40), Math.round((float) height / 2 - 5), 20, 20, "A:"));
        //buttons.get(2).enabled = false;
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
            if(tile.page == tile.pages) tile.page = 0;
            else tile.page = Math.min(tile.pages, tile.page + 1);
        }
        if (guibutton.id == 1) {
            if (tile.page == 0) tile.page = tile.pages;
            else tile.page = Math.max(0, tile.page - 1);
        }
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
        if(controller != null){
            Slot invSlot = getSlotAt(mouseX, mouseY);
            if(invSlot != null) {
                //left shift click to network
                if(mouseButton == 0 && shift){
                    ItemStack stack = invSlot.getStack();
                    invSlot.setStack(controller.addItemToNetwork(stack));
                    return;
                }
            }

            for (int i = 0; i < slots.size(); i++) {
                Vec2f slot = slots.get(i);
                int id = i + (tile.page * 36);
                List<ItemStack> stacks = getFilteredStacks();
                if(mouseHoveringOverSlot(slot,mouseX,mouseY)){
                    //left click
                    if(mouseButton == 0){
                        //left shift click from network
                        if(shift){
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = stack.getItem().getMaxCount();
                            inventoryPlayer.addStack(controller.removeItemFromNetwork(stack.itemId,stack.getDamage(),stack.getStationNbt(),amount));
                            break;
                        }
                        ItemStack heldItemStack = inventoryPlayer.getCursorStack();
                        if(heldItemStack != null){
                            inventoryPlayer.setCursorStack(controller.addItemToNetwork(heldItemStack));
                        } else {
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = stack.getItem().getMaxCount();
                            inventoryPlayer.setCursorStack(controller.removeItemFromNetwork(stack.itemId,stack.getDamage(),stack.getStationNbt(),amount));
                        }
                    }
                    //right click
                    if(mouseButton == 1){
                        ItemStack heldItemStack = inventoryPlayer.getCursorStack();
                        if(heldItemStack != null){
                            Optional<ItemStack> leftovers = Optional.ofNullable(controller.addItemToNetwork(heldItemStack.split(1)));
                            if(heldItemStack.count <= 0) {
                                inventoryPlayer.setCursorStack(leftovers.orElse(null));
                            }
                            leftovers.ifPresent((S)->heldItemStack.count += S.count);
                        } else {
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = Math.min(stack.count / 2, stack.getItem().getMaxCount() / 2);
                            inventoryPlayer.setCursorStack(controller.removeItemFromNetwork(stack.itemId,stack.getDamage(),stack.getStationNbt(),amount));
                        }
                    }
                }
            }
        }
    }

    public void onClosed() {
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - backgroundWidth) / 2;
        final int centerY = (height - backgroundHeight) / 2;

        GL11.glPushMatrix();
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if(controller != null) {
                List<ItemStack> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 36);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2f slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    renderDigitalItem.render(stack, (int) slot.x, (int) slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2f slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
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

    public @UnmodifiableView List<ItemStack> getFilteredStacks() {

        NetworkController controller = tile.getController();
        if(controller != null) {
            return controller.getAllItems();
        }
        return Collections.emptyList();
    }
}
