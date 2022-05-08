// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.mod_RetroStorage;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityDiscDrive extends TileEntityStorage
    implements IInventory
{

    public TileEntityDiscDrive()
    {
        contents = new ItemStack[10];
        createVirtualDisc();
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
    
    
    
    public void createVirtualDisc() {
    	virtualDisc.setItemData((NBTTagCompound) NBTBase.createTagOfType((byte) 10));
    	for(int i = 0; i < getSizeInventory()-2;i++) {
    		if(getStackInSlot(i) != null) {
    			if (getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
    				ItemStack disc = getStackInSlot(i);
    				ItemStorageDisc discItem = (ItemStorageDisc) disc.getItem();
    				NBTTagCompound discNBTData = disc.getItemData();
    				Object[] discData = discNBTData.getValues().toArray();
    				for(int j = 0;j<discData.length;j++) {
    					NBTTagCompound discItemNBT = (NBTTagCompound) discData[j];
    					DiscManipulator.addDataToPartitionedDisc(discItemNBT, virtualDisc, i);
    				}
    			}
    		}
    	}
    	setInventorySlotContents(9, virtualDisc);
    }
    
    public void updateDiscs() {
    	NBTTagCompound vDiscNBTData = virtualDisc.getItemData();
		Object[] vDiscData = vDiscNBTData.getValues().toArray();
		if (vDiscData.length == 0) {
			for(int i = 0; i < getSizeInventory()-2;i++) {
	    		if(getStackInSlot(i) != null) {
	    			if (getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
	    				ItemStack discStack = getStackInSlot(i);
	    				ItemStorageDisc discItem = (ItemStorageDisc) discStack.getItem();
	    				discStack.setItemData((NBTTagCompound) NBTBase.createTagOfType((byte) 10));
	    				return;
	    			}
	    		}
			}
		}
		for(int i = 0; i < getSizeInventory()-2;i++) {
    		if(getStackInSlot(i) != null) {
    			if (getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
    				ItemStack discStack = getStackInSlot(i);
    				ItemStorageDisc discItem = (ItemStorageDisc) discStack.getItem();
    				//System.out.println(DiscManipulator.getPartitionFromDisc(virtualDisc, i).toStringExtended());
    				discStack.setItemData(DiscManipulator.getPartitionFromDisc(virtualDisc, i));
    			}
    		}
		}
    }
    
    public void updateEntity()
    {
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
    	if(i != 9) {
    		for(int j = 0;j < contents.length;j++) {
        		if (contents[j] == null) { 
        			contents[j] = itemstack;
        			break;
        		}
        	}
    	} else {
    		contents[i] = itemstack;
    	}
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack contents[];
    private ItemStack virtualDisc = (new ItemStack(mod_RetroStorage.virtualDisc));
}
