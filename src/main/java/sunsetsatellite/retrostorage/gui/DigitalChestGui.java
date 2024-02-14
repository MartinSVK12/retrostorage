package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Inventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.container.DigitalChestContainer;

public class DigitalChestGui extends ContainerScreen {

    public DigitalChestGui(PlayerEntity playerEntity, Inventory inventory) {
        super(new DigitalChestContainer(playerEntity.inventory, inventory));
        backgroundHeight = 220;
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int var2 = this.minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/gui/digital_chest.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(var2);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw("Digital Chest", 8, 6, 0x404040);
        textRenderer.draw("Inventory", 8,  (backgroundHeight - 95) + 2, 0x404040);
    }
}
