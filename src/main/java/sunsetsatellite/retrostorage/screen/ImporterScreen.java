package sunsetsatellite.retrostorage.screen;


import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.FluidImporterBlockEntity;
import sunsetsatellite.retrostorage.block.entity.ImporterBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ImporterScreenHandler;

public class ImporterScreen extends HandledScreen {

    public ImporterScreen(PlayerInventory inventoryplayer, ImporterBlockEntity tileEntityImporter) {
        super(new ImporterScreenHandler(inventoryplayer, tileEntityImporter));
        tile = tileEntityImporter;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonWidget(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isWhitelist ? "W" : "B"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawForeground() {
        textRenderer.draw("Item Importer", 56, 6, 0x404040);
        textRenderer.draw("Slot: " + tile.slot, 16, 50, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 0) {
            if (tile.slot >= 0) {
                tile.slot--;
            }
        }
        if (guibutton.id == 1) {
            tile.slot++;
        }
        if (guibutton.id == 2) {
            tile.isWhitelist = !tile.isWhitelist;
            guibutton.text = tile.isWhitelist ? "W" : "B";
        }
    }

    ImporterBlockEntity tile;
}
