

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Item;

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
