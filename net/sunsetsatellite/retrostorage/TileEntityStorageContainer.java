package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class TileEntityStorageContainer extends TileEntity {

    public TileEntityStorageContainer()
    {
    }

    
    public void updateEntity()
    {
        if(isUnlimited && storedID != 0){
            storedAmount = maxAmount;
        }
        if(storedAmount == 0){
            if(!isItemLocked) {
                storedID = 0;
                storedMetadata = 0;
                storedData = new NBTTagCompound();
            }
        }
    }


    public String getInvName()
    {
        return "Storage Container";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        storedID = nbttagcompound.getInteger("itemId");
        storedAmount = nbttagcompound.getInteger("amount");
        storedMetadata = nbttagcompound.getInteger("damage");
        storedData = nbttagcompound.getCompoundTag("data");
        isItemLocked = nbttagcompound.getBoolean("locked");
        super.readFromNBT(nbttagcompound);
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("itemId",storedID);
        nbttagcompound.setInteger("amount",storedAmount);
        nbttagcompound.setInteger("damage",storedMetadata);
        nbttagcompound.setCompoundTag("data",storedData);
        nbttagcompound.setBoolean("locked",isItemLocked);
        super.writeToNBT(nbttagcompound);
    }

    public int getInventoryStackLimit()
    {
        return maxAmount;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int storedID = 0;
    public int storedMetadata = 0;
    public int storedAmount = 0;
    public boolean isItemLocked = false;
    public boolean isUnlimited = true;
    public int maxAmount = 4096;
    public NBTTagCompound storedData = new NBTTagCompound();
}
