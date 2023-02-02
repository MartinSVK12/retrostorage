package sunsetsatellite.retrostorage.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import sunsetsatellite.retrostorage.RetroStorage;

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
    public NBTTagCompound getDefaultTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setCompoundTag("disc",new NBTTagCompound());
        if(itemID == RetroStorage.goldenDisc.itemID){
            nbt.setBoolean("overrideColor",true);
            nbt.setByte("color", (byte) 0x4);
        }
        return nbt;
    }

    @Override
    public byte getItemNameColor(ItemStack itemstack) {
        return super.getItemNameColor(itemstack);
    }


    public int maxStackCapacity;

}