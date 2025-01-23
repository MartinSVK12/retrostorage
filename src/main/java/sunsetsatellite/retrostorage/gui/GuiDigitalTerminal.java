package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Vec2i;
import sunsetsatellite.retrostorage.containers.ContainerDigitalTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class GuiDigitalTerminal extends GuiContainer implements IExtendedScreenDraw {

    public final TileEntityDigitalTerminal tile;
    public GuiTextField recipeNameField;

    public final GuiRenderDigitalItem renderDigitalItem = new GuiRenderDigitalItem(Minecraft.getMinecraft(this));
    public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final InventoryPlayer inventoryPlayer;

    public GuiDigitalTerminal(InventoryPlayer inventoryplayer, TileEntityDigitalTerminal tile) {
        super(new ContainerDigitalTerminal(inventoryplayer, tile));
        ySize = 250;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2i(x,y));
            }
        }
    }

    private void handlePageScroll(boolean scrollUp) {
        if (tile.network != null) {
            INetworkController controller = tile.getController();
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
    public void tick() {
        int scrollDelta = Mouse.getDWheel();
        if (scrollDelta != 0) {
            handlePageScroll(scrollDelta > 0);
        }
    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Digital Terminal", 7, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 105) + 2, 0x404040);
        int Page = tile.page + 1;
        int Pages = tile.pages + 1;
        if(tile.page > tile.pages) tile.page = 0;
        INetworkController controller = tile.getController();

        String pageString = "";
        if (controller != null) {
            if (controller.isActive()) {
                pageString = "Page: " + Page + "/" + Pages;
                fontRenderer.drawString(pageString, 63, 75, 0x404040);
            } else {
                pageString = "Out of energy!" + Page + "/" + Pages;
                fontRenderer.drawString(pageString, 63, 75, 0x404040);
            }
        } else {
            pageString = "Network out of energy!";
            fontRenderer.drawString(pageString, 7, 75, 0xFF4040);
        }



        if(tile.network != null) {
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getAmount() >= controller.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }

                String stackMessage;

                if (controller.getStackCapacity() == 0) {
                    stackMessage = "NO DISCS";
                } else {
                    stackMessage = controller.getStackAmount() + "/" + controller.getStackCapacity();
                }

                fontRenderer.drawCenteredString(stackMessage, 145, 6, color);
            }
        }
    }

    public boolean mouseHoveringOverSlot(final Vec2i slot, int x, int y)
    {
        final int k = (width - xSize) / 2;
        final int l = (height - ySize) / 2;
        x -= k;
        y -= l;
        return x >= slot.x - 1 && x < slot.x + 16 + 1 && y >= slot.y - 1 && y < slot.y + 16 + 1;
    }

    public void init() {
        super.init();
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        if (recipeNameField != null) {
            recipeNameField.drawTextBox();
        } else {
            System.out.println("null recipeNameField");
        }
    }



    protected void buttonPressed(GuiButton guibutton) {

    }



    @Override
    public void keyTyped(char c, int i, int mouseX, int mouseY) {
        if (recipeNameField != null) {
            System.out.println(recipeNameField.getText());
            if (recipeNameField.isFocused) {
                Keyboard.enableRepeatEvents(true);
                if (c == Keyboard.KEY_ESCAPE) {
                    Keyboard.enableRepeatEvents(false);
                    recipeNameField.setFocused(false);


                } else recipeNameField.textboxKeyTyped(c, i);

            } else {
                super.keyTyped(c, i, mouseX, mouseY);
            }
        } else {
            super.keyTyped(c, i, mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {



        super.mouseClicked(mouseX, mouseY, mouseButton);
        int scrollDelta = Mouse.getEventDWheel();
        System.out.println(scrollDelta);

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        INetworkController controller = tile.getController();
        if(controller != null){
            Slot invSlot = getSlotAtPosition(mouseX, mouseY);
            if(invSlot != null) {
                //left shift click to network
                if(mouseButton == 0 && shift){
                    ItemStack stack = invSlot.getStack();
                    invSlot.putStack(controller.addItemToNetwork(stack));
                    return;
                }
            }

            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                int id = i + (tile.page * 27);
                List<ItemStack> stacks = getFilteredStacks();
                if(mouseHoveringOverSlot(slot,mouseX,mouseY)){
                    //left click
                    if(mouseButton == 0){
                        //left shift click from network
                        if(shift){
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = stack.getItem().getItemStackLimit();
                            inventoryPlayer.insertItem(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount),false);
                            break;
                        }
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null){
                            inventoryPlayer.setHeldItemStack(controller.addItemToNetwork(heldItemStack));
                        } else {
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = stack.getItem().getItemStackLimit();
                            inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount));
                        }
                    }
                    //right click
                    if(mouseButton == 1){
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null){
                            Optional<ItemStack> leftovers = Optional.ofNullable(controller.addItemToNetwork(heldItemStack.splitStack(1)));
                            if(heldItemStack.stackSize <= 0) {
                                inventoryPlayer.setHeldItemStack(leftovers.orElse(null));
                            }
                            leftovers.ifPresent((S)->heldItemStack.stackSize += S.stackSize);
                        } else {
                            if(id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if(stack == null) break;
                            int amount = Math.min(stack.stackSize / 2, stack.getItem().getItemStackLimit() / 2);
                            inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount));
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
        final int centerX = (width - xSize) / 2;
        final int centerY = (height - ySize) / 2;

        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if(controller != null) {
                List<ItemStack> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 27);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 27);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    renderDigitalItem.render(stack,slot.x,slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 27);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
                    if (inventoryPlayer.getHeldItemStack() == null && mouseHoveringOverSlot(slot,mouseX,mouseY))
                    {
                        GL11.glTranslatef(-centerX, -centerY, 0.0F);
                        boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || mc.gameSettings.alwaysShowDescriptions.value;
                        String str = tooltip.getTooltipText(stack, showDescription, null) + "\n" + TextFormatting.GRAY + stack.stackSize;
                        if(!str.isEmpty())
                        {
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            tooltip.render(str, mouseX, mouseY, 8, -8);
                        }
                        GL11.glPopMatrix();
                        break;
                    }
                }
            }
        }
    }

    public @UnmodifiableView List<ItemStack> getFilteredStacks() {
        SearchQuery query = SearchQuery.resolve("");
        try {
            Class<?> tmbRenderer = Class.forName("turing.tmb.client.TMBRenderer");
            for (Field F1 : tmbRenderer.getDeclaredFields()) {
                if (F1.getType() == GuiTextField.class) {
                    try {
                        GuiTextField field = ((GuiTextField) F1.get(null));
                        String text = field.getText();
                        query = SearchQuery.resolve(text);
                    } catch (IllegalAccessException ignored) {
                        //failed to access text field
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException ignored) {
            //tmb not installed, ignore
        }

        INetworkController controller = tile.getController();
        if(controller != null) {
            List<ItemStack> stacks = controller.getAllItems();
            if(query.mode == SearchQuery.SearchMode.ALL && query.query.getLeft() == SearchQuery.QueryType.NAME && query.scope.getLeft() == SearchQuery.SearchScope.NONE){
                String s = query.query.getRight();
                if(!Objects.equals(s, "")){
                    stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                }
            }
            return stacks;
        }
        return Collections.emptyList();
    }
}
