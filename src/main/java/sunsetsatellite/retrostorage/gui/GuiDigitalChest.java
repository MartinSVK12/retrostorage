package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.containers.ContainerDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;

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
        int color = 0xFFFFFF;
        if(tile.page >= tile.pages){
            color = 0xFF4040;
        }
        fontRenderer.drawCenteredString((new StringBuilder().append(tile.page).append("/").append(tile.pages)).toString(), 87, 93, color);
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
            i = mc.renderEngine.getTexture("/assets/retrostorage/gui/digital_chest.png");
        } else {
            i = mc.renderEngine.getTexture("/assets/retrostorage/gui/digital_chest_unavailable.png");
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
            if(tile.page > 1){
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
