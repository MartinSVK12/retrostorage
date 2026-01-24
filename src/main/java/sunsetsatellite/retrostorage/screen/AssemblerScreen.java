package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.AssemblerScreenHandler;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class AssemblerScreen extends HandledScreen {
    private final AssemblerBlockEntity tile;

    public AssemblerScreen(PlayerInventory playerInv, AssemblerBlockEntity tile) {
        super(new AssemblerScreenHandler(playerInv, tile));
        this.tile = tile;
    }

    @Override
    protected void drawForeground() {
        super.drawForeground();
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), tile.advanced ? 54 : 64, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("disc_container"));
        if (tile.advanced) {
            bg = this.minecraft.textureManager.getTextureId(gui("disc_container_extended"));
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
