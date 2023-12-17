package sunsetsatellite.retrostorage.gui;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.render.FontRenderer;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.containers.ContainerTaskRequest;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

import java.util.ArrayList;

public class GuiTaskRequest extends GuiContainer {
    public FontRenderer fontRenderer = RetroStorage.mc.fontRenderer;
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

    public void init() {
        I18n stringtranslate = I18n.getInstance();
        this.screenTitle = "Task Request";
        this.slotContainer = new GuiItemSlot(this.mc, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();
    }

    public void initButtons() {
        I18n stringtranslate = I18n.getInstance();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 64), 20, 20, "-"));
        controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 64), 20, 20, "+"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 30), Math.round(height / 2 - 64), 60, 20, "Request"));
    }

    protected void buttonPressed(GuiButton guibutton) {
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
                        //RetroStorage.LOGGER.debug("Processing requests work in progress!");
                        ArrayList<CompoundTag> processList = new ArrayList<>();
                        for (CompoundTag tagCompound : (ArrayList<CompoundTag>) tile.recipeContents[requestedSlotId]) {
                            processList.add(new CompoundTag(tagCompound));
                        }
                        tile.network.requestProcessing(processList);
                    } else {
                        tile.network.requestCrafting((RecipeEntryCrafting<?,?>) tile.recipeContents[requestedSlotId]);
                    }
                }
                RetroStorage.mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        //TODO: drawItemStack(requestedItem,32,32);
        fontRenderer.drawString(requestAmount+"x",10,36,0x404040);
        fontRenderer.drawString(requestedItem.stackSize+"x "+I18n.getInstance().translateKey(requestedItem.getItemName()+".name"), 55, 36, 0x404040);
        fontRenderer.drawString(this.screenTitle,95,10,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("assets/retrostorage/gui/task_request.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
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
                ArrayList<CompoundTag> tasks = (ArrayList<CompoundTag>) tile.recipeContents[requestedSlotId];
                for(CompoundTag task : tasks){
                    if(!task.getBoolean("isOutput")){
                        ItemStack stack = ItemStack.readItemStackFromNbt(task.getCompound("stack"));
                        if(stack == null) continue;
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

