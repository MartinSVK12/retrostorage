package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;

import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.menus.MenuFluidImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;

public class ScreenFluidImporter extends ScreenFluidFake {

    public ScreenFluidImporter(ContainerInventory inventoryplayer, TileEntityFluidImporter tileEntityImporter) {
        super(inventoryplayer, new MenuFluidImporter(inventoryplayer, tileEntityImporter));
        tile = tileEntityImporter;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isWhitelist ? "W" : "B"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Fluid Importer", 56, 6, 0x404040);
        font.drawString("Slot: " + tile.slot, 16, 50, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
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
            guibutton.displayString = tile.isWhitelist ? "W" : "B";
        }
    }

    TileEntityFluidImporter tile;
}
