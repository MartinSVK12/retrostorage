package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.screen.widget.TaskListWidget;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.util.ArrayList;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class RequestQueueScreen extends Screen {
    public final String guiId = "container.retrostorage.requestQueue";
    private TaskListWidget taskList;
    public ArrayList<CraftingTask> tasks = new ArrayList<>();
    public NetworkController controller;
    public RequestTerminalScreen parent;
    public TickTimer refreshQueueTimer = new TickTimer(this, this::refreshList, 20, true);

    public RequestQueueScreen(NetworkController controller, RequestTerminalScreen parent) {
        this.parent = parent;
        this.controller = controller;
    }

    @Override
    public void init() {
        super.init();
        taskList = new TaskListWidget(minecraft, width, height, 72, height - 64, 36, this);
        taskList.registerButtons(buttons, 4, 5);
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
        drawCenteredTextWithShadow(textRenderer, TranslationStorage.getInstance().getClientTranslation(guiId), this.width / 2, 0x14, 0xFFFFFFFF);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(140, this.height - 175, this.width * 2, this.height + 351); //TODO: fix this breaking at lower resolutions than 1080p
        refreshQueueTimer.tick();
        this.taskList.render(mouseX, mouseY, delta);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void refreshList() {
        tasks.clear();
        if (controller != null) {
            tasks.addAll(controller.getRequestQueue());
            for (CraftingTask task : controller.getRequestQueue()) {
                taskList.itemHeight = (36 * (task.nodes.all().size() + 2));
            }
        }
    }

    public TextRenderer getFont() {
        return this.textRenderer;
    }
}
