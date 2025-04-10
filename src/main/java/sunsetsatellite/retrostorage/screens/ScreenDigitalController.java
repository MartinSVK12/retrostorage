package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.Screen;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

public class ScreenDigitalController extends Screen {

    public ScreenDigitalController(ContainerInventory inventoryPlayer, TileEntityDigitalController tile) {
        super();
        this.tile = tile;
    }

    @Override
    public void render(int x, int y, float renderPartialTicks) {
        super.render(x, y, renderPartialTicks);
        renderBackground();
        int i = 64;
        font.drawString("Digital Controller", 2, i, 0xFFFFFFFF);
        font.drawString("Press ESC to exit.", 2, i += 10, 0xFFFFFFFF);
        if (tile.network != null) {
            if(tile.active){
                if (tile.externalEnergy != null) {
                    font.drawString("External energy source connected.", 2, i += 10, 0xFFFFFFFF);
                    font.drawString(String.format("Using %d E/t.",tile.getEnergyConsumption()), 2, i += 10, 0xFFFFFFFF);
                } else {
                    font.drawString(String.format("Network energy: %d", Math.round(tile.energy)), 2, i += 10, 0xFFFFFFFF);
                    font.drawString(String.format("Using %d E/t (%s remain.)",tile.getEnergyConsumption(), NumberUtil.formatTime((float)tile.energy/(tile.getEnergyConsumption()*20))), 2, i += 10, 0xFFFFFFFF);
                }
            } else {
                font.drawString("Network out of energy!", 2, i += 10, 0xFFFFFFFF);
            }
            i += 10;
            font.drawString(
                    String.format("Network: %s", tile.network
                    ), 2, i += 10, 0xFFFFFFFF);
            i += 10;
            font.drawString(
                    String.format("%d storage devices connected.", tile.getAttachedStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            font.drawString(
                    String.format("%d fluid storage devices connected.", tile.getAttachedFluidStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            font.drawString(
                    String.format("%d processors connected.", tile.getProcessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            font.drawString(
                    String.format("%d co-processors connected.", tile.getCoprocessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final TileEntityDigitalController tile;
}
