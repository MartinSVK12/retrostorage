

package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.tiles.TileEntityAssembler;


public class ContainerAssembler extends Container
{

    public ContainerAssembler(IInventory iinventory, TileEntityAssembler TileEntityAssembler)
    {
    	tile = TileEntityAssembler;
    	
    	//addSlot(new SlotViewOnly(TileEntityAssembler, 9, 134, 53));
    	
        for(int i = 0; i < 3; i++)
        {
            for(int l = 0; l < 3; l++)
            {
                addSlot(new Slot(TileEntityAssembler,l + i * 3, 62 + l * 18, 17 + i * 18));
            }

        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }


    private TileEntityAssembler tile;

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return tile.isUseableByPlayer(entityplayer);
    }

    @Override
    protected void retrySlotClick(int i, int j, boolean bl, EntityPlayer par4EntityPlayer) {

    }
}
