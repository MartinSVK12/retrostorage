package sunsetsatellite.retrostorage.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.containers.ContainerDigitalTerminal;
import sunsetsatellite.retrostorage.containers.ContainerRequestTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.SlotDigital;


public class GuiRequestTerminal extends GuiContainer
{

    public GuiRequestTerminal(EntityPlayer player, TileEntityRequestTerminal tile)
    {
        super(new ContainerRequestTerminal(player.inventory, tile));
        ySize = 220;
        this.tile = tile;
        this.player = player;
    }

	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Request Terminal", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(tile.page).append("/").append(tile.pages)).toString(), 65, 93, 0x404040);
        if(tile.network != null && tile.network.drive != null){
            fontRenderer.drawString(tile.network.drive.virtualDisc.tag.getCompoundTag("disc").func_28110_c().toArray().length +"/"+tile.network.drive.virtualDriveMaxStacks, 80, 112, 0x404040);
        }
    }

    public void initGui()
    {
    	super.initGui();
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
        controlList.add(new GuiButton(2, Math.round(width / 2 - 50), Math.round(height / 2 - 5), 20, 20, "Q"));
        controlList.add(new GuiButton(3, Math.round(width / 2 + 30), Math.round(height / 2 - 5), 20, 20, "X"));
    }
    
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/retrostorage/gui/digital_terminal.png");
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
            if(tile.network != null){
                if(tile.page<tile.pages) {
                    tile.page++;
                    for(Object slot : inventorySlots.inventorySlots){
                        if(slot instanceof SlotDigital){
                            ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).slotIndex + ((tile.page-1) * 37) + 1;
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
            if(tile.network != null) {
                if (tile.page > 1) {
                    tile.page--;
                    for(Object slot : inventorySlots.inventorySlots){
                        if(slot instanceof SlotDigital){
                            ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).slotIndex + ((tile.page-1) * 37) + 1;
                        }
                    }
                    //DiscManipulator.saveDisc(tile.controller.network_disc, tile, tile.page);

                    //DiscManipulator.clearDigitalInv(tile);
                    //DiscManipulator.loadDisc(tile.controller.network_disc, tile, tile.page);
                }
            }
        }
        if(guibutton.id == 2){
            ((IOpenGUI)player).displayGUI(new GuiRequestQueue(tile.network, this));
        }
        if(guibutton.id == 3){
            if(tile.network != null) {
                tile.network.clearRequestQueue();
                player.addChatMessage("action.retrostorage.clearTaskQueue");
            }
        }
        //System.out.println(tile.page);
    }
    
    public void updateScreen()
    {
    	
    }

    public void onGuiClosed(){
        /*if(tile.getStackInSlot(0) != null){
            DiscManipulator.saveDisc(tile.getStackInSlot(0), tile, tile.page);
        }*/
    }
    
    public TileEntityRequestTerminal tile;
    public EntityPlayer player;
}
