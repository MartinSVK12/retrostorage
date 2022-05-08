// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_RetroStorage;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityDigitalChest extends TileEntity
    implements IInventory
{

    public TileEntityDigitalChest()
    {
        contents = new ItemStack[39];
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
    
    public void withdrawItem(Slot slot) {
    	//System.out.println((new StringBuilder().append("Slot ").append(slot).append(" want to withdraw item.")).toString());
    	if(getStackInSlot(0) == null) {
    		setInventorySlotContents(0, slot.getStack());
    		NBTTagCompound cellData = getStackInSlot(1).getItemData();
    		Object[] cellDataArray = getStackInSlot(1).getItemData().getValues().toArray();
    		NBTTagCompound cellDataNew = (new NBTTagCompound());
    		for(int i = 0;i < cellDataArray.length;i++) {
    			if(!(((NBTTagCompound) cellDataArray[i]).getShort("id") == slot.getStack().itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == slot.getStack().getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") == slot.getStack().stackSize && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(slot.getStack().getItemData()))) {
    				cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);	
    			}
    		}
    		getStackInSlot(1).setItemData(cellDataNew);
    	}
    }
    
    private void readFromDisc(ItemStack discStack) {
    	Object[] data = discStack.getItemData().getValues().toArray();
		pages = data.length / 36;
		int i = 3;
		
		for (int j = 0 + page*36;j < data.length+1;j++) {
	        if(j < data.length) {
	        	NBTTagCompound itemNBT = (NBTTagCompound) data[j];
				ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
				setInventorySlotContents(i, item);
	        } else {
	        	for(i=i;i<=38;i++) {
	        		setInventorySlotContents(i, null);
	        	}	
	        }
			if (i < 38) {
				i++;
			} else {
				break;
			}		
	    }
    }
    
    private void addToDisc(ItemStack item, ItemStack discStack) {
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = getStackInSlot(1).getItemData();
		Object[] cellDataArray = cellData.getValues().toArray();
		int cellDataSize = cellData.getValues().size();
		if(cellDataSize >= disc.getMaxStackCapacity()) {
			return;
		}
		NBTTagCompound itemNBT = (new NBTTagCompound());
		NBTTagCompound similiarItemNBT = (new NBTTagCompound());
		boolean NBTMatchFound = false;
		int i1;
		for(i1 = 0;i1<cellDataArray.length;i1++) {
			NBTTagCompound cellItemNBT = (NBTTagCompound) cellDataArray[i1];
			if(cellItemNBT.getShort("id") == item.itemID && cellItemNBT.getShort("damage") == item.getItemDamage() && cellItemNBT.getCompoundTag("Data").equals(item.getItemData())) {
				similiarItemNBT = cellItemNBT;
				NBTMatchFound = true;
				break;
			}
		}
		if(NBTMatchFound) {
			if(similiarItemNBT.getByte("Count") + item.stackSize <= 64) {
				similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + item.stackSize));
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				getStackInSlot(1).setItemData(cellData);
			}
			else {
				int remainder = similiarItemNBT.getByte("Count") + item.stackSize - 64;
				similiarItemNBT.setByte("Count", (byte)64);
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				itemNBT.setByte("Count", (byte)remainder);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
				getStackInSlot(1).setItemData(cellData);
			}
		} else {
			itemNBT.setByte("Count", (byte)item.stackSize);
			itemNBT.setShort("id", (short)item.itemID);
			itemNBT.setShort("Damage", (short)item.getItemDamage());
			itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
			//for (int i = 0;i < 2;i++) {
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
			//}
			getStackInSlot(1).setItemData(cellData);
		}
    }
    
    public void updateEntity()
    {
    	/*if (xCoord != 0 && yCoord != 0 && zCoord != 0){
    		if(network.size() > 0) {
    			Iterator<Entry<ArrayList<Integer>, HashMap<String, Object>>> itrt = network.entrySet().iterator();
    			while (itrt.hasNext()) {
    				Map.Entry<ArrayList<Integer>, HashMap<String, Object>> element = (Map.Entry<ArrayList<Integer>, HashMap<String, Object>>)itrt.next();
    				TileEntity tile = (TileEntity) element.getValue().get("tile");
    				if (tile != null) {
    					if (tile instanceof TileEntityDiscDrive) {
    						TileEntityDiscDrive drive = (TileEntityDiscDrive) tile;
    						if (drive.getStackInSlot(0).getItem() instanceof ItemStorageDisc) {
    							readFromDisc(drive.getStackInSlot(0));
    						}
    					}
    				}
    			}
			}
    		TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
    		
    		if (tile instanceof TileEntityChest){
    			//System.out.println("chest connected");
    			for(int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
    				ItemStack item = ((TileEntityChest) tile).getStackInSlot(i);
    				if (item != null) {
    					if (getStackInSlot(1) != null){
    						addToDisc(item, getStackInSlot(1));
    						((TileEntityChest) tile).setInventorySlotContents(i, null);
    					}
    					//System.out.println("There's something in slot "+i);
    				}
    			}
    		}
    	}*/
    	if (getStackInSlot(2) != null) {
    		if (getStackInSlot(1) != null) {
    			if (getStackInSlot(1).getItem() instanceof ItemStorageDisc && getStackInSlot(1).getItem() != mod_RetroStorage.virtualDisc) {
					addToDisc(getStackInSlot(2),getStackInSlot(1));
					setInventorySlotContents(2, null);
    			}
			}
    	}
    	if (getStackInSlot(1) != null) {
    		if(getStackInSlot(1).getItem() instanceof ItemStorageDisc && getStackInSlot(1).getItem() != mod_RetroStorage.virtualDisc) {
    			readFromDisc(getStackInSlot(1));
    			//System.out.println("END LOOP");
    			//System.out.println(data.toString());*/
    		} else
        	{
    			for(int i = 3;i <= 38;i++) {
    				setInventorySlotContents(i, null);
    			}
        		
        	}
    	} else {
    		for(int i = 3;i <= 38;i++) {
				setInventorySlotContents(i, null);
			}
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
        return "Digital Chest";
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
    public int page;
    public int pages;
}
