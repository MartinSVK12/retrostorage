package sunsetsatellite.retrostorage.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import sunsetsatellite.catalyst.core.util.NumberFormatter;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

public class DigitalControllerScreen extends Screen {

    private final DigitalControllerBlockEntity tile;

    public DigitalControllerScreen(PlayerInventory playerInv, DigitalControllerBlockEntity tile) {
        this.tile = tile;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        renderBackground();
        int i = 64;
        textRenderer.draw("Digital Controller", 2, i, 0xFFFFFFFF);
        textRenderer.draw("Press ESC to exit.", 2, i += 10, 0xFFFFFFFF);
        if (tile.network != null || tile.world.isRemote) {
            if (tile.active) {
                if (tile.externalEnergy != null) {
                    textRenderer.draw("External energy source connected.", 2, i += 10, 0xFFFFFFFF);
                    if (tile.externalEnergy instanceof BlockEntity be) {
                        textRenderer.draw(String.format("%s at %s", be.getClass().getSimpleName().replace("BlockEntity", ""), new Vec3i(be.x, be.y, be.z)), 2, i += 10, 0xFFFFFFFF);
                    }
                    textRenderer.draw(String.format("Using %d E/t.", tile.getEnergyConsumption()), 2, i += 10, 0xFFFFFFFF);
                } else {
                    textRenderer.draw(String.format("Network energy: %d", Math.round(tile.energy)), 2, i += 10, 0xFFFFFFFF);
                    textRenderer.draw(String.format("Using %d E/t (%s remain.)", tile.getEnergyConsumption(), NumberFormatter.formatTime((float) tile.energy / (tile.getEnergyConsumption() * 20))), 2, i += 10, 0xFFFFFFFF);
                }
            } else {
                textRenderer.draw("Network out of energy!", 2, i += 10, 0xFFFFFFFF);
            }
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
                    String.format("%d coprocessors connected.", tile.getCoprocessors().size()
                    ), 2, i += 10, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
