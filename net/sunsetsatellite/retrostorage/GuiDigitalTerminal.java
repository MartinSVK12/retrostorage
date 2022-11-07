// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Slot;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerDispenser, FontRenderer, RenderEngine, 
//            InventoryPlayer, TileEntityDispenser

public class GuiDigitalTerminal extends GuiContainer
{

    public GuiDigitalTerminal(InventoryPlayer inventoryplayer, TileEntityDigitalTerminal tileentitydigitalterminal)
    {
        super(new ContainerDigitalTerminal(inventoryplayer, tileentitydigitalterminal));
        ySize = 220;
        tile = tileentitydigitalterminal;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Digital Terminal", 55, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(tile.page+1).append("/").append(tile.pages+1)).toString(), 65, 93, 0x404040);
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
    }
    
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/retrostorage/digital_terminal.png");
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
            if(tile.controller != null){
                if(tile.page<tile.pages) {
                    tile.page++;
                    for(Object slot : inventorySlots.slots){
                        if(slot instanceof SlotDigital){
                            ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).slotNumber + ((tile.page-1) * 37) + 1;
                        }
                    }
                    //DiscManipulator.saveDisc(tile.controller.network_disc, tile, tile.page);

                    //DiscManipulator.clearDigitalInv(tile);
                    //DiscManipulator.loadDisc(tile.controller.network_disc, tile, tile.page);
                }
            }
        }
        if(guibutton.id == 1)
        {
            if(tile.controller != null) {
                if (tile.page > 0) {
                    tile.page--;
                    for(Object slot : inventorySlots.slots){
                        if(slot instanceof SlotDigital){
                            ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).slotNumber + ((tile.page-1) * 37) + 1;
                        }
                    }
                    //DiscManipulator.saveDisc(tile.controller.network_disc, tile, tile.page);

                    //DiscManipulator.clearDigitalInv(tile);
                    //DiscManipulator.loadDisc(tile.controller.network_disc, tile, tile.page);
                }
            }
        }
        //System.out.println(tile.page);
    }
    
    public void updateScreen()
    {
    	
    }

    public void onGuiClosed(){
        if(tile.getStackInSlot(0) != null){
            DiscManipulator.saveDisc(tile.getStackInSlot(0), tile, tile.page);
        }
    }
    
    private TileEntityDigitalTerminal tile;
}
