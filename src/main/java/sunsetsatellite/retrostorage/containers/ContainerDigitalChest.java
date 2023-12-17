package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.util.SlotDigital;

import java.util.List;

public class ContainerDigitalChest extends Container
{

    public ContainerDigitalChest(IInventory iinventory, TileEntityDigitalChest tileentitydigitalchest)
    {

        addSlot(new SlotDigital(tileentitydigitalchest, 0, 80, 108));

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

    /*@Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean shift, boolean ctrl) {
        if(tile.getStackInSlot(0) != null){
            if(tile.getAmountOfUsedSlots() < ((ItemStorageDisc)tile.getStackInSlot(0).getItem()).getMaxStackCapacity()){
                //RetroStorage.LOGGER.debug(String.format("i:%d player:%s, bool1:%s, bool2:%s",i,entityPlayer,shift,ctrl));
                ItemStack item = this.getSlot(i).getStack().copy();
                ItemStack original = this.getSlot(i).getStack();
                if(i > 0 && i < 37){
                    this.onStackMergeShiftClick(this.getSlot(i).getStack(),37,73,false);
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

    private TileEntityDigitalChest tile;
}
