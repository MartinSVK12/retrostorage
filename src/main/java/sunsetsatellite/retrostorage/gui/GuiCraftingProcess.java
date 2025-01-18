package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

import java.util.ArrayList;

public class GuiCraftingProcess extends GuiScreen {
    public FontRenderer fontRenderer = RetroStorage.mc.fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiCraftingProcessStep slotContainer;
    public ArrayList<CraftingProcess.Step> list = new ArrayList<>();
    public CraftingProcess craftingProcess;

    public GuiCraftingProcess(CraftingProcess process) {
        super(null);
        this.craftingProcess = process;
    }

    public void init() {
        I18n stringtranslate = I18n.getInstance();
        this.screenTitle = "Crafting Process";
        this.slotContainer = new GuiCraftingProcessStep(this.mc, this.width, this.height, 72, this.height - 64, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();
    }

    public void initButtons() {
        I18n stringtranslate = I18n.getInstance();
    }

    protected void buttonPressed(GuiButton guibutton) {
        if (guibutton.enabled) {

        }
    }

    @Override
    public boolean pausesGame() {
        return false;
    }

    public void drawScreen(int x, int y, float renderPartialTicks) {
        int xSize = 256;
        int ySize = 256;
        super.drawScreen(x, y, renderPartialTicks);
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/request_queue.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        this.drawStringCentered(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 351); //TODO: fix this breaking at lower resolutions than 1080p
        this.list.clear();
        list.addAll(craftingProcess.steps);
        /*if (network != null) {
            list.addAll(network.getRequestQueue());
            for (CraftingTask task : network.getRequestQueue()) {
                slotContainer.posZ = (36 * (task.nodes.all().size() + 2));
            }
        }*/
        this.slotContainer.drawScreen(x, y, renderPartialTicks);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

}

