package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TextFieldElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.menus.MenuDigitalTerminal;
import sunsetsatellite.retrostorage.mp.terminal.item.PacketTerminalInteraction;
import sunsetsatellite.retrostorage.mp.terminal.item.PacketTerminalRequestContents;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.api.INetworkController;
import turing.tmb.client.TMBRenderer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ScreenDigitalTerminal extends ScreenContainerAbstract implements IExtendedScreenDraw {

    public final TileEntityDigitalTerminal tile;
    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());
    public final TooltipElement tooltip = new TooltipElement(Minecraft.getMinecraft());
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final ContainerInventory inventoryPlayer;
    public boolean searching = false;
    public String searchQuery = "";
    public int lastVirtualSlotClicked = -1;
    public TickTimer requestTimer = new TickTimer(this, ()-> NetworkHandler.sendToServer(new PacketTerminalRequestContents(searchQuery)), 10, true);

    public ScreenDigitalTerminal(ContainerInventory inventoryplayer, TileEntityDigitalTerminal tile) {
        super(new MenuDigitalTerminal(inventoryplayer, tile));
        ySize = 220;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2i(x,y));
            }
        }
    }

    @Override
    public void tick() {
        int scrollDelta = Mouse.getDWheel();
        if (scrollDelta != 0) {
            handlePageScroll(scrollDelta > 0);
        }
        requestTimer.tick();
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


    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Digital Terminal", 8, 6, 0x404040);
        if(searching) drawStringCenteredShadow(fontRenderer, "<< Searching >>", (xSize / 2), -8, 0xFFFFFF);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);
        if(tile.page > tile.pages) tile.page = 0;

        String pageText = "Page: " + tile.page + "/" + tile.pages;
        int pageTextWidth = fontRenderer.stringWidth(pageText);
        drawStringNoShadow(fontRenderer,pageText, xSize - 8 - pageTextWidth, 6, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getAmount() >= controller.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                String stackText = controller.getStackAmount() + "/" + controller.getStackCapacity();
                int stackTextWidth = fontRenderer.stringWidth(stackText);
                drawStringShadow(fontRenderer, stackText, xSize - 8 - stackTextWidth, (ySize - 95) + 2, color);
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

    public int getVirtualSlotAtPosition(int x, int y){
        for (int i = 0; i < slots.size(); i++) {
            Vec2i slot = slots.get(i);
            if (mouseHoveringOverSlot(slot, x, y)) {
                return i;
            }
        }
        return -1;
    }

    public void init() {
        super.init();
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        buttons.add(new ButtonElement(0, j-27+6,k+27+8+29, 21, 21, "↓"));
        buttons.add(new ButtonElement(1, j-27+6,k+27+8, 21, 21, "↑"));
        //buttons.add(new ButtonElement(2, Math.round((float) width / 2 - 40), Math.round((float) height / 2 - 5), 20, 20, "A:"));
        //buttons.get(2).enabled = false;
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/digital_terminal.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        drawTexturedModalRect(j-27, k+27, 177, 0, 27, 65);
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
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
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        Slot invSlot = getSlotAtPosition(mouseX, mouseY);
        int slotId = -1;
        int vSlotId = getVirtualSlotAtPosition(mouseX, mouseY);
        if(invSlot != null) slotId = invSlot.index;

        NetworkHandler.sendToServer(new PacketTerminalInteraction(searchQuery, slotId, vSlotId, tile.page, mouseButton,shift));

        lastVirtualSlotClicked = vSlotId;

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }



    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - xSize) / 2;
        final int centerY = (height - ySize) / 2;

        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if(controller != null) {
                List<ItemStack> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 54);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    renderDigitalItem.render(stack,slot.x,slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    final ContainerInventory inventoryPlayer = mc.thePlayer.inventory;
                    if (inventoryPlayer.getHeldItemStack() == null && mouseHoveringOverSlot(slot,mouseX,mouseY))
                    {
                        GLRenderer.modelM4f().translate(-centerX, -centerY, 0.0F);
                        boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || GameSettings.ITEM_DESCRIPTIONS.value == DescriptionPromptEnum.ALWAYS_SHOW;
                        String str = tooltip.getTooltipText(stack, showDescription, null) + "\n" + TextFormatting.GRAY + stack.stackSize;
                        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        tooltip.render(str, mouseX, mouseY, 8, -8);
						//GLRenderer.popFrame(); //todo: suspicious call to popFrame
                        break;
                    }
                }
            }
        }
    }

    public @UnmodifiableView List<ItemStack> getFilteredStacks() {

        if(EnvironmentHelper.isMultiplayerClient()){
            return ((MenuDigitalTerminal) inventorySlots).networkStacks;
        }

        SearchQuery query = SearchQuery.resolve("");

        //miniscule amounts of reflection
        try {
            Class<?> tmbRenderer = Class.forName("turing.tmb.client.TMBRenderer");
            for (Field F1 : tmbRenderer.getDeclaredFields()) {
                if (F1.getType() == TextFieldElement.class) {
                    try {
                        TextFieldElement field = ((TextFieldElement) F1.get(null));
                        if(field != null) {
                            String text = field.getText();
                            query = SearchQuery.resolve(text);
                        }
                    } catch (IllegalAccessException ignored) {
                        //failed to access text field
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException ignored) {
            //tmb not installed, ignore
        }

        /*String text = TMBRenderer.search.getText();
        query = SearchQuery.resolve(text);*/

        searching = false;
        searchQuery = "";
        INetworkController controller = tile.getController();
        if(controller != null) {
            List<ItemStack> stacks = controller.getAllItems();
            if(query.mode == SearchQuery.SearchMode.ALL && query.query.getLeft() == SearchQuery.QueryType.NAME && query.scope.getLeft() == SearchQuery.SearchScope.NONE){
                String s = query.query.getRight();
                if(!Objects.equals(s, "")){
                    searchQuery = s;
                    stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                    searching = true;
                }
            }
            return stacks;
        }
        return Collections.emptyList();
    }
}
