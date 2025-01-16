package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.teamterminus.machineessentials.util.NumberFormat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.TaskRequestScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.calc.CalculationResult;
import sunsetsatellite.retrostorage.util.crafting.calc.CalculationResultType;
import sunsetsatellite.retrostorage.util.crafting.calc.CraftingCalculator;

public class TaskRequestScreen extends HandledScreen {
    protected String screenTitle = "Scroll Container";
    private RecipeItemSlotScreen slotContainer;
    public CalculationResult calculationResult;
    public final NetworkController network;
    public ItemStack requestedItem;
    public ItemStack lastRequestedItem;
    public NetworkCraftable requestedCraftable;
    public int requestAmount = 1;
    public RequestTerminalBlockEntity tile;
    public RenderDigitalItem guiRenderItem;

    public TaskRequestScreen(RequestTerminalBlockEntity tile, ItemStack request, NetworkCraftable craftable) {
        super(new TaskRequestScreenHandler(tile));
        backgroundWidth = 256;
        backgroundHeight = 256;
        this.requestedItem = request;
        this.tile = tile;
        this.network = tile.getController();
        this.requestedCraftable = craftable;
        this.guiRenderItem = new RenderDigitalItem(Minecraft.INSTANCE);
    }

    public void init() {
        this.screenTitle = "Task Request";
        this.slotContainer = new RecipeItemSlotScreen(this.minecraft, this.width, this.height, 140, this.height - 48, 36, this);

        this.slotContainer.registerScrollButtons(this.buttons, 4, 5);
        this.initButtons();

        recalculate();
    }

    public void initButtons() {
        buttons.add(new ButtonWidget(0, Math.round(width / 2 + 50), Math.round(height / 2 - 64), 20, 20, "-"));
        buttons.add(new ButtonWidget(1, Math.round(width / 2 - 70), Math.round(height / 2 - 64), 20, 20, "+"));// /2 - 34, - 150
        buttons.add(new ButtonWidget(2, Math.round(width / 2 - 30), Math.round(height / 2 - 64), 60, 20, "Request"));
    }

    protected void buttonClicked(ButtonWidget guibutton) {

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean mod = shift || control || alt || space;

        if (guibutton.active) {
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
                    minecraft.setScreen(new RequestQueueScreen(tile.getController(), null));
                    //((IOpenGUI) minecraft.thePlayer).displayGUI(new RequestQueueScreen(network, null));
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
    protected void drawForeground() {
        guiRenderItem.render(requestedItem, 32, 32);
        textRenderer.draw("x"+ NumberFormat.format(requestAmount), 6, 36, 0x404040);
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(requestedItem.getTranslationKey()), 55, 36, 0x404040);
        textRenderer.draw(this.screenTitle, 95, 10, 0x404040);
    }
    

    @Override
    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/task_request.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    public void render(int x, int y, float renderPartialTicks) {
        ((ButtonWidget) buttons.get(2)).active = calculationResult.getType() == CalculationResultType.OK;
        super.render(x, y, renderPartialTicks);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 100); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.render(x, y, renderPartialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    public void drawString(String s, int x, int y, int color){
        this.textRenderer.draw(s,x,y,color);
    }


}

