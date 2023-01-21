package sunsetsatellite.retrostorage;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

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

    @Override
    public byte getItemNameColor(ItemStack itemstack) {
        return super.getItemNameColor(itemstack);
    }
    /*@Override
    public String getDescription(ItemStack stack) {
        return stack.getItemData().toString() + " out of " + getMaxStackCapacity();
    }

    @Override
    public int getDescriptionColor(ItemStack stack) {
        return 0xFF808080;
    }

    @Override
    public int getNameColor(ItemStack stack) {
        return -1;
    }*/

    public int maxStackCapacity;

}