package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;

public class TileEntityRecipeEncoder extends TileEntity 
	implements IInventory{

	public TileEntityRecipeEncoder() {
		 contents = new ItemStack[10];
	}
	
	public int getSizeInventory()
    {
        return contents.length;
    }
	
	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory()-1; i++) {
			if(getStackInSlot(i) != null) {
				return false;
			} else
			{
				continue;
			}
		}
		return true;
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
        return "Recipe Encoder";
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

	public void updateEntity()
    {
		return;
    }
	
	public void encodeDisc() {
		ItemStack recipeDisc = getStackInSlot(9);
		if (recipeDisc != null) {
    			if (recipeDisc.getItem() instanceof ItemRecipeDisc) {
    				ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
    				for(int i = 0;i<9;i++) {
    					ItemStack item = getStackInSlot(i);
    					itemList.add(i, item);
    				}
    				//System.out.println(itemList.toString());
    				NBTTagCompound nbt = DiscManipulator.convertRecipeToNBT(itemList);
    				recipeDisc.setItemData(nbt);
    				//System.out.println(recipeDisc.getItemData().toString());
    			}
		}
	}
	
	public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
	
	private ItemStack[] contents;
}
