

package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;
import net.sunsetsatellite.retrostorage.containers.ContainerRecipeEncoder;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import org.lwjgl.opengl.GL11;

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
        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                if (tile.useMeta.get(l * 3 + k1)) {
                    fontRenderer.drawString("M", 30 + k1 * 18, 17 + l * 18, 0x00FF00);
                } else {
                    fontRenderer.drawString("!M", 30 + k1 * 18, 17 + l * 18, 0xFF0000);
                }
            }

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/recipe_encoder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 15), Math.round(height / 2 - 25), 60, 20, "Encode"));
        //controlList.add(new GuiButton(1, Math.round(width / 2 + 60) , Math.round(height / 2) - 75, 20, 20, tile.useMeta ? "M" : "!M"));

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
        /*if(guibutton.id == 1){
            tile.useMeta = !tile.useMeta;
            guibutton.displayString = tile.useMeta ? "M" : "!M";
        }*/
        //System.out.println(tile.page);
    }

    private TileEntityRecipeEncoder tile;
}
