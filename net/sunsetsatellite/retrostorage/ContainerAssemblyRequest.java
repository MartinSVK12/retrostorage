

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;


public class ContainerAssemblyRequest extends Container
{

    public ContainerAssemblyRequest(IInventory iinventory, TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        //addSlot(new SlotViewOnly(TileEntityRequestTerminal, 2, 80, 35));
    	
        tile = TileEntityRequestTerminal;

    }

	public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityRequestTerminal tile;
}
