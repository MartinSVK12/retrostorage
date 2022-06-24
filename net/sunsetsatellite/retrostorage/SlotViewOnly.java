// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

// Referenced classes of package net.minecraft.src:
//            Slot, EntityPlayer, ItemStack, Item, 
//            AchievementList, ModLoader, IInventory

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
