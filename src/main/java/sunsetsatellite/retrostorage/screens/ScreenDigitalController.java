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
        drawStringNoShadow(fontRenderer,"Digital Controller", 2, i, 0xFFFFFFFF);
        drawStringNoShadow(fontRenderer,"Press ESC to exit.", 2, i += 10, 0xFFFFFFFF);
        if (tile.network != null) {
            if(tile.active){
                if (tile.externalEnergy != null) {
                    drawStringNoShadow(fontRenderer,"External energy source connected.", 2, i += 10, 0xFFFFFFFF);
                    drawStringNoShadow(fontRenderer,String.format("Using %d E/t.",tile.getEnergyConsumption()), 2, i += 10, 0xFFFFFFFF);
                } else {
                    drawStringNoShadow(fontRenderer,String.format("Network energy: %d", Math.round(tile.energy)), 2, i += 10, 0xFFFFFFFF);
                    drawStringNoShadow(fontRenderer,String.format("Using %d E/t (%s remain.)",tile.getEnergyConsumption(), NumberUtil.formatTime((float)tile.energy/(tile.getEnergyConsumption()*20))), 2, i += 10, 0xFFFFFFFF);
                }
            } else {
                drawStringNoShadow(fontRenderer,"Network out of energy!", 2, i += 10, 0xFFFFFFFF);
            }
            i += 10;
            drawStringNoShadow(fontRenderer,
                    String.format("Network: %s", tile.network
                    ), 2, i += 10, 0xFFFFFFFF);
            i += 10;
            drawStringNoShadow(fontRenderer,
                    String.format("%d storage devices connected.", tile.getAttachedStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            drawStringNoShadow(fontRenderer,
                    String.format("%d fluid storage devices connected.", tile.getAttachedFluidStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            drawStringNoShadow(fontRenderer,
                    String.format("%d processors connected.", tile.getProcessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            drawStringNoShadow(fontRenderer,
                    String.format("%d coprocessors connected.", tile.getCoprocessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final TileEntityDigitalController tile;
}
