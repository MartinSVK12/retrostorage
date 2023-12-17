package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;

import java.util.List;

public class ContainerAdvInterface extends Container
{

    public ContainerAdvInterface(IInventory iinventory, TileEntityAdvInterface TileEntityAdvInterface)
    {
        tile = TileEntityAdvInterface;

        //addSlot(new SlotDigital(TileEntityInterface, 9, 134, 53));

        for(int i = 0; i < 3; i++)
        {
            for(int l = 0; l < 3; l++)
            {
                addSlot(new Slot(TileEntityAdvInterface, l + i * 3, 62 + l * 18, 17 + i * 18));
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

    /*@Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean bl, boolean bl2) {
        if(tile.network != null){
            //RetroStorage.LOGGER.debug(String.format("i:%d player:%s, bool1:%s, bool2:%s",i,entityPlayer,shift,ctrl));
            ItemStack item = this.getSlot(i).getStack().copy();
            ItemStack original = this.getSlot(i).getStack();
            if(i > 0 && i < 9){
                this.onStackMergeShiftClick(this.getSlot(i).getStack(),9,44,false);
            } else {
                this.onStackMergeShiftClick(this.getSlot(i).getStack(),0,9,false);
                this.getSlot(i).onPickupFromSlot(item);
            }
            if (original.stackSize == 0) {
                this.getSlot(i).putStack(null);
            } else {
                this.getSlot(i).onSlotChanged();
            }
        }
    }*/

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityAdvInterface tile;

}
