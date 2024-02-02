package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.util.DigitalNetwork;
import net.sunsetsatellite.retrostorage.util.Task;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiRequestQueue extends GuiScreen {
    public FontRenderer fontRenderer = ModLoader.getMinecraftInstance().fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiTaskSlot slotContainer;
    public ArrayList<Task> list = new ArrayList<>();
    public DigitalNetwork network;
    public GuiRequestTerminal parent;

    public GuiRequestQueue(DigitalNetwork network, GuiRequestTerminal parent) {
        super();
        this.parent = parent;
        this.network = network;
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.screenTitle = "Request Queue";
        this.slotContainer = new GuiTaskSlot(this.mc, this.width, this.height, 72, this.height-64, 36, this);

        this.slotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();
    }

    public void initButtons() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {

        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScreen(int x, int y, float renderPartialTicks) {
        int xSize = 256;
        int ySize = 256;
        super.drawScreen(x, y, renderPartialTicks);
        int i = mc.renderEngine.getTexture("/assets/retrostorage/gui/request_queue.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140,this.height-175,this.width*2, this.height+351); //TODO: fix this breaking at lower resolutions than 1080p
        this.list.clear();
        if(network != null){
            list.addAll(network.requestQueue);
        }
        this.slotContainer.drawScreen(x, y, renderPartialTicks);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

}

