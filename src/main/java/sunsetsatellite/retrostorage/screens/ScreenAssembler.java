package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.menus.MenuAssembler;
import sunsetsatellite.retrostorage.tiles.TileEntityAssembler;

public class ScreenAssembler extends ScreenContainerAbstract {

    public ScreenAssembler(ContainerInventory inventoryplayer, TileEntityAssembler TileEntityAssembler) {
        super(new MenuAssembler(inventoryplayer, TileEntityAssembler));
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Assembler", 64, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/disc_container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
