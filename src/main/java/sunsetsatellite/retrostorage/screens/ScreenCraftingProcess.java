package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

import java.util.ArrayList;

public class ScreenCraftingProcess extends Screen {
    protected String screenTitle = "Scroll Container";
    private CraftingProcessStepElement slotContainer;
    public ArrayList<CraftingProcess.Step> list = new ArrayList<>();
    public CraftingProcess craftingProcess;

    public ScreenCraftingProcess(CraftingProcess process) {
        super(null);
        this.craftingProcess = process;
    }

    public void init() {
        I18n stringtranslate = I18n.getInstance();
        this.screenTitle = "Crafting Process";
        this.slotContainer = new CraftingProcessStepElement(this.mc, this.width, this.height, 72, this.height - 64, 36, this);

        this.slotContainer.registerScrollButtons(this.buttons, 4, 5);
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
    public void render(int x, int y, float renderPartialTicks) {
        int xSize = 256;
        int ySize = 256;
        super.render(x, y, renderPartialTicks);
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/request_queue.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        this.drawStringCentered(this.font, this.screenTitle, this.width / 2, 20, 16777215);
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

    public Font getFont(){
        return font;
    }

}

