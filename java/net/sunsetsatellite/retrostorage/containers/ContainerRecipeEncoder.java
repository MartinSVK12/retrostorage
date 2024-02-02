

package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;


public class ContainerRecipeEncoder extends Container
{

    public ContainerRecipeEncoder(IInventory iinventory, TileEntityRecipeEncoder tileeentityrecipeeencoder)
    {
    	tile = tileeentityrecipeeencoder;
    	
    	addSlot(new Slot(tileeentityrecipeeencoder, 9, 124, 35));
        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                addSlot(new Slot(tileeentityrecipeeencoder, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
            }

        }

        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
                addSlot(new Slot(iinventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
            addSlot(new Slot(iinventory, j1, 8 + j1 * 18, 142));
        }

    }

    private TileEntityRecipeEncoder tile;

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return tile.isUseableByPlayer(entityplayer);
    }

    @Override
    protected void retrySlotClick(int slot, int j, boolean bl, EntityPlayer par4EntityPlayer) {
        tile.useMeta.put(slot-1,!tile.useMeta.get(slot-1));
    }
}
