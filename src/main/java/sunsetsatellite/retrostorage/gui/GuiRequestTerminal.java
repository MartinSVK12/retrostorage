package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.collection.Pair;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Vec2i;
import sunsetsatellite.retrostorage.containers.ContainerRequestTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuiRequestTerminal extends GuiContainer implements IExtendedScreenDraw {

    public final GuiRenderDigitalItem renderDigitalItem = new GuiRenderDigitalItem(Minecraft.getMinecraft(this));
    public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public TileEntityRequestTerminal tile;
    public EntityPlayer player;

    public GuiRequestTerminal(InventoryPlayer invPlayer, TileEntityRequestTerminal tile) {
        super(new ContainerRequestTerminal(invPlayer, tile));
        ySize = 220;
        this.tile = tile;
        this.player = invPlayer.player;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2i(x,y));
            }
        }

    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Request Terminal", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString("Page: " + tile.page + "/" + tile.pages, 65, 93, 0x404040);
        if(tile.getController() != null){
            fontRenderer.drawCenteredString(String.valueOf(tile.getController().getCraftables().size()), 88, 112, 0xFFFFFFFF);
        }
    }

    public void init() {
        super.init();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
        controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 50), Math.round(height / 2 - 5), 20, 20, "Q"));
        controlList.add(new GuiButton(3, Math.round(width / 2 + 30), Math.round(height / 2 - 5), 20, 20, "X"));
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void buttonPressed(GuiButton guibutton) {
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
            ((IOpenGUI) player).displayGUI(new GuiRequestQueue(tile.getController(), this));
        }
        if (guibutton.id == 3) {
            if (tile.getController() != null) {
                tile.getController().clearRequestQueue();
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
                int id = i + (tile.page * 36);
                @UnmodifiableView List<Pair<ItemStack, NetworkCraftable>> stacks = getFilteredStacks();
                if (mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                    if (mouseButton == 0) {
                        if (id >= stacks.size()) break;
                        ItemStack stack = stacks.get(id).getLeft();
                        NetworkCraftable craftable = stacks.get(id).getRight();
                        if (stack == null) break;
                        mc.displayGuiScreen(new GuiTaskRequest(tile, stack, craftable));
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
                this.tile.pages = (int) (double) (stacks.size() / 36);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getLeft();
                    if(stack == null) continue;
                    renderDigitalItem.render(stack,slot.x,slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id).getLeft();
                    if(stack == null) continue;
                    final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
                    if (inventoryPlayer.getHeldItemStack() == null && mouseHoveringOverSlot(slot,mouseX,mouseY))
                    {
                        GL11.glTranslatef(-centerX, -centerY, 0.0F);
                        boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || mc.gameSettings.alwaysShowDescriptions.value;
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

        SearchQuery query = SearchQuery.resolve("");

        //miniscule amounts of reflection
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
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack,NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if(NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(),NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            if(query.mode == SearchQuery.SearchMode.ALL && query.query.getLeft() == SearchQuery.QueryType.NAME && query.scope.getLeft() == SearchQuery.SearchScope.NONE){
                String s = query.query.getRight();
                if(!Objects.equals(s, "")){
                    stacks = stacks.stream().filter(P -> P.getLeft().getDisplayName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                }
            }
            return stacks;
        }
        return Collections.emptyList();
    }
}
