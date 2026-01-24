package sunsetsatellite.retrostorage.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.AdvInterfaceBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.AdvInterfaceScreenHandler;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class AdvInterfaceScreen extends HandledScreen {
    private final AdvInterfaceBlockEntity tile;

    public AdvInterfaceScreen(PlayerInventory playerInv, AdvInterfaceBlockEntity tile) {
        super(new AdvInterfaceScreenHandler(playerInv, tile));
        this.tile = tile;
    }

    @Override
    protected void drawForeground() {
        super.drawForeground();
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 64, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        if (tile.workingTile != null && tile.workingTile instanceof BlockEntity be) {
            Vec3i pos = new Vec3i(be.x, be.y, be.z);
            textRenderer.draw(tile.workingTile.getClass().getSimpleName().replace("BlockEntity", "") + " at " + pos, 0, -10, 0xFFFFFF);
        }
        if (tile.workingNode != null) {
            textRenderer.draw(tile.workingNode.getState() + " (" + tile.workingNode.getCompletionPercentage() + "%)", 0, -20, 0xFFFFFF);
        }
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("disc_container"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
