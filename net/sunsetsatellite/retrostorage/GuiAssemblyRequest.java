// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerDispenser, FontRenderer, RenderEngine, 
//            InventoryPlayer, TileEntityDispenser

public class GuiAssemblyRequest extends GuiContainer
{

    public GuiAssemblyRequest(InventoryPlayer inventoryplayer, TileEntityRequestTerminal TileEntityRequestTerminal, ItemStack requestStack)
    {
        super(new ContainerAssemblyRequest(inventoryplayer, TileEntityRequestTerminal));
        tile = TileEntityRequestTerminal;
        request = requestStack;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Assembly Request", 45, 6, 0x404040);
        fontRenderer.drawString(count + "x " /*+ StringTranslate.getInstance().translateNamedKey(request.getItemName())*/, 10, 34, 0x404040);
        fontRenderer.drawString(request.stackSize+"x "+StringTranslate.getInstance().translateNamedKey(request.getItemName()), 55, 34, 0x404040);
        drawItemStack(request,30,30);
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 50), 20, 20, "-"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 50), 20, 20, "+"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 30), Math.round(height / 2 - 50), 60, 20, "Request"));
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
    
    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id == 0)
        {
        	if(count > 1){
                count--;
            }
        }
        if(guibutton.id == 1)
        {
            if(count < 99){
                count++;
            }
        }
        if(guibutton.id == 2){
            EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
            DiscManipulator.addCraftRequest(request,count,tile.controller);
            /*entityplayer.addChatMessage("Requesting: "+count+"x "+StringTranslate.getInstance().translateNamedKey(request.getItemName()));
            for(int i = 0;i<count;i++){
                tile.controller.assemblyQueue.add(request.getItem());
            }*/
            ModLoader.OpenGUI(entityplayer, new GuiRequestTerminal(entityplayer.inventory, tile));
        }
        //System.out.println(tile.page);*/
    }
    
    public void updateScreen()
    {
    	
    }
    
    private TileEntityRequestTerminal tile;
    private ItemStack request;
    private int count = 1;
}
