package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.sunsetsatellite.retrostorage.tiles.TileEntityProcessProgrammer;

public class ContainerProcessProgrammer extends Container
{

    public ContainerProcessProgrammer(IInventory iinventory, TileEntityProcessProgrammer tileEntityProcessProgrammer)
    {
        tile = tileEntityProcessProgrammer;

        addSlot(new Slot(tileEntityProcessProgrammer, 0, 66, 100));
        addSlot(new Slot(tileEntityProcessProgrammer, 1, 95, 100));

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

    }

    private TileEntityProcessProgrammer tile;

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.isUseableByPlayer(entityPlayer);
    }

    @Override
    protected void retrySlotClick(int i, int j, boolean bl, EntityPlayer par4EntityPlayer) {

    }
}
