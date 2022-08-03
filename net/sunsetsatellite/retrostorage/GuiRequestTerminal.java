// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerDispenser, FontRenderer, RenderEngine, 
//            InventoryPlayer, TileEntityDispenser

public class GuiRequestTerminal extends GuiContainer
{

    public GuiRequestTerminal(InventoryPlayer inventoryplayer, TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        super(new ContainerRequestTerminal(inventoryplayer, TileEntityRequestTerminal));
        tile = TileEntityRequestTerminal;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Request Terminal", 45, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(tile.page+1).append("/").append(tile.pages+1)).toString(), 65, 93, 0x404040);
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));
        controlList.add(new GuiButton(2, Math.round(width / 2 - 35), Math.round(height / 2 - 5), 20, 20, "Q"));// /2 - 34, - 150
        controlList.add(new GuiButton(3, Math.round(width / 2 + 15), Math.round(height / 2 - 5), 20, 20, "X"));
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
        	if(tile.page<tile.pages)
        	tile.page++;
        }
        if(guibutton.id == 1)
        {
        	if(tile.page > 0)
        	tile.page--;
        }
        if(guibutton.id == 2){
            if(tile.controller != null){
                EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
                System.out.println("Assembly queue: "+tile.controller.assemblyQueue.toString());
                entityplayer.addChatMessage("Assembly queue: "+tile.controller.assemblyQueue.toString());
            }
        }
        if(guibutton.id == 3){
            if(tile.controller != null) {
                EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
                tile.controller.assemblyQueue.clear();
                entityplayer.addChatMessage("Assembly queue cleared!");
            }
        }
        //System.out.println(tile.page);
    }
    
    public void updateScreen()
    {
    	
    }
    
    private TileEntityRequestTerminal tile;
}
