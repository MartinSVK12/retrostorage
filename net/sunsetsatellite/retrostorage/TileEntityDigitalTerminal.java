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
    	if(slot.slotNumber == 1) {
    		return;
    	}
    	DiscManipulator.outputItemFromDisc(network_disc, slot.getStack(), (TileEntityInNetworkWithInv) this, (TileEntityInNetworkWithInv) network_drive);
    	network_drive.updateDiscs();
    	//System.out.println((new StringBuilder().append("Slot ").append(slot).append(" want to withdraw item.")).toString());
    	
    }

    
    public void updateEntity()
    {

		if(network.size() > 0) {
            for (Entry<ArrayList<Integer>, HashMap<String, Object>> element : network.entrySet()) {
                ArrayList<Integer> pos = element.getKey();
                TileEntity tile = (TileEntity) worldObj.getBlockTileEntity(pos.get(0), pos.get(1), pos.get(2));
                if (tile != null) {
                    if (tile instanceof TileEntityDiscDrive) {
                        TileEntityDiscDrive drive = (TileEntityDiscDrive) tile;
                        network_drive = drive;
                        if (drive.getStackInSlot(drive.getSizeInventory() - 1) != null) {
                            if (drive.getStackInSlot(drive.getSizeInventory() - 1).getItem() instanceof ItemStorageDisc) {
                                network_drive.createVirtualDisc();
                                DiscManipulator.readFromDisc(drive.getStackInSlot(drive.getSizeInventory() - 1), page, this);
                                network_disc = drive.getStackInSlot(drive.getSizeInventory() - 1);
                                pages = network_disc.getItemData().getValues().size() / 36;
                                //DiscManipulator.readPartitionFromPartitionedDisc(network_disc, pages, this, -1);
                                break;
                            } else {
                                network_disc = null;
                                network_drive = null;
                            }
                        } else {
                            network_disc = null;
                            network_drive = null;
                        }
                    } else if (tile instanceof TileEntityStorageBlock) {
                        TileEntityStorageBlock drive = (TileEntityStorageBlock) tile;
                        network_drive = drive;
                        if (drive.getStackInSlot(drive.getSizeInventory() - 1) != null) {
                            if (drive.getStackInSlot(drive.getSizeInventory() - 1).getItem() instanceof ItemStorageDisc) {
                                network_drive.createVirtualDisc();
                                DiscManipulator.readFromDisc(drive.getStackInSlot(drive.getSizeInventory() - 1), page, this);
                                network_disc = drive.getStackInSlot(drive.getSizeInventory() - 1);
                                pages = network_disc.getItemData().getValues().size() / 36;
                                //DiscManipulator.readPartitionFromPartitionedDisc(network_disc, pages, this, -1);
                                break;
                            } else {
                                network_disc = null;
                                network_drive = null;
                            }
                        } else {
                            network_disc = null;
                            network_drive = null;
                        }
                    } else {
                        network_disc = null;
                        network_drive = null;
                    }
                } else {
                    network_disc = null;
                    network_drive = null;
                }
            }
		} else {
			network_disc = null;
			network_drive = null;
		}
    		/*TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
    		
    		if (tile instanceof TileEntityChest){
    			//System.out.println("chest connected");
    			for(int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
    				ItemStack item = ((TileEntityChest) tile).getStackInSlot(i);
    				if (item != null) {
    					if (network_disc != null){
    						addToDisc(item, network_disc);
    						((TileEntityChest) tile).setInventorySlotContents(i, null);
    					}
    					//System.out.println("There's something in slot "+i);
    				}
    			}
    		}*/
    	if (getStackInSlot(2) != null) {
    		if (network_disc != null) {
    			if (network_disc.getItem() instanceof ItemStorageDisc) {
    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
    					if(DiscManipulator.getFirstAvailablePartition(network_disc, network_drive) != -1) {
    						DiscManipulator.addStackToPartitionedDisc(getStackInSlot(2),network_disc,DiscManipulator.getFirstAvailablePartition(network_disc, network_drive));
    						setInventorySlotContents(2, null);
    						network_drive.updateDiscs();
    					}
    				}
    			}
			}
    	}
    	if (network_disc != null) {
    		if(network_disc.getItem() instanceof ItemStorageDisc) {
    			//DiscManipulator.readPartitionFromPartitionedDisc(network_disc, pages, this, -1);
    			//DiscManipulator.readAllFromPartitionedDisc(network_disc, page, this);
    			pages = network_disc.getItemData().getValues().size() / 36;
    			network_drive.createVirtualDisc();
    			DiscManipulator.readFromDisc(network_disc,page,this);
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
    	
    	setInventorySlotContents(1, network_disc);
    	
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 8000D;
    }

    private ItemStack contents[];
    private ItemStack network_disc = null;
    private TileEntityStorage network_drive = null;
    public int page;
    public int pages;
}
