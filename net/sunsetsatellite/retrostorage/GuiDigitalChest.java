

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiDigitalChest extends GuiContainer
{

    public GuiDigitalChest(InventoryPlayer inventoryplayer, TileEntityDigitalChest tileentitydigitalchest)
    {
        super(new ContainerDigitalChest(inventoryplayer, tileentitydigitalchest));
        ySize = 220;
        tile = tileentitydigitalchest;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Digital Chest", 60, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(tile.page+1).append("/").append(tile.pages+1)).toString(), 65, 93, 0x404040);
    }

    public void initGui()
    {
    	super.initGui();
//    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
//    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
    }
    
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i;
        if(tile.getStackInSlot(0) != null){
            i = mc.renderEngine.getTexture("/retrostorage/digital_chest.png");
        } else {
            i = mc.renderEngine.getTexture("/retrostorage/digital_chest_unavailable.png");
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 0)
        {
            if(tile.page<tile.pages) {
                DiscManipulator.saveDisc(tile.getStackInSlot(0), tile, tile.page);
                tile.page++;
                DiscManipulator.clearDigitalInv(tile);
                DiscManipulator.loadDisc(tile.getStackInSlot(0), tile, tile.page);
            }
        }
        if(guibutton.id == 1)
        {
            if(tile.page > 0){
                DiscManipulator.saveDisc(tile.getStackInSlot(0), tile, tile.page);
                tile.page--;
                DiscManipulator.clearDigitalInv(tile);
                DiscManipulator.loadDisc(tile.getStackInSlot(0),tile,tile.page);
            }
        }
    }
    
    public void updateScreen()
    {
    	
    }

    public void onGuiClosed(){
        if(tile.getStackInSlot(0) != null){
            DiscManipulator.saveDisc(tile.getStackInSlot(0), tile, tile.page);
        }
    }
    
    private TileEntityDigitalChest tile;
}
