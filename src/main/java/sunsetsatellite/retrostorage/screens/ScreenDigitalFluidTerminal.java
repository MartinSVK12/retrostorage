package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TextFieldElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.menus.MenuDigitalFluidTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ScreenDigitalFluidTerminal extends ScreenContainerAbstract implements IExtendedScreenDraw {

    public final TileEntityDigitalFluidTerminal tile;
    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());
    public final TooltipElement tooltip = new TooltipElement(Minecraft.getMinecraft());
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final ContainerInventory inventoryPlayer;
    public boolean searching = false;

    public ScreenDigitalFluidTerminal(ContainerInventory inventoryplayer, TileEntityDigitalFluidTerminal tile) {
        super(new MenuDigitalFluidTerminal(inventoryplayer, tile));
        ySize = 220;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;

        for (int i = 0; i < 4; i++) {
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
        font.drawString("Digital Fluid Terminal", 40, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        if(tile.page > tile.pages) tile.page = 0;
        font.drawString("Page: " + tile.page + "/" + tile.pages + (searching ? " (Searching)" : ""), 63 - (searching ? 30 : 0), 93, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                font.drawCenteredString(NumberUtil.format(controller.getFluidStackAmount()) + "/" + NumberUtil.format(controller.getFluidStackCapacity()), 90, 112, color);
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
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, ">"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        //buttons.add(new ButtonElement(2, Math.round((float) width / 2 - 40), Math.round((float) height / 2 - 5), 20, 20, "A:"));
        //buttons.get(2).enabled = false;
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
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
        super.mouseClicked(mouseX, mouseY, mouseButton);

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        INetworkController controller = tile.getController();
        if(controller != null){
            for (int i = 0; i < slots.size(); i++) {
                Vec2i slot = slots.get(i);
                int id = i + (tile.page * 36);
                List<ItemStack> stacks = getFilteredStacks();
                if (mouseHoveringOverSlot(slot, mouseX, mouseY)) {
                    //left click
                    if (mouseButton == 0) {
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null) {
                            if (id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if (stack == null) break;
                            Block<?> blockFluid = Blocks.blocksList[stack.itemID];
                            if(blockFluid == null) break;
                            if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                                if(item instanceof ItemBucketEmpty && stack.stackSize < 1000) break;
                                if (item.canFill(heldItemStack)) {
                                    int amount = item.getRemainingCapacity(heldItemStack);
                                    FluidStack fluidStack = controller.removeFluidFromNetwork(blockFluid.id(), amount);
                                    item.fill(fluidStack,heldItemStack);
                                    if(fluidStack.amount <= 0) fluidStack = null;
                                    if(fluidStack != null){
                                        controller.addFluidToNetwork(fluidStack);
                                    }
                                }
                            }
                        }
                    } else if (mouseButton == 1) { //right click
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null) {
                            if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                                if (item.canDrain(heldItemStack)) {
                                    int amount = item.getCurrentFluid(heldItemStack).amount;
                                    if (amount > 0) {
                                        FluidStack drained = item.drain(heldItemStack, amount);
                                        if (drained != null) {
                                            Optional<FluidStack> fluidStack = Optional.ofNullable(controller.addFluidToNetwork(drained));
                                            fluidStack.ifPresent((S) -> item.fill(S, heldItemStack));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //TODO: reimplement player fluid i/o
        }
    }

    /*private boolean drainFluidContainer(INetworkController controller, ItemStack heldItemStack, IItemFluidContainer item) {
        if(item.canDrain(inventoryPlayer.getHeldItemStack())){
            int amountInItem = item.getCapacity(heldItemStack) - item.getRemainingCapacity(heldItemStack);
            FluidStack drained = item.drain(heldItemStack, amountInItem);
            Optional<FluidStack> fluidStack = Optional.ofNullable(controller.addFluidToNetwork(drained));
            fluidStack.ifPresent((S)->item.fill(S,heldItemStack));
            return true;
        }
        return false;
    }*/

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
                this.tile.pages = (int) (double) (stacks.size() / 36);
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    renderDigitalItem.render(stack,slot.x,slot.y,mouseHoveringOverSlot(slot,mouseX,mouseY));
                }
                for (int i = 0; i < slots.size(); i++) {
                    Vec2i slot = slots.get(i);
                    ItemStack stack;
                    int id = i + (tile.page * 36);
                    if(id >= stacks.size()) break;
                    stack = stacks.get(id);
                    if(stack == null) continue;
                    final ContainerInventory inventoryPlayer = mc.thePlayer.inventory;
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
        INetworkController controller = tile.getController();
        if(controller != null) {
            List<ItemStack> stacks = controller.getAllFluids().stream().map(FluidStack::toItemStack).collect(Collectors.toList());
            if(query.mode == SearchQuery.SearchMode.ALL && query.query.getLeft() == SearchQuery.QueryType.NAME && query.scope.getLeft() == SearchQuery.SearchScope.NONE){
                String s = query.query.getRight();
                if(!Objects.equals(s, "")){
                    stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                    searching = true;
                }
            }
            return stacks;
        }
        return Collections.emptyList();
    }
}
