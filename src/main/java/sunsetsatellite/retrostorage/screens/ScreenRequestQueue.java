package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;

import org.lwjgl.opengl.GL41;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.api.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.util.ArrayList;

public class ScreenRequestQueue extends Screen {
    protected String screenTitle = "Scroll Container";
    private TaskSlotElement slotContainer;
    public ArrayList<CraftingTask> list = new ArrayList<>();
    public INetworkController network;
    public ScreenRequestTerminal parent;
    public TickTimer refreshQueueTimer = new TickTimer(this, this::refreshList, 20, true);

    public ScreenRequestQueue(INetworkController network, ScreenRequestTerminal parent) {
        super(null);
        this.parent = parent;
        this.network = network;
    }

    public void init() {
        I18n stringtranslate = I18n.getInstance();
        this.screenTitle = "Request Queue";
        this.slotContainer = new TaskSlotElement(this.mc, this.width, this.height, 72, this.height - 64, 36, this);
        this.initButtons();
    }

    public void initButtons() {
        I18n stringtranslate = I18n.getInstance();
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (guibutton.enabled) {

        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(int mx, int my, float partialTick) {
        int xSize = 256;
        int ySize = 256;
        super.render(mx, my, partialTick);
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/request_queue.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        drawStringCenteredShadow(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xffffff);
        GLRenderer.enableState(State.SCISSOR_TEST);
        GL41.glScissor(140, this.height - 175, this.width * 2, this.height + 351); //TODO: fix this breaking at lower resolutions than 1080p
        refreshQueueTimer.tick();
        this.slotContainer.render(mx, my, partialTick);

		GLRenderer.disableState(State.SCISSOR_TEST);
    }

    private void refreshList() {
        this.list.clear();
        if (network != null) {
            list.addAll(network.getRequestQueue());
            for (CraftingTask task : network.getRequestQueue()) {
                slotContainer.itemHeight = (36 * (task.nodes.all().size() + 2));
            }
        }
    }

    public FontRenderer getFont(){
        return this.fontRenderer;
    }

}

