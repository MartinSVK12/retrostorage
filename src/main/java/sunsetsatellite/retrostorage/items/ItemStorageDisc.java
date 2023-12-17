package sunsetsatellite.retrostorage.items;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class ItemStorageDisc extends Item implements ICustomDescription
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

    /*@Override
    public CompoundTag getDefaultTag() {
        CompoundTag nbt = new CompoundTag();
        //nbt.putCompound("disc",new CompoundTag());
        if(itemID == RetroStorage.goldenDisc.itemID){
            nbt.putBoolean("overrideColor",true);
            nbt.putByte("color", (byte) 0x4);
        }
        return nbt;
    }

    @Override
    public byte getItemNameColor(ItemStack itemstack) {
        return super.getItemNameColor(itemstack);
    }*/


    public int maxStackCapacity;

    @Override
    public String getDescription(ItemStack itemStack) {
        return TextFormatting.MAGENTA+""+itemStack.getData().getCompound("disc").getValues().size()+" entries out of "+maxStackCapacity;
    }
}