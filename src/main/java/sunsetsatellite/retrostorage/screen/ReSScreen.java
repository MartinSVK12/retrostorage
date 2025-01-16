package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

public abstract class ReSScreen extends HandledScreen {

    public final RenderDigitalItem renderDigitalItem;

    public ReSScreen(ScreenHandler container) {
        super(container);
        renderDigitalItem = new RenderDigitalItem(Minecraft.INSTANCE);
    }

    public void drawCenteredString(String string, int x, int y, int color) {
        int length = textRenderer.getWidth(string);
        textRenderer.drawWithShadow(string, x - length / 2, y, color);
    }
}
