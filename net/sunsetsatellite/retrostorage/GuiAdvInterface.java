

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;


public class GuiAdvInterface extends GuiContainer
{

    public GuiAdvInterface(InventoryPlayer inventoryplayer, TileEntityAdvInterface TileEntityAdvInterface)
    {
        super(new ContainerAdvInterface(inventoryplayer, TileEntityAdvInterface));
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Adv. Interface", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
