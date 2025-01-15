package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.core.util.Vec2i;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.containers.ContainerDigitalFluidTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class GuiDigitalFluidTerminal extends GuiContainer implements IExtendedScreenDraw {

    public final TileEntityDigitalFluidTerminal tile;
    public final GuiRenderDigitalItem renderDigitalItem = new GuiRenderDigitalItem(Minecraft.getMinecraft(this));
    public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final InventoryPlayer inventoryPlayer;

    public GuiDigitalFluidTerminal(InventoryPlayer inventoryplayer, TileEntityDigitalFluidTerminal tile) {
        super(new ContainerDigitalFluidTerminal(inventoryplayer, tile));
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

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Digital Fluid Terminal", 40, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        if(tile.page > tile.pages) tile.page = 0;
        fontRenderer.drawString("Page: " + tile.page + "/" + tile.pages, 63, 93, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                fontRenderer.drawCenteredString(NumberUtil.format(controller.getFluidStackAmount()) + "/" + NumberUtil.format(controller.getFluidStackCapacity()), 90, 112, color);
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
        controlList.add(new GuiButton(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, ">"));
        controlList.add(new GuiButton(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        //controlList.add(new GuiButton(2, Math.round((float) width / 2 - 40), Math.round((float) height / 2 - 5), 20, 20, "A:"));
        //controlList.get(2).enabled = false;
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
                if(mouseHoveringOverSlot(slot,mouseX,mouseY)){
                    //left click
                    if(mouseButton == 0){
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null) {

                            if (drainBucket(controller)) break;

                            //insert into bucket
                            if (id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if (stack == null) break;
                            BlockFluid blockFluid = (BlockFluid) Block.blocksList[stack.itemID];
                            if (stack.stackSize >= 1000) {
                                if (inventoryPlayer.getHeldItemStack() != null
                                        && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucketEmpty
                                        && CatalystFluids.CONTAINERS.findEmptyContainers(blockFluid).contains(inventoryPlayer.getHeldItemStack().getItem())) {
                                    Item item = CatalystFluids.CONTAINERS.findFilledContainersWithContainer(blockFluid, inventoryPlayer.getHeldItemStack().getItem()).get(0);
                                    if (item != null) {
                                        ItemStack filledBucket = new ItemStack(item, 1);
                                        if (controller.countFluids(blockFluid.id) >= 1000) {
                                            controller.removeFluidFromNetwork(blockFluid.id, 1000);
                                            if (inventoryPlayer.getHeldItemStack().stackSize > 1) {
                                                boolean isInvFull = true;
                                                for (int j = 0; i < inventoryPlayer.mainInventory.length; ++j) {
                                                    if (inventoryPlayer.mainInventory[j] == null) {
                                                        isInvFull = false;
                                                        break;
                                                    }
                                                }
                                                if (isInvFull) {
                                                    inventoryPlayer.player.dropPlayerItem(filledBucket);
                                                } else {
                                                    inventoryPlayer.insertItem(filledBucket, false);
                                                }
                                                inventoryPlayer.getHeldItemStack().stackSize--;
                                                break;
                                            } else {
                                                inventoryPlayer.setHeldItemStack(filledBucket);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            //I/O from fluid containers
                            if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                                List<BlockFluid> fluids = CatalystFluids.CONTAINERS.findFluidsWithAnyContainer((Item) item);
                                if(fluids != null && !fluids.isEmpty()){
                                    /*int amountInItem = item.getCapacity(heldItemStack) - item.getRemainingCapacity(heldItemStack);
                                    if(amountInItem >= stack.stackSize){
                                        //drain
                                        if (drainFluidContainer(controller, heldItemStack, item)) break;
                                    } else {*/
                                        if(CatalystFluids.CONTAINERS.findContainers(blockFluid).contains(item)) {
                                            //fill
                                            if(item.canFill(heldItemStack)){
                                                int amount = item.getRemainingCapacity(heldItemStack);
                                                FluidStack fluidStack = controller.removeFluidFromNetwork(blockFluid.id, amount);
                                                item.fill(fluidStack,heldItemStack);
                                                if(fluidStack.amount <= 0) fluidStack = null;
                                                if(fluidStack != null){
                                                    controller.addFluidToNetwork(fluidStack);
                                                }
                                            }
                                        }
                                    //}
                                }
                            }
                        }
                    }
                    //right click
                    if(mouseButton == 1){
                        ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                        if(heldItemStack != null) {
                            //insert from bucket
                            if (drainBucket(controller)) break;

                            if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                                List<BlockFluid> fluids = CatalystFluids.CONTAINERS.findFluidsWithAnyContainer((Item) item);
                                if (fluids != null && !fluids.isEmpty()) {
                                    //drain
                                    if (drainFluidContainer(controller, heldItemStack, item)) break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean drainFluidContainer(INetworkController controller, ItemStack heldItemStack, IItemFluidContainer item) {
        if(item.canDrain(inventoryPlayer.getHeldItemStack())){
            int amountInItem = item.getCapacity(heldItemStack) - item.getRemainingCapacity(heldItemStack);
            FluidStack drained = item.drain(heldItemStack, amountInItem);
            Optional<FluidStack> fluidStack = Optional.ofNullable(controller.addFluidToNetwork(drained));
            fluidStack.ifPresent((S)->item.fill(S,heldItemStack));
            return true;
        }
        return false;
    }

    private boolean drainBucket(INetworkController controller) {
        if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucket) {
            ItemBucket bucket = (ItemBucket) inventoryPlayer.getHeldItemStack().getItem();
            List<BlockFluid> fluids = CatalystFluids.CONTAINERS.findFluidsWithFilledContainer(bucket);
            if (!fluids.isEmpty()) {
                BlockFluid fluid = fluids.get(0);
                if (controller.getFluidAmount() + 1000 <= controller.getFluidCapacity()) {
                    controller.addFluidToNetwork(new FluidStack(fluid, 1000));
                    inventoryPlayer.setHeldItemStack(new ItemStack(bucket.getContainerItem(), 1));
                    return true;
                }
            }
        }
        return false;
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
            List<ItemStack> stacks = controller.getAllFluids().stream().map(FluidStack::toItemStack).collect(Collectors.toList());
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
