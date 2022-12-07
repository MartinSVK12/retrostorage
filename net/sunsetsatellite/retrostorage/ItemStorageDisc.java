

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemStorageDisc extends Item
    implements IDataItem
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

    @Override
    public String getDescription(ItemStack stack) {
        return stack.getItemData().toString() + " out of " + getMaxStackCapacity();
    }

    public int maxStackCapacity;

}
