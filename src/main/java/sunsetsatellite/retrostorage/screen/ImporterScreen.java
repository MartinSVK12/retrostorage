package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.ScreenActionPacket;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.ImporterBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ImporterScreenHandler;

public class ImporterScreen extends FilterScreen {
    private final ImporterBlockEntity tile;

    public ImporterScreen(PlayerInventory playerInv, ImporterBlockEntity tile) {
        super(new ImporterScreenHandler(playerInv, tile));
        this.tile = tile;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonWidget(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isWhitelist ? "W" : "B"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    @Override
    protected void drawForeground() {
        super.drawForeground();
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 56, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        textRenderer.draw("Slot: " + tile.slot, 16, 50, 0x404040);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (!button.active) {
            return;
        }
        if (button.id == 0) {
            if (tile.slot >= 0) {
                tile.slot--;
            }
        }
        if (button.id == 1) {
            tile.slot++;
        }
        if (button.id == 2) {
            tile.isWhitelist = !tile.isWhitelist;
            button.text = tile.isWhitelist ? "W" : "B";
        }

        if (tile.world.isRemote) {
            PacketHelper.send(new ScreenActionPacket(button.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z)));
        }
    }
}
