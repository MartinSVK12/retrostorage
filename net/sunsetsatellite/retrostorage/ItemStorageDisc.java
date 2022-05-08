// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Item;

// Referenced classes of package net.minecraft.src:
//            Item, World, Block, ItemStack, 
//            EntityPlayer

public class ItemStorageDisc extends Item
{

    public ItemStorageDisc(int i, int j)
    {
        super(i);
        maxStackCapacity = j;
    }
    
    public int getMaxStackCapacity() {
    	return maxStackCapacity;
    }
    
    public Item setMaxStackCapacity(int i) {
    	maxStackCapacity = i;
    	return this;
    }
    
    public int maxStackCapacity;

}
