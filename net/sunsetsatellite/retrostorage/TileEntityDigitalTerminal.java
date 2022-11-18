// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityDigitalTerminal extends TileEntityInNetworkWithInv
{

    public TileEntityDigitalTerminal()
    {
        contents = new ItemStack[37];
    }

    public int getSizeInventory()
    {
        return 37;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }
    
    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void updateContents(){
    }

    public void updateEntity()
    {
        ticksSinceUpdate += 1;
        if(ticksSinceUpdate >= 60){
            ticksSinceUpdate = 0;
            if(controller != null && controller.isActive()){
                DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
            }
        }
        if(controller == null || !controller.isActive()){
            for(int i = 0;i<getSizeInventory();i++){
                setInventorySlotContents(i,null);
            }
            return;
        }
        if(controller != null){
            contents = controller.network_inv.inventoryContents;
            setInventorySlotContents(0, controller.network_disc);
        }

        if(controller.network_disc != null) {
            this.pages = ((int) Math.floor((double) controller.network_disc.getItemData().size() / (36)));
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if(itemstack != null && getStackInSlot(0) != null){
            if (i == 0 && itemstack.getItem() instanceof ItemStorageDisc) {
                //DiscManipulator.saveDisc(controller.network_disc, this, page);

            }
        }

        if(i == 0 && itemstack == null){
            DiscManipulator.clearDigitalInv(this);
        }
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }
    
    public void onInventoryChanged() {
    	super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Digital Terminal";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getAmountOfUsedSlots(){
        int j = 0;
        if(controller.network_disc != null){
            j = controller.network_disc.getItemData().size();
        }
        return j;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 8000D;
    }

    private ItemStack contents[];
    private int ticksSinceUpdate = 0;
}
