

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;


public class ContainerRedstoneEmitter extends Container
{

    public ContainerRedstoneEmitter(IInventory iinventory, TileEntityRedstoneEmitter tileEntityRedstoneEmitter)
    {
    	tile = tileEntityRedstoneEmitter;

        addSlot(new Slot(tileEntityRedstoneEmitter, 0, 45, 35));

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

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityRedstoneEmitter tile;

}
