package net.sunsetsatellite.retrostorage.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.sunsetsatellite.retrostorage.containers.ContainerDigitalTerminal;
import net.sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import net.sunsetsatellite.retrostorage.util.SlotDigital;
import org.lwjgl.opengl.GL11;


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
        fontRenderer.drawString("Digital Terminal", 50, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString((new StringBuilder().append("Page: ").append(tile.page).append("/").append(tile.pages)).toString(), 65, 93, 0x404040);
        if(tile.network != null && tile.network.drive != null){
            int color = 0xFFFFFF;
            if(tile.network.drive.virtualDisc.stackTagCompound.getCompoundTag("disc").getTags().toArray().length >= tile.network.drive.virtualDriveMaxStacks){
                color = 0xFF4040;
            }
            fontRenderer.drawStringWithShadow(tile.network.drive.virtualDisc.stackTagCompound.getCompoundTag("disc").getTags().toArray().length +"/"+tile.network.drive.virtualDriveMaxStacks, 90, 112, color);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int par3) {
        int l = mc.renderEngine.getTexture("/assets/retrostorage/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    public void initGui()
    {
    	super.initGui();
        for(Object slot : inventorySlots.inventorySlots){
            if(slot instanceof SlotDigital){
                ((SlotDigital) slot).variableIndex = ((SlotDigital) slot).slotIndex + (36*(tile.page-1));
                //RetroStorage.LOGGER.info(String.format("V: %d R: %d",((SlotDigital) slot).variableIndex,((SlotDigital) slot).slotIndex));
            }
        }
    	controlList.add(new GuiButton(0, Math.round(width / 2 + 50), Math.round(height / 2 - 5), 20, 20, ">"));
    	controlList.add(new GuiButton(1, Math.round(width / 2 - 70), Math.round(height / 2 - 5), 20, 20, "<"));// /2 - 34, - 150
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
                            ((SlotDigital) slot).variableIndex += 36;
                            //RetroStorage.LOGGER.info(String.format("V: %d R: %d",((SlotDigital) slot).variableIndex,((SlotDigital) slot).slotIndex));
                        }
                    }
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
                            ((SlotDigital) slot).variableIndex -= 36;
                            //RetroStorage.LOGGER.info(String.format("V: %d R: %d",((SlotDigital) slot).variableIndex,((SlotDigital) slot).slotIndex));
                        }
                    }
                }
            }
        }
        //System.out.println(tile.page);
    }
    
    public void updateScreen()
    {
    	
    }

    public void onGuiClosed(){
    }
    
    private TileEntityDigitalTerminal tile;
}
