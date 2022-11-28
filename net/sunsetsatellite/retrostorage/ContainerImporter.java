

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;


public class ContainerImporter extends Container
{

    public ContainerImporter(IInventory iinventory, TileEntityImporter tileEntityImporter)
    {
        tile = tileEntityImporter;

    }

	public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityImporter tile;
}
