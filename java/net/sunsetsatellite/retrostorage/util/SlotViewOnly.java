package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotViewOnly extends Slot {

    public int variableIndex = 0;
    public SlotViewOnly(IInventory iinventory, int id, int x, int y) {
        super(iinventory, id, x, y);
        variableIndex = id;
    }

    public boolean canPutStackInSlot(ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack itemstack) {}
}
