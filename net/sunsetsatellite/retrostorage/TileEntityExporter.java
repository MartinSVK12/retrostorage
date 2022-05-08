package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;

public class TileEntityExporter extends TileEntityInNetworkWithInv {

	public TileEntityExporter() {
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
        return "Item Exporter";
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
		connectDrive();
		
		if(network.size() > 0 && network_disc != null && network_drive != null) {
			TileEntity chest = findTileEntityAroundBlock();
			if (chest instanceof TileEntityChest){
				//System.out.println("chest connected");
				/*for(int i = 0; i < ((TileEntityChest) chest).getSizeInventory(); i++) {
					ItemStack item = ((TileEntityChest) chest).getStackInSlot(i);
					if (item == null) {
						if (network_disc.getItem() instanceof ItemStorageDisc) {
		    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
		    					if(DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive) != -1) {
	    							ItemStack network_item = DiscManipulator.getItemFromDiscByIndex(network_disc, i+1);
		    						if(network_item != null || network_item.itemID != 0) {
		    							DiscManipulator.removeFromPartitionedDisc(network_disc, network_item, DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive));
			    						((TileEntityChest) chest).setInventorySlotContents(i, network_item);
			    						network_drive.updateDiscs();
		    						}
		    					}
		    				}
		    			}
					}*/
				chest = (TileEntityChest) chest;
				ItemStack item = null;
				int slot = 0;
				for(int i = 0; i < ((TileEntityChest) chest).getSizeInventory(); i++) {
					item = ((TileEntityChest) chest).getStackInSlot(i);
					if (item != null) {
						continue;
					} else {
						slot = i;
						break;
					}
				}
				if(isEmpty()) {
					if (item == null) {
						if (network_disc.getItem() instanceof ItemStorageDisc) {
		    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
		    					if(DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive) != -1) {
	    							ItemStack network_item = DiscManipulator.getItemFromDiscByIndex(network_disc, 1);
		    						if(network_item != null || network_item.itemID != 0) {
		    							DiscManipulator.removeFromPartitionedDisc(network_disc, network_item, DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive));
			    						((TileEntityChest) chest).setInventorySlotContents(slot, network_item);
			    						network_drive.updateDiscs();
		    						}
		    					}
		    				}
						}
					}
				} else {
					if (item == null) {
						if (network_disc.getItem() instanceof ItemStorageDisc) {
		    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
		    					if(DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive) != -1) {
		    						ItemStack filter_item = DiscManipulator.itemExistsInDisc(network_disc, getStackInSlot(0).getItem());
		    						if (filter_item != null) {
		    							DiscManipulator.removeFromPartitionedDisc(network_disc, filter_item, DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive));
		    							((TileEntityChest) chest).setInventorySlotContents(slot, filter_item);
			    						network_drive.updateDiscs();
		    						} else {
		    							int i = 0;
		    							do {
		    								if(getStackInSlot(i) != null) {
		    									filter_item = DiscManipulator.itemExistsInDisc(network_disc, getStackInSlot(i).getItem());
		    								}
		    								i++;
		    								if(i == 9) {
		    									break;
		    								}
		    							} while (filter_item == null);
		    							if (filter_item != null) {
			    							DiscManipulator.removeFromPartitionedDisc(network_disc, filter_item, DiscManipulator.getFirstNonEmptyPartition(network_disc, network_drive));
			    							((TileEntityChest) chest).setInventorySlotContents(slot, filter_item);
				    						network_drive.updateDiscs();
			    						}
		    						}
		    					}
		    				}
						}
					}
				}
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
