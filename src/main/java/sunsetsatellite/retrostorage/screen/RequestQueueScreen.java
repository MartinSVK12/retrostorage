package sunsetsatellite.retrostorage.screen;


import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.util.ArrayList;

public class RequestQueueScreen extends Screen {
    protected String screenTitle = "Scroll Container";
    private TaskSlotScreen slotContainer;
    public ArrayList<CraftingTask> list = new ArrayList<>();
    public NetworkController network;
    public RequestTerminalScreen parent;

    public RequestQueueScreen(NetworkController network, RequestTerminalScreen parent) {
        super();
        this.parent = parent;
        this.network = network;
    }

    public void init() {
        this.screenTitle = "Request Queue";
        this.slotContainer = new TaskSlotScreen(this.minecraft, this.width, this.height, 72, this.height - 64, 36, this);

        this.slotContainer.registerScrollButtons(this.buttons, 4, 5);
        this.initButtons();
    }

    public void initButtons() {
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (guibutton.active) {

        }
    }


    @Override
    public boolean shouldPause() {
        return false;
    }

    public void render(int x, int y, float renderPartialTicks) {
        int backgroundWidth = 256;
        int backgroundHeight = 256;
        super.render(x, y, renderPartialTicks);
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/request_queue.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        drawCenteredString(this.screenTitle, this.width / 2, 20, 16777215);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 351); //TODO: fix this breaking at lower resolutions than 1080p
        this.list.clear();
        if (network != null) {
            list.addAll(network.getRequestQueue());
            for (CraftingTask task : network.getRequestQueue()) {
                slotContainer.posZ = (36 * (task.nodes.all().size() + 2));
            }
        }
        this.slotContainer.render(x, y, renderPartialTicks);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }


    public void drawCenteredString(String string, int x, int y, int color) {
        int length = textRenderer.getWidth(string);
        textRenderer.drawWithShadow(string, x - length / 2, y, color);
    }

    public void drawString(String string, int x, int y, int color) {
        textRenderer.draw(string, x, y, color);
    }
}

