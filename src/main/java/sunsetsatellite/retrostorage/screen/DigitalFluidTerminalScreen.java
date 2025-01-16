package sunsetsatellite.retrostorage.screen;



import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Vec2f;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventoryItem;
import net.teamterminus.machineessentials.util.NumberFormat;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.DigitalFluidTerminalBlockEntity;
import sunsetsatellite.retrostorage.interfaces.mixin.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.screen.handler.DigitalFluidTerminalScreenHandler;
import sunsetsatellite.retrostorage.screen.handler.DigitalTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DigitalFluidTerminalScreen extends ReSScreen implements IExtendedScreenDraw {

    public final DigitalFluidTerminalBlockEntity tile;
    //public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2f> slots = new ArrayList<>();
    public final PlayerInventory inventoryPlayer;

    public DigitalFluidTerminalScreen(PlayerInventory inventoryplayer, DigitalFluidTerminalBlockEntity tile) {
        super(new DigitalFluidTerminalScreenHandler(inventoryplayer, tile));
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
        textRenderer.draw("Digital Fluid Terminal", 40, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        if(tile.page > tile.pages) tile.page = 0;
        textRenderer.draw("Page: " + tile.page + "/" + tile.pages, 63, 93, 0x404040);
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getFluidAmount() >= controller.getFluidCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                drawCenteredString(NumberFormat.format(controller.getFluidStackAmount()) + "/" + NumberFormat.format(controller.getFluidStackCapacity()), 90, 112, color);
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

            for (int i = 0; i < slots.size(); i++) {
                Vec2f slot = slots.get(i);
                int id = i + (tile.page * 36);
                List<ItemStack> stacks = getFilteredStacks();
                if(mouseHoveringOverSlot(slot,mouseX,mouseY)){
                    //left click
                    if(mouseButton == 0){
                        ItemStack heldItemStack = inventoryPlayer.getCursorStack();
                        if(heldItemStack != null) {

                            if (drainBucket(controller)) break;

                            //insert into bucket
                            if (id >= stacks.size()) break;
                            ItemStack stack = stacks.get(id);
                            if (stack == null) break;
                            /*BlockFluid blockFluid = (BlockFluid) Block.blocksList[stack.itemID];
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
                                }
                            }*/
                        }
                    }
                    //right click
                    if(mouseButton == 1){
                        ItemStack heldItemStack = inventoryPlayer.getCursorStack();
                        if(heldItemStack != null) {
                            //insert from bucket
                            if (drainBucket(controller)) break;

                            /*if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                                List<BlockFluid> fluids = CatalystFluids.CONTAINERS.findFluidsWithAnyContainer((Item) item);
                                if (fluids != null && !fluids.isEmpty()) {
                                    //drain
                                    if (drainFluidContainer(controller, heldItemStack, item)) break;
                                }
                            }*/
                        }
                    }
                }
            }
        }
    }

    private boolean drainFluidContainer(NetworkController controller, ItemStack heldItemStack, FluidInventoryItem item) {
        /*if(item.canDrain(inventoryPlayer.getHeldItemStack())){
            int amountInItem = item.getCapacity(heldItemStack) - item.getRemainingCapacity(heldItemStack);
            FluidStack drained = item.drain(heldItemStack, amountInItem);
            Optional<FluidStack> fluidStack = Optional.ofNullable(controller.addFluidToNetwork(drained));
            fluidStack.ifPresent((S)->item.fill(S,heldItemStack));
            return true;
        }*/
        return false;
    }

    private boolean drainBucket(NetworkController controller) {
        /*if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucket) {
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
        }*/
        return false;
    }

    public void onClosed() {
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        final int centerX = (width - backgroundWidth) / 2;
        final int centerY = (height - backgroundHeight) / 2;

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
    }

    public @UnmodifiableView List<ItemStack> getFilteredStacks() {

        NetworkController controller = tile.getController();
        if(controller != null) {
            List<ItemStack> stacks = controller.getAllFluids().stream().map(FluidStack::toItemStack).collect(Collectors.toList());
            return stacks;
        }
        return Collections.emptyList();
    }
}
