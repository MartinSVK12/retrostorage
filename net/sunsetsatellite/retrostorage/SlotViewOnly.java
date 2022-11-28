

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotViewOnly extends Slot
{

    public SlotViewOnly(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {

    }
    
    public int getSlotStackLimit()
    {
        return Byte.MAX_VALUE;
    }


    public boolean canTakeFromSlot() {
    	return false;
    }
    
}
