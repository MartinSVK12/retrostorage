package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.menus.MenuTaskRequest;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CalculationResult;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;
import sunsetsatellite.retrostorage.util.crafting.CraftingCalculator;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

public class ScreenTaskRequest extends ScreenContainerAbstract {
    protected String screenTitle = "Scroll Container";
    private RecipeItemSlotElement slotContainer;
    public CalculationResult calculationResult;
    public final INetworkController network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public NetworkCraftable requestedCraftable;
    public int requestAmount = 1;
    public TileEntityRequestTerminal tile;
    public DigitalItemElement guiRenderItem = new DigitalItemElement(Minecraft.getMinecraft());

    public ScreenTaskRequest(TileEntityRequestTerminal tile, ItemStack request, NetworkCraftable craftable) {
        super(new MenuTaskRequest(null,tile));
        xSize = 256;
        ySize = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.network = tile.getController();
        this.requestedCraftable = craftable;
    }

    public void init() {
        this.screenTitle = "Task Request";
        this.slotContainer = new RecipeItemSlotElement(this.mc, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.buttons, 4, 5);
        this.initButtons();

        recalculate();
    }

    public void initButtons() {
        buttons.add(new ButtonElement(0, Math.round(width / 2f + 50), Math.round(height / 2f - 64), 20, 20, "-"));
        buttons.add(new ButtonElement(1, Math.round(width / 2f - 70), Math.round(height / 2f - 64), 20, 20, "+"));// /2 - 34, - 150
        buttons.add(new ButtonElement(2, Math.round(width / 2f - 30), Math.round(height / 2f - 64), 60, 20, "Request"));
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {

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
                    mc.displayScreen(new ScreenRequestQueue(network, null));
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
        font.drawString("x"+NumberUtil.format(requestAmount), 6, 36, 0x404040);
        font.drawString(requestedItem.getDisplayName(), 55, 36, 0x404040);
        font.drawString(this.screenTitle, 95, 10, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/task_request.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int x, int y, float renderPartialTicks) {
        buttons.get(2).enabled = calculationResult.getType() == CalculationResultType.OK;
        super.render(x, y, renderPartialTicks);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 100); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.drawScreen(x, y, renderPartialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public Font getFont(){
        return this.font;
    }
}

