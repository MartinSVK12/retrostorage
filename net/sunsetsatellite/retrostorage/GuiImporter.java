

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiImporter extends GuiContainer
{

    public GuiImporter(InventoryPlayer inventoryplayer, TileEntityImporter tileentityimporter)
    {
        super(new ContainerImporter(inventoryplayer, tileentityimporter));
        tile = tileentityimporter;
    }

    public void initGui()
    {
        super.initGui();
        controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 60), 20, 20, "-"));
        controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Item Importer", 56, 6, 0x404040);
        fontRenderer.drawString("Slot: "+tile.slot, 70, 30, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/retrostorage/assembly_request.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if(tile.slot >= 0)
            tile.slot--;
        }
        if (guibutton.id == 1) {
            tile.slot++;
        }
    }

    private TileEntityImporter tile;
}
