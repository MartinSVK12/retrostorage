

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;


public class ContainerDigitalChest extends Container
{

    public ContainerDigitalChest(IInventory iinventory, TileEntityDigitalChest tileentitydigitalchest)
    {
    	
    	//addSlot(new Slot(tileentitydigitalchest, 0, 44, 108));
    	addSlot(new SlotDigital(tileentitydigitalchest, 0, 80, 108));
    	//addSlot(new Slot(tileentitydigitalchest, 2, 116, 108));
    	
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
    	
        tile = tileentitydigitalchest;
        for(int i = 0; i < 4; i++)
        {
            for(int l = 0; l < 9; l++)
            {
        		addSlot(new SlotDigital(tileentitydigitalchest,l + i * 9 + 1, 8 + l * 18, 18 + i * 18));
            }

        }

        

        

    }

	public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    public void withdrawItem(Slot slot) {
    	/*tile.withdrawItem(slot);*/
    }
    
    private TileEntityDigitalChest tile;
}
