package sunsetsatellite.retrostorage.screen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

public class DigitalControllerScreen extends Screen {

    public DigitalControllerScreen(PlayerInventory inventoryPlayer, DigitalControllerBlockEntity tile) {
        super();
        this.tile = tile;
    }

    @Override
    public void render(int x, int y, float renderPartialTicks) {
        super.render(x, y, renderPartialTicks);
        int i = 64;
        textRenderer.draw("Digital Controller", 2, i, 0xFFFFFFFF);
        textRenderer.draw("Press ESC to exit.", 2, i += 10, 0xFFFFFFFF);
        if (tile.network != null) {
            /*if (tile.externalEnergy != null) {
                textRenderer.draw("External energy source connected.", 2, i += 10, 0xFFFFFFFF);
            } else {
                textRenderer.draw(String.format("Network energy: %d", Math.round(tile.energy)), 2, i += 10, 0xFFFFFFFF);
            }*/
            textRenderer.draw(
                    String.format("Active: %s", tile.active
                    ), 2, i += 10, 0xFFFFFFFF);
            i += 10;
            textRenderer.draw(
                    String.format("Network: %s", tile.network
                    ), 2, i += 10, 0xFFFFFFFF);
            i += 10;
            textRenderer.draw(
                    String.format("%d storage devices connected.", tile.getAttachedStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            textRenderer.draw(
                    String.format("%d fluid storage devices connected.", tile.getAttachedFluidStorage().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            textRenderer.draw(
                    String.format("%d processors connected.", tile.getProcessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
            textRenderer.draw(
                    String.format("%d co-processors connected.", tile.getCoprocessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private final DigitalControllerBlockEntity tile;
}
