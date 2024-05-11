package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.render.FontRenderer;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.containers.ContainerTaskRequest;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.crafting.*;

public class GuiTaskRequest extends GuiContainer {
    public FontRenderer fontRenderer = RetroStorage.mc.fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiRecipeItemSlot slotContainer;
    public CalculationResult calculationResult;
    public final DigitalNetwork network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public int requestAmount = 1;
    public TileEntityRequestTerminal tile;
    public int requestedSlotId;
    public GuiRenderDigitalItem guiRenderItem = new GuiRenderDigitalItem(RetroStorage.mc);

    public GuiTaskRequest(TileEntityRequestTerminal tile, ItemStack request, int slotId) {
        super(new ContainerTaskRequest(tile));
        xSize = 256;
        ySize = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.network = tile.network;
        this.requestedSlotId = slotId;
    }

    public void init() {
        I18n stringtranslate = I18n.getInstance();
        this.screenTitle = "Task Request";
        this.slotContainer = new GuiRecipeItemSlot(this.mc, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();

        recalculate();
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
                    recalculate();
                }
            }
            if(guibutton.id == 1){
                requestAmount++;
                recalculate();
            }
            if(guibutton.id == 2){
                if(requestedSlotId < 0 || requestedSlotId >= network.knownCraftables.size()) return;
                if(calculationResult.getType() == CalculationResultType.OK){
                    network.requestCrafting(calculationResult.getTask());
                    ((IOpenGUI)mc.thePlayer).displayGUI(new GuiRequestQueue(tile.network, null));
                }
            }
        }
    }

    private void recalculate(){
        if(requestedSlotId < 0 || requestedSlotId >= network.knownCraftables.size()) return;
        NetworkCraftable craftable = network.knownCraftables.get(requestedSlotId);
        CraftingCalculator calc = new CraftingCalculator(network,requestAmount,requestedItem,craftable,network.knownCraftables);
        calculationResult = calc.calculate();
        lastRequestedItem = requestedItem;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        guiRenderItem.render(requestedItem,32,32);
        fontRenderer.drawString(requestAmount+"x",10,36,0x404040);
        fontRenderer.drawString(requestedItem.getDisplayName(), 55, 36, 0x404040);
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
        controlList.get(2).enabled = calculationResult.getType() == CalculationResultType.OK;
        super.drawScreen(x, y, renderPartialTicks);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140,this.height-175,this.width*2, this.height+100); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.drawScreen(x, y, renderPartialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

}

