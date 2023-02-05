

package sunsetsatellite.retrostorage.containers;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.SlotDigital;
import sunsetsatellite.retrostorage.util.SlotViewOnly;


public class ContainerDigitalTerminal extends Container
{

    public ContainerDigitalTerminal(IInventory iinventory, TileEntityDigitalTerminal tileentitydigitalterminal)
    {

    	addSlot(new SlotViewOnly(tileentitydigitalterminal, 0, 60, 108));
    	
    	for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 198));
        }
    	
    	for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

        }
    	
        tile = tileentitydigitalterminal;
        for(int i = 0; i < 4; i++)
        {
            for(int l = 0; l < 9; l++)
            {
        		addSlot(new SlotDigital(tileentitydigitalterminal,l + i * 9 + 1 , 8 + l * 18, 18 + i * 18));
            }

        }

        

        

    }

    @Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean shift, boolean ctrl) {
        if(tile.network != null){
            //RetroStorage.LOGGER.info(String.format("i:%d player:%s, bool1:%s, bool2:%s",i,entityPlayer,shift,ctrl));
            ItemStack item = this.getSlot(i).getStack().copy();
            ItemStack original = this.getSlot(i).getStack();
            if(i > 0 && i < 37){
                if(tile.getAmountOfUsedSlots() < tile.network.drive.virtualDriveMaxStacks) {
                    this.onStackMergeShiftClick(this.getSlot(i).getStack(), 37, 73, false);
                }
            } else {
                this.onStackMergeShiftClick(this.getSlot(i).getStack(),1,36,false);
                this.getSlot(i).onPickupFromSlot(item);
            }
            if (original.stackSize == 0) {
                this.getSlot(i).putStack(null);
            } else {
                this.getSlot(i).onSlotChanged();
            }
        }
    }

    @Override
    public ItemStack clickInventorySlot(int slotID, int button, boolean shift, boolean control, EntityPlayer player) {
        Slot slot = this.getSlot(slotID);
        if(slot instanceof SlotViewOnly){
            return null;
        }
        return super.clickInventorySlot(slotID, button, shift, control, player);
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }
    
    private TileEntityDigitalTerminal tile;
}
