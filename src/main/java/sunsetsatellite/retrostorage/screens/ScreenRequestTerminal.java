package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TextFieldElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.collection.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.menus.MenuRequestTerminal;
import sunsetsatellite.retrostorage.mp.PacketClearRequestQueue;
import sunsetsatellite.retrostorage.mp.terminal.request.PacketRequestTerminalRequestContents;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.api.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScreenRequestTerminal extends ScreenContainerAbstract implements IExtendedScreenDraw {

    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());
    public final TooltipElement tooltip = new TooltipElement(Minecraft.getMinecraft());
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public TileEntityRequestTerminal tile;
    public Player player;
    public boolean searching = false;
    public String searchQuery = "";
    public TickTimer requestTimer = new TickTimer(this, ()-> NetworkHandler.sendToServer(new PacketRequestTerminalRequestContents(searchQuery)), 10, true);

    public ScreenRequestTerminal(ContainerInventory invPlayer, TileEntityRequestTerminal tile) {
        super(new MenuRequestTerminal(invPlayer, tile));
        ySize = 220;
        this.tile = tile;
        this.player = invPlayer.player;

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
        font.drawString("Request Terminal", 8, 6, 0x404040);
        if(searching) font.drawCenteredString("<< Searching >>", (xSize / 2), -8, 0xFFFFFF);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        String pageText = "Page: " + tile.page + "/" + tile.pages;
        int pageTextWidth = font.getStringWidth(pageText);
        font.drawString(pageText, xSize - 8 - pageTextWidth, 6, 0x404040);
        if(tile.getController() != null){
            String craftableSize = String.valueOf(tile.getController().getCraftables().size());
            int strWidth = font.getStringWidth(craftableSize);
            int color = 0xFFFFFF;
            font.drawStringWithShadow(craftableSize, xSize - 8 - strWidth, (ySize - 95) + 2, color);
        }
    }

    public void init() {
        super.init();
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        buttons.add(new ButtonElement(0, j-27+6,k+8+8+29, 21, 21, "↓"));
        buttons.add(new ButtonElement(1, j-27+6,k+8+8, 21, 21, "↑"));
        buttons.add(new ButtonElement(2, j-27+6,k+8+8+29*2, 21, 21, "Q"));
        buttons.add(new ButtonElement(3, j-27+6,k+8+8+29*3, 21, 21, "X"));
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/request_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        drawTexturedModalRect(j-27, k+8, 177, 0, 27, 123);
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
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
            mc.displayScreen(new ScreenRequestQueue(tile.getController(), this));
        }
        if (guibutton.id == 3) {
            INetworkController c = tile.getController();
            if (c != null) {
                c.clearRequestQueue();
                Vec3i pos = c.getPosition();
                if(EnvironmentHelper.isClientWorld()){
                    NetworkHandler.sendToServer(new PacketClearRequestQueue(pos.x, pos.y, pos.z));
                }
                player.sendTranslatedChatMessage("action.retrostorage.clearTaskQueue");
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

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        //RetroStorage.mc.displayGuiScreen(new GuiTaskRequest(tile, slot.getStack().copy(), ((SlotViewOnly) slot).variableIndex));

        INetworkController controller = tile.getController();
        if(controller != null) {
            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                int id = i + (tile.page * 54);
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                if (mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                    if (mouseButton == 0) {
                        if (id >= stacks.size()) break;
                        ItemStack stack = stacks.get(id).getLeft();
                        NetworkCraftable craftable = stacks.get(id).getRight();
                        if (stack == null) break;
                        List<NetworkCraftable> craftables = stacks.stream().map(Pair::getRight).collect(Collectors.toList());
                        mc.displayScreen(new ScreenTaskRequest(tile, stack, craftable, craftables));
                    }
                }
            }
        }
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - xSize) / 2;
        final int centerY = (height - ySize) / 2;

        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if(controller != null) {
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                this.tile.pages = (int) (double) (stacks.size() / 54);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getLeft();
                    if(stack == null) continue;
                    renderDigitalItem.render(stack,slot.x,slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 54);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getLeft();
                    if(stack == null) continue;
                    final ContainerInventory inventoryPlayer = mc.thePlayer.inventory;
                    if (inventoryPlayer.getHeldItemStack() == null && mouseHoveringOverSlot(slot,mouseX,mouseY))
                    {
                        GL11.glTranslatef(-centerX, -centerY, 0.0F);
                        boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || mc.gameSettings.itemDescriptions.value == DescriptionPromptEnum.ALWAYS_SHOW;
                        String str = tooltip.getTooltipText(stack, showDescription, null);
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

    public @UnmodifiableView List<Pair<ItemStack,NetworkCraftable>> getFilteredStacks() {

        if(EnvironmentHelper.isClientWorld()){
            return ((MenuRequestTerminal) inventorySlots).networkCraftables;
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

        searching = false;
        searchQuery = "";
        INetworkController controller = tile.getController();
        if(controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack,NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if(NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(),NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            if(query.mode == SearchQuery.SearchMode.ALL && query.query.getLeft() == SearchQuery.QueryType.NAME && query.scope.getLeft() == SearchQuery.SearchScope.NONE){
                String s = query.query.getRight();
                if(!Objects.equals(s, "")){
                    searchQuery = s;
                    stacks = stacks.stream().filter(P -> P.getLeft().getDisplayName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                    searching = true;
                }
            }
            return stacks;
        }
        return Collections.emptyList();
    }
}
