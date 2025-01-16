package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.AssemblerScreenHandler;

public class AssemblerScreen extends HandledScreen {

    public AssemblerScreen(PlayerInventory inventoryplayer, AssemblerBlockEntity TileEntityAssembler) {
        super(new AssemblerScreenHandler(inventoryplayer, TileEntityAssembler));
    }

    protected void drawForeground() {
        textRenderer.draw("Assembler", 64, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/disc_container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }
}
