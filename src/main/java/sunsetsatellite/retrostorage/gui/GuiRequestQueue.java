package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.Task;

import java.util.ArrayList;

public class GuiRequestQueue extends GuiScreen {
    public FontRenderer fontRenderer = RetroStorage.mc.fontRenderer;
    protected String screenTitle = "Scroll Container";
    private GuiTaskSlot slotContainer;
    public ArrayList<Task> list = new ArrayList<>();
    public DigitalNetwork network;
    public GuiRequestTerminal parent;

    public GuiRequestQueue(DigitalNetwork network, GuiRequestTerminal parent) {
        super(null);
        this.parent = parent;
        this.network = network;
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.screenTitle = "Request Queue";
        this.slotContainer = new GuiTaskSlot(this.mc, this.width, this.height, 72, this.height - 72, 36, this);

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
        int i = mc.renderEngine.getTexture("assets/retrostorage/gui/request_queue.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        this.list.clear();
        if(network != null){
            list.addAll(network.requestQueue);
        }
        this.slotContainer.drawScreen(x, y, renderPartialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);

    }

}

