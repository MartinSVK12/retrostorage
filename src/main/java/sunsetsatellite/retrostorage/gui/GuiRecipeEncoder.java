

package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.containers.ContainerRecipeEncoder;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;

public class GuiRecipeEncoder extends GuiContainer
{

    /*public GuiRecipeEncoder(InventoryPlayer inventoryplayer, World world, int i, int j, int k)
    {
        super(new ContainerWorkbench(inventoryplayer, world, i, j, k));
    }*/

    public GuiRecipeEncoder(InventoryPlayer inventoryplayer, TileEntityRecipeEncoder tileEntityRecipeEncoder) {
    	super(new ContainerRecipeEncoder(inventoryplayer, tileEntityRecipeEncoder));
    	tile = tileEntityRecipeEncoder;
		
	}

	public void onGuiClosed()
    {
        super.onGuiClosed();
        inventorySlots.onCraftGuiClosed(mc.thePlayer);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Recipe Encoder", 28, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }
    
    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 15), Math.round(height / 2 - 25), 60, 20, "Encode"));
    }
    
    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 0)
        {
        	tile.encodeDisc();
        }
        //System.out.println(tile.page);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/gui/recipe_encoder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
    
    private TileEntityRecipeEncoder tile;
}
