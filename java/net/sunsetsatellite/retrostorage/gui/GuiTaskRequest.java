package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.containers.ContainerTaskRequest;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import net.sunsetsatellite.retrostorage.util.DigitalNetwork;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiTaskRequest extends GuiContainer {
    public FontRenderer fontRenderer = ModLoader.getMinecraftInstance().fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiItemSlot slotContainer;
    public ArrayList<Object> list = new ArrayList<>();
    public DigitalNetwork network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public int requestAmount = 1;
    public TileEntityRequestTerminal tile;
    public int requestedSlotId;

    public GuiTaskRequest(TileEntityRequestTerminal tile, ItemStack request, int slotId) {
        super(new ContainerTaskRequest(tile));
        xSize = 256;
        ySize = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.requestedSlotId = slotId;
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.screenTitle = "Task Request";
        this.slotContainer = new GuiItemSlot(this.mc, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();
    }

    public void initButtons() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 64), 20, 20, "-"));
        controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 64), 20, 20, "+"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 30), Math.round(height / 2 - 64), 60, 20, "Request"));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if(guibutton.id == 0){
                if(requestAmount > 1){
                    requestAmount--;
                }
            }
            if(guibutton.id == 1){
                requestAmount++;
            }
            if(guibutton.id == 2){
                for (int i = 0; i < requestAmount; i++) {
                    if(tile.recipeContents[requestedSlotId] instanceof ArrayList){
                        //RetroStorage.LOGGER.info("Processing requests work in progress!");
                        ArrayList<NBTTagCompound> processList = new ArrayList<>();
                        for (NBTTagCompound tagCompound : (ArrayList<NBTTagCompound>) tile.recipeContents[requestedSlotId]) {
                            processList.add((NBTTagCompound) tagCompound.copy());
                        }
                        tile.network.requestProcessing(processList);
                    } else {
                        tile.network.requestCrafting((IRecipe) tile.recipeContents[requestedSlotId]);
                    }
                }
                ModLoader.getMinecraftInstance().displayGuiScreen(null);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        //drawItemStack(requestedItem,32,32);
        fontRenderer.drawString(requestAmount+"x",10,36,0x404040);
        fontRenderer.drawString(requestedItem.stackSize+"x "+StringTranslate.getInstance().translateKey(requestedItem.getItem().getItemNameIS(requestedItem)+".name"), 55, 36, 0x404040);
        fontRenderer.drawString(this.screenTitle,95,10,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/task_request.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x, y, renderPartialTicks);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140,this.height-175,this.width*2, this.height+100); //TODO: fix this breaking at lower resolutions than 1080p
        if(tile.recipeContents[requestedSlotId] instanceof ArrayList) {
            if(lastRequestedItem == null || !(requestedItem.isItemEqual(lastRequestedItem))){
                this.list.clear();
                ArrayList<NBTTagCompound> tasks = (ArrayList<NBTTagCompound>) tile.recipeContents[requestedSlotId];
                for(NBTTagCompound task : tasks){
                    if(!task.getBoolean("isOutput")){
                        ItemStack stack = ItemStack.loadItemStackFromNBT(task.getCompoundTag("stack"));//new ItemStack(task.getCompoundTag("stack"));
                        list.add(stack);
                    }
                }
                lastRequestedItem = requestedItem;
            }
        } else {
            if(lastRequestedItem == null || !(requestedItem.isItemEqual(lastRequestedItem))){
                this.list.clear();
                list.addAll(tile.network.getRequirements(requestedItem));
                lastRequestedItem = requestedItem;
            }
        }


        /*list.addAll(RetroStorage.condenseItemList(RetroStorage.getRecipeItems(RetroStorage.findRecipesByOutputUsingList(requestedItem,tile.network.getAvailableRecipes()).get(0))));
        if(list.size() == 0){
            list.add("noRecipesError");
        }*/
        /*if(network != null){
            list.addAll(network.requestQueue);
        }*/
        this.slotContainer.drawScreen(x, y, renderPartialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

}

