package sunsetsatellite.retrostorage.items;

import net.minecraft.src.Item;
import net.minecraft.src.NBTTagCompound;
import sunsetsatellite.retrostorage.RetroStorage;

public class ItemAdvRecipeDisc extends Item {
    public ItemAdvRecipeDisc(int i) {
        super(i);
    }

    @Override
    public NBTTagCompound getDefaultTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setCompoundTag("disc",new NBTTagCompound());
        nbt.setBoolean("overrideColor",true);
        nbt.setByte("color", (byte) 0x2);
        return nbt;
    }
}
