package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;

public class TileEntityDigitalContainer extends TileEntity
{
    public int facing = 0;
    public int page = 0;
    public int pages = 0;

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        facing = nbttagcompound.getInteger("facing");

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("facing",facing);

    }
}
