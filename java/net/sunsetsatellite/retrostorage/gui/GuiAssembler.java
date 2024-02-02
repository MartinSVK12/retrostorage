

package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.sunsetsatellite.retrostorage.containers.ContainerAssembler;
import net.sunsetsatellite.retrostorage.tiles.TileEntityAssembler;
import org.lwjgl.opengl.GL11;

public class GuiAssembler extends GuiContainer
{

    public GuiAssembler(InventoryPlayer inventoryplayer, TileEntityAssembler TileEntityAssembler)
    {
        super(new ContainerAssembler(inventoryplayer, TileEntityAssembler));
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Assembler", 64, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/disc_container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
