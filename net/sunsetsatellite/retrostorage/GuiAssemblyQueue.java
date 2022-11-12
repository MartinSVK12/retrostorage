package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiAssemblyQueue extends GuiContainer
{

    public GuiAssemblyQueue(InventoryPlayer inventoryplayer, TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        super(new ContainerAssemblyQueue(inventoryplayer, TileEntityRequestTerminal));
        tile = TileEntityRequestTerminal;
        ySize = 220;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Assembly Queue", 47, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(page+1).append("/").append(pages+1)).toString(), 65, 93, 0x404040);
        int j = page*36;
        if(tile.controller != null){
            List<Object> q = Arrays.asList(tile.controller.assemblyQueue.stream().toArray());
            pages = q.size()/36;
            if(q.size() > 0){
                for(int i = 0; i < 4; i++)
                {
                    for(int l = 0; l < 9; l++)
                    {
                        j++;
                        if(q.size() > j){
                            drawItemStack((ItemStack)q.get(j), 8 + l * 18, 18 + i * 18);
                        }
                        //addSlot(new SlotViewOnly(TileEntityRequestTerminal,l + i * 9 + 3 , 8 + l * 18, 18 + i * 18));
                    }

                }
            }
        }
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));
        controlList.add(new GuiButton(2, Math.round(width / 2 - 35), Math.round(height / 2 - 5), 20, 20, "R"));// /2 - 34, - 150
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
        	if(page<pages)
        	page++;
        }
        if(guibutton.id == 1)
        {
        	if(page > 0)
        	page--;
        }
        if(guibutton.id == 2){
            if(tile.controller != null){
                EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
                ModLoader.OpenGUI(entityplayer, new GuiRequestTerminal(entityplayer.inventory, tile));
            }
        }
        if(guibutton.id == 3){
            if(tile.controller != null) {
                for (Map.Entry<ArrayList<Integer>, HashMap<String, Object>> entry : tile.controller.network.entrySet()) {
                    //loop over tile entities in network
                    TileEntity tile = (TileEntity) entry.getValue().values().toArray()[0];
                    if(tile instanceof TileEntityInterface){
                        ((TileEntityInterface) tile).cancelProcessing();
                    }
                }

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
    private int page = 0;
    private int pages = 0;
}
