

package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import java.util.List;

public class ItemStorageDisc extends ItemReS {

    public int maxStackCapacity;

    public ItemStorageDisc(int i, int j) {
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
    public void addInformation(ItemStack itemStack, List list) {
        if(!itemStack.hasTagCompound()){
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        list.add(itemStack.getTagCompound().getCompoundTag("disc").toString()+" out of "+ getMaxStackCapacity());
    }
}

