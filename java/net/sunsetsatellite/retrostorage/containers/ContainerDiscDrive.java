package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import net.sunsetsatellite.retrostorage.util.SlotViewOnly;


public class ContainerDiscDrive extends Container
{

    public ContainerDiscDrive(IInventory iinventory, TileEntityDiscDrive tileEntitydiscdrive)
    {
    	tile = tileEntitydiscdrive;
    	
    	addSlot(new SlotViewOnly(tileEntitydiscdrive, 2, 80, 35));
        addSlot(new Slot(tileEntitydiscdrive, 0, 46, 35));
        addSlot(new Slot(tileEntitydiscdrive, 1, 115, 35));

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

    @Override
    public ItemStack slotClick(int i, int j, boolean bl, EntityPlayer par4EntityPlayer) {
        Slot slot = this.getSlot(i);
        if(slot instanceof SlotViewOnly){
            return null;
        }
        return super.slotClick(i, j, bl, par4EntityPlayer);
    }

    private TileEntityDiscDrive tile;

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return tile.isUseableByPlayer(entityplayer);
    }

    @Override
    protected void retrySlotClick(int i, int j, boolean bl, EntityPlayer par4EntityPlayer) {

    }

}
