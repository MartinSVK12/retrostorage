package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.player.inventory.InventoryPlayer;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

public class GuiDigitalController extends GuiScreen {

    public GuiDigitalController(InventoryPlayer inventoryPlayer, TileEntityDigitalController tile) {
        super();
        this.tile = tile;
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x, y, renderPartialTicks);
        int i = 64;
        fontRenderer.drawString("Digital Controller", 2, i, 0xFFFFFFFF);
        fontRenderer.drawString("Press ESC to exit.", 2, i += 10, 0xFFFFFFFF);
        if (tile.network != null) {
            if (tile.externalEnergy != null) {
                fontRenderer.drawString("External energy source connected.", 2, i += 10, 0xFFFFFFFF);
            } else {
                fontRenderer.drawString(String.format("Network energy: %d", Math.round(tile.energy)), 2, i += 10, 0xFFFFFFFF);
            }
            i += 10;
            fontRenderer.drawString(
                    String.format("Network: %s", tile.network
                    ), 2, i += 10, 0xFFFFFFFF);
            i += 10;
            fontRenderer.drawString(
                    String.format("%d storage devices connected.", tile.getAttachedStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(
                    String.format("%d processors connected.", tile.getProcessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(
                    String.format("%d co-processors connected.", tile.getCoprocessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            /*if (tile.externalEnergy == null) {
                if (tile.active && tile.energy > 0) {
                    int cableSize = tile.network.searchAll(TileEntityNetworkCable.class).size();
                    fontRenderer.drawString(String.format("Usage: %d (%ds left)", (tile.network.devicesSize() - cableSize) + 1, Math.round(
                            (tile.energy / ((tile.network.devicesSize() - cableSize) + 1)) / 20
                    )), 2, i, 0xFFFFFFFF);
                }
            } else {
                if (tile.active && tile.energy > 0) {
                    int cableSize = tile.network.searchAll(TileEntityNetworkCable.class).size();
                    fontRenderer.drawString(String.format("Usage: %d (%ds left)", (tile.network.devicesSize() - cableSize) + 1, Math.round(
                            ((float) tile.externalEnergy.getEnergy() / ((tile.network.devicesSize() - cableSize) + 1)) / 20
                    )), 2, i, 0xFFFFFFFF);
                }
            }


            i += 10;
            if (tile.network.drive != null) {
                fontRenderer.drawString(String.format("Drive detected: %s", tile.network.drive.toStringFormatted()), 2, i, 0xFFFFFFFF);
            }
            i += 10;
            if (tile.network.fluidDrive != null) {
                fontRenderer.drawString(String.format("Fluid drive detected: %s", tile.network.fluidDrive.toStringFormatted()), 2, i, 0xFFFFFFFF);
            }
            fontRenderer.drawString(String.format("Assemblers: %d", tile.network.getAssemblers().size()), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(String.format("Interfaces: %d", tile.network.getAdvInterfaces().size()), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(String.format("Coprocessors: %d", tile.network.getCoprocessors().size()), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(String.format("Available craftables: %d", tile.network.knownCraftables.size()), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(String.format("Current active tasks: %d / %d", tile.network.currentTasks.size(), tile.network.getCoprocessors().size() + 1), 2, i += 10, 0xFFFFFFFF);
            fontRenderer.drawString(String.format("Request queue size: %d", tile.network.requestQueue.size()), 2, i += 10, 0xFFFFFFFF);*/
        }
    }

    @Override
    public boolean pausesGame() {
        return false;
    }

    private final TileEntityDigitalController tile;
}
