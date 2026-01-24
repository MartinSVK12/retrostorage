package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.screen.widget.CraftingProcessListWidget;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

import java.util.ArrayList;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class CraftingProcessScreen extends Screen {

    private CraftingProcessListWidget slotContainer;
    public ArrayList<CraftingProcess.Step> list = new ArrayList<>();
    public CraftingProcess craftingProcess;

    public CraftingProcessScreen(CraftingProcess process) {
        this.craftingProcess = process;
    }

    @Override
    public void init() {
        this.slotContainer = new CraftingProcessListWidget(this.minecraft, this.width, this.height, 72, this.height - 64, 36, this);

        this.slotContainer.registerButtons(this.buttons, 4, 5);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        int xSize = 256;
        int ySize = 256;
        super.render(mouseX, mouseY, delta);
        int bg = this.minecraft.textureManager.getTextureId(gui("request_queue"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        drawTexture(x, y, 0, 0, xSize, ySize);
        drawCenteredTextWithShadow(textRenderer, TranslationStorage.getInstance().getClientTranslation("container.retrostorage.craftingProcess.name"), this.width / 2, 0x14, 0xFFFFFFFF);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 351); //TODO: fix this breaking at lower resolutions than 1080p
        this.slotContainer.render(mouseX, mouseY, delta);
        this.list.clear();
        list.addAll(craftingProcess.steps);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public TextRenderer getFont() {
        return textRenderer;
    }
}
