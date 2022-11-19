// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityDiscDrive extends TileEntityStorage
    implements IInventory
{

    public TileEntityDiscDrive()
    {
        contents = new ItemStack[3];
    }

    public int getSizeInventory()
    {
        return contents.length;
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
    
    public void updateEntity()
    {
        setInventorySlotContents(2,virtualDisc);
        if(controller != null && controller.network_drive != null){
            if (getStackInSlot(2) != null){
                if (getStackInSlot(2).getItem() == mod_RetroStorage.virtualDisc){
                    virtualDisc = getStackInSlot(2);
                }
            }
            if (getStackInSlot(0) != null){
                if (getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                    virtualDriveMaxStacks += ((ItemStorageDisc) getStackInSlot(0).getItem()).getMaxStackCapacity();
                    Object[] vDiscData = virtualDisc.getItemData().getValues().toArray();
                    NBTTagCompound vDiscNBT = virtualDisc.getItemData();
                    ItemStack disc = getStackInSlot(0);
                    Object[] discNBT = disc.getItemData().getValues().toArray();
                    for (Object o : discNBT) {
                        controller.network_inv.addItemStackToInventory(new ItemStack((NBTTagCompound) o));
                        //vDiscNBT.setCompoundTag(String.valueOf(vDiscData.length+i1+1), (NBTTagCompound) o);
                    }
                    setInventorySlotContents(0,null);
                    discsUsed.add(disc);
                    DiscManipulator.saveDisc(controller.network_disc, controller.network_inv);
                    controller.forceReload();
                }
            }
        }
    }

    public void removeLastDisc() {
        if(discsUsed.size() > 0){
            ItemStack disc = discsUsed.get(0);
            discsUsed.remove(0);
            virtualDriveMaxStacks -= Math.min(virtualDriveMaxStacks,((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
            setInventorySlotContents(1,disc);
            NBTTagCompound discData = new NBTTagCompound();
            Object[] virtualDiscDataV = virtualDisc.getItemData().getValues().toArray();
            Object[] virtualDiscDataK = virtualDisc.getItemData().getKeys().toArray();
            int j = Math.min(virtualDisc.getItemData().size(), ((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
            for(int i = 0; i < j; i++) {
                discData.setCompoundTag(String.valueOf(i + 1), (NBTTagCompound) virtualDiscDataV[i]);
                controller.network_inv.setInventorySlotContents(i + 1, null);
                //virtualDisc.getItemData().removeTag((String) virtualDiscDataK[i]);
            }
            disc.setItemData(discData);
            DiscManipulator.saveDisc(controller.network_disc, controller.network_inv);
            controller.forceReload();
        }
        //DiscManipulator.saveDisc(virtualDisc,controller.network_inv);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
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
        return "Disc Drive";
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
        nbttaglist = nbttagcompound.getTagList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            discsUsed.add(new ItemStack(nbttagcompound1));
        }
        virtualDriveMaxStacks = nbttagcompound.getInteger("MaxStacks");

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
        nbttaglist = new NBTTagList();
        for(int i = 0; i < discsUsed.size(); i++)
        {
            if(discsUsed.get(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                discsUsed.get(i).writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("DiscsUsed", nbttaglist);
        nbttagcompound.setTag("MaxStacks",new NBTTagInt(virtualDriveMaxStacks));
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack contents[];
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    public ItemStack virtualDisc = (new ItemStack(mod_RetroStorage.virtualDisc));
    public int virtualDriveMaxStacks = 0;
}
