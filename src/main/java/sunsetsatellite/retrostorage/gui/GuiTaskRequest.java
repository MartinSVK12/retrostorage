package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.containers.ContainerTaskRequest;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CalculationResult;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;
import sunsetsatellite.retrostorage.util.crafting.CraftingCalculator;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

public class GuiTaskRequest extends GuiContainer {
    public FontRenderer fontRenderer = RetroStorage.mc.fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiRecipeItemSlot slotContainer;
    public CalculationResult calculationResult;
    public final INetworkController network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public NetworkCraftable requestedCraftable;
    public int requestAmount = 1;
    public TileEntityRequestTerminal tile;
    public GuiRenderDigitalItem guiRenderItem = new GuiRenderDigitalItem(RetroStorage.mc);

    public GuiTaskRequest(TileEntityRequestTerminal tile, ItemStack request, NetworkCraftable craftable) {
        super(new ContainerTaskRequest(tile));
        xSize = 256;
        ySize = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.network = tile.getController();
        this.requestedCraftable = craftable;
    }

    public void init() {
        this.screenTitle = "Task Request";
        this.slotContainer = new GuiRecipeItemSlot(this.mc, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();

        recalculate();
    }

    public void initButtons() {
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 64), 20, 20, "-"));
        controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 64), 20, 20, "+"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 30), Math.round(height / 2 - 64), 60, 20, "Request"));
    }

    protected void buttonPressed(GuiButton guibutton) {

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                if (requestAmount > 1) {
                    if(!mod){
                        requestAmount--;
                    } else if (shift) {
                        requestAmount -= 10;
                    } else if (control) {
                        requestAmount -= 100;
                    } else if (alt) {
                        requestAmount -= 1000;
                    }
                    requestAmount = Math.max(1, requestAmount);
                    recalculate();
                }
            }
            if (guibutton.id == 1) {
                if(!mod){
                    requestAmount++;
                } else if (shift) {
                    requestAmount += 10;
                } else if (control) {
                    requestAmount += 100;
                } else if (alt) {
                    requestAmount += 1000;
                }
                recalculate();
            }
            if (guibutton.id == 2) {
                if (requestedCraftable == null || !network.getCraftables().contains(requestedCraftable)) return;
                if (calculationResult.getType() == CalculationResultType.OK) {
                    network.requestCrafting(calculationResult.getTask());
                    ((IOpenGUI) mc.thePlayer).displayGUI(new GuiRequestQueue(network, null));
                }
            }
        }
    }

    private void recalculate() {
        if (requestedCraftable == null || !network.getCraftables().contains(requestedCraftable)) return;
        CraftingCalculator calc = new CraftingCalculator(network, requestAmount, new VariantStack(requestedItem), requestedCraftable, network.getCraftables());
        calculationResult = calc.calculate();
        lastRequestedItem = requestedItem;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        guiRenderItem.render(requestedItem, 32, 32);
        fontRenderer.drawString("x"+NumberUtil.format(requestAmount), 6, 36, 0x404040);
        fontRenderer.drawString(requestedItem.getDisplayName(), 55, 36, 0x404040);
        fontRenderer.drawString(this.screenTitle, 95, 10, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/task_request.png");
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
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 100); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.drawScreen(x, y, renderPartialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

}

