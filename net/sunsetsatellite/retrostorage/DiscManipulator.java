package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.*;

public class DiscManipulator {

	public DiscManipulator() {
	}

	public static void outputItemFromDisc(ItemStack discStack, ItemStack item, TileEntityInNetworkWithInv reader, TileEntityInNetworkWithInv drive) {
		if(reader.getStackInSlot(0) == null) {
    		reader.setInventorySlotContents(0, item);
    		NBTTagCompound cellData = discStack.getItemData();
    		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
    		NBTTagCompound cellDataNew = (new NBTTagCompound());
			boolean removed = false;
    		for(int i = 0;i < cellDataArray.length;i++) {
    			if(!(((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") == item.stackSize && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData()))) {
    				cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
    			} else {
					if (!removed){
						removed = true;
					} else {
						cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
					}
				}
    		}
    		discStack.setItemData(cellDataNew);
    	}
	}

	public static void removeFromDisc(ItemStack discStack, ItemStack item) {
		NBTTagCompound cellData = discStack.getItemData();
		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
		NBTTagCompound cellDataNew = (new NBTTagCompound());
		for(int i = 0;i < cellDataArray.length;i++) {
			if(!(((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") == item.stackSize && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData()))) {
				cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
			}
		}
		discStack.setItemData(cellDataNew);
	}

	public static boolean decreaseItemAmountOnDisc(ItemStack discStack, ItemStack item) {
		System.out.println("Removing "+item.toString()+" from "+discStack.toString());
		NBTTagCompound cellData = discStack.getItemData();
		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
		NBTTagCompound cellDataNew = (new NBTTagCompound());
		//System.out.println(item.toString());
        int subtracting = 0;
		boolean flag = false;
		for(int i = 0; i < cellDataArray.length; i++) {
			if((((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") >= item.stackSize) && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData())) {
				//System.out.println("Subtracting..");
				if (subtracting < 2){
                    int amount = ((int)((NBTTagCompound) cellDataArray[i]).getByte("Count")) - item.stackSize;
                    if (amount <= 0) {
                        //System.out.println("zero");
                        flag = true;
                        continue;
                    }
                    System.out.println(amount);
                    NBTTagCompound itemNBT = (new NBTTagCompound());
                    itemNBT.setByte("Count", (byte)amount);
                    itemNBT.setShort("id", (short) ((NBTTagCompound) cellDataArray[i]).getShort("id"));
                    itemNBT.setShort("Damage", (short) ((NBTTagCompound) cellDataArray[i]).getShort("Damage"));
                    itemNBT.setInteger("disc", ((NBTTagCompound) cellDataArray[i]).getInteger("disc"));
                    itemNBT.setCompoundTag("Data", ((NBTTagCompound) cellDataArray[i]).getCompoundTag("Data"));
                    cellDataNew.setCompoundTag(String.valueOf(i+1),itemNBT);
                    subtracting++;
                } else {
                    cellDataNew.setCompoundTag(String.valueOf(i+1),((NBTTagCompound) cellDataArray[i]).copy());
                }
				flag = true;
			} else {
				cellDataNew.setCompoundTag(String.valueOf(i+1),((NBTTagCompound) cellDataArray[i]).copy());
			}

		}
		//System.out.println(cellDataNew.toStringExtended());
		discStack.setItemData(cellDataNew);
		//System.out.println(discStack.getItemData().toString());
		return flag;
	}

	public static boolean testDecreaseItemAmountOnDisc(ItemStack discStack, ItemStack item) {
		NBTTagCompound cellData = discStack.getItemData();
		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
		NBTTagCompound cellDataNew = (new NBTTagCompound());
		//System.out.println(item.toString());
		boolean flag = false;
		for(int i = 0;i < cellDataArray.length;i++) {
			/*System.out.println(((NBTTagCompound) cellDataArray[i]).toStringExtended());
			System.out.println((((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID));
			System.out.println(((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage());
			System.out.println(((NBTTagCompound)cellDataArray[i]).getByte("Count") >= item.stackSize);
			System.out.println(((NBTTagCompound)cellDataArray[i]).getByte("Count"));*/
			//System.out.println(item.stackSize);
			//System.out.println(((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData()));
			if((((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") >= item.stackSize) && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData())) {
				/*int amount = ((int)((NBTTagCompound) cellDataArray[i]).getByte("Count")) - item.stackSize;
				if (amount <= 0) {
					System.out.println("zero");
					flag = true;
					continue;
				}
				((NBTTagCompound) cellDataArray[i]).setByte("Count",(byte)amount);*/
				//cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
				flag = true;
			} else {
				//cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
			}

		}
		//discStack.setItemData(cellDataNew);
		if(flag) {
			return true;
		}
		return false;
	}

	public static void removeFromPartitionedDisc(ItemStack discStack, ItemStack item, int partitionIndex) {
		NBTTagCompound cellData = discStack.getItemData();
		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
		NBTTagCompound cellDataNew = (new NBTTagCompound());
		boolean removed = false;
		for(int i = 0;i < cellDataArray.length;i++) {
			if(!(((NBTTagCompound) cellDataArray[i]).getShort("id") == item.itemID && ((NBTTagCompound)cellDataArray[i]).getShort("Damage") == item.getItemDamage() && ((NBTTagCompound)cellDataArray[i]).getByte("Count") == item.stackSize && ((NBTTagCompound)cellDataArray[i]).getInteger("disk") == partitionIndex && ((NBTTagCompound)cellDataArray[i]).getCompoundTag("Data").equals(item.getItemData()))) {
				cellDataNew.setCompoundTag(String.valueOf(i),(NBTTagCompound) cellDataArray[i]);
			} else {
				if (!removed) {
					removed = true;
				} else {
					cellDataNew.setCompoundTag(String.valueOf(i), (NBTTagCompound) cellDataArray[i]);
				}
			}
		}
		discStack.setItemData(cellDataNew);
	}

	public static ItemStack getItemFromDiscByIndex(ItemStack discStack, int index) {
		NBTTagCompound itemNBT = discStack.getItemData().getCompoundTag(((Integer)index).toString());
		ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
		return item;
	}

	public static ItemStack itemExistsInDisc(ItemStack discStack, Item item) {
		NBTTagCompound cellData = discStack.getItemData();
		Object[] cellDataArray = discStack.getItemData().getValues().toArray();
		NBTTagCompound cellDataNew = (new NBTTagCompound());
		for(int i = 0;i < cellDataArray.length;i++) {
			if((((NBTTagCompound) cellDataArray[i]).getShort("id") == item.shiftedIndex )) {
				ItemStack stack = new ItemStack((NBTTagCompound) cellDataArray[i]);
				return stack;
			}
		}
		return null;
	}

    public static void readFromDisc(ItemStack discStack, int page, TileEntityInNetworkWithInv reader) {
    	Object[] data = discStack.getItemData().getValues().toArray();
		int i = 3;

		for (int j = 0 + page*36;j < data.length+1;j++) {
	        if(j < data.length) {
	        	NBTTagCompound itemNBT = (NBTTagCompound) data[j];
				ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
				reader.setInventorySlotContents(i, item);
	        } else {
	        	for(;i<=38;i++) {
	        		reader.setInventorySlotContents(i, null);
	        	}
	        }
			if (i < 38) {
				i++;
			} else {
				break;
			}
	    }
    }

	public static void readItemAssembly(int page, TileEntityInNetworkWithInv reader, TileEntityDigitalController controller){
		int i = 3;

		for (int j = 0 + page*36;j < controller.itemAssembly.size()+1;j++) {
			if(j < controller.itemAssembly.size()) {
				//NBTTagCompound itemNBT = (NBTTagCompound) data[j];
					reader.setInventorySlotContents(i, (ItemStack) controller.itemAssembly.keySet().toArray()[j]);

				//ItemStack item = //(new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
			} else {
				for(;i<=38;i++) {
					reader.setInventorySlotContents(i, null);
				}
			}
			if (i < 38) {
				i++;
			} else {
				break;
			}
		}
	}
	
    /*public static void readPartitionFromPartitionedDisc(ItemStack discStack, int page, TileEntityInNetworkWithInv reader, int partitionIndex) {
    	Object[] data = discStack.getItemData().getCompoundTag("partition"+partitionIndex).getValues().toArray();
		int pages = data.length / 36;
		int i = 3;
		
		for (int j = 0 + page*36;j < data.length+1;j++) {
	        if(j < data.length) {
	        	NBTTagCompound itemNBT = (NBTTagCompound) data[j];
				ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
				reader.setInventorySlotContents(i, item);
	        } else {
	        	for(i=i;i<=38;i++) {
	        		reader.setInventorySlotContents(i, null);
	        	}	
	        }
			if (i < 38) {
				i++;
			} else {
				break;
			}		
	    }
    }*/
    
	/*public static void addPartitionToDisc(ItemStack discStack) {
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound discNBT = discStack.getItemData();
		int discNBTSize = 0;//discNBT.getValues().size()-1;
		if (discNBT.hasKey("partition-1")) {
			discNBTSize = discNBT.getValues().size()-2;
		} else {
			discNBTSize = discNBT.getValues().size()-1;
		}
		NBTTagCompound partition = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		discNBT.setCompoundTag("partition"+(discNBTSize+1), partition);
	}
	
	public static void addPartitionToDisc(ItemStack discStack, int index) {
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound discNBT = discStack.getItemData();
		int discNBTSize = discNBT.getValues().size()-1;
		NBTTagCompound partition = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		discNBT.setCompoundTag("partition"+index, partition);
	}*/
	
	
	public static NBTTagCompound getPartitionFromDisc(ItemStack discStack, int partitionIndex) {
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound discNBT = discStack.getItemData();
		NBTTagCompound data = (NBTTagCompound) NBTBase.createTagOfType((byte) 10);
		for(Object item : discNBT.getValues().toArray()) {
			if (((NBTTagCompound) item).getInteger("disc") == partitionIndex) {
				data.setCompoundTag((new StringBuilder()).append(data.getValues().size()+1).toString(),(NBTTagCompound) item);
			}
		}
		//System.out.println(data.toStringExtended());
		return data;
		//return discNBT.getCompoundTag("partition"+partitionIndex);*/
		
	}
	
	public static int getMaxPartitions(TileEntityInNetworkWithInv drive) {
		int partitions = 0;
		if(drive instanceof TileEntityDiscDrive) {
			for(int i = 0; i < drive.getSizeInventory()-2;i++) {
	    		if(drive.getStackInSlot(i) != null) {
	    			if (drive.getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
	    				partitions++;
	    			}
	    		}
			}
		} else if (drive instanceof TileEntityStorageBlock) {
			return 1;
		}
		
		return partitions;
	}
	
	public static int getDiscMaxStacks(ItemStack discStack, TileEntityInNetworkWithInv drive) {
		
		if(discStack.getItem() != mod_RetroStorage.virtualDisc) {
			return ((ItemStorageDisc) discStack.getItem()).getMaxStackCapacity();
		} else if (discStack.getItem() == mod_RetroStorage.virtualDisc) {
			int capacity = 0;
			for(int i = 0;i < getMaxPartitions(drive);i++) {
				ItemStack disc = drive.getStackInSlot(i);
				if(disc != null) {
					ItemStorageDisc discItem = (ItemStorageDisc) disc.getItem();
					capacity += discItem.maxStackCapacity;
				}
			}
			return capacity;
		} else {
			return 0;
		}
	}
	
	public static int getFirstAvailablePartition(ItemStack discStack, TileEntityInNetworkWithInv drive) {
		for(int i = 0;i < getMaxPartitions(drive);i++) {
			ItemStack disc = drive.getStackInSlot(i);
			if(disc != null) {
				ItemStorageDisc discItem = (ItemStorageDisc) disc.getItem();
				if(disc.itemData.getValues().size() < discItem.maxStackCapacity) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static int getFirstNonEmptyPartition(ItemStack discStack, TileEntityInNetworkWithInv drive) {
		for(int i = 0;i < getMaxPartitions(drive);i++) {
			ItemStack disc = drive.getStackInSlot(i);
			if(disc != null) {
				ItemStorageDisc discItem = (ItemStorageDisc) disc.getItem();
				if(disc.itemData.getValues().size() > 0) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static void addDataToPartitionedDisc(NBTTagCompound data, ItemStack discStack, int partitionIndex) {
		/*ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = discStack.getItemData();
		NBTTagCompound cellPartitionData = discStack.getItemData().getCompoundTag("partition"+partitionIndex);
		Object[] cellDataArray = cellPartitionData.getValues().toArray();
		int cellDataSize = cellPartitionData.getValues().size();
		if(cellDataSize >= disc.getMaxStackCapacity()) {
			return;
		}
		NBTTagCompound itemNBT = (new NBTTagCompound());
		NBTTagCompound similiarItemNBT = (new NBTTagCompound());
		boolean NBTMatchFound = false;
		int i1;
		for(i1 = 0;i1<cellDataArray.length;i1++) {
			NBTTagCompound cellItemNBT = (NBTTagCompound) cellDataArray[i1];
			if(cellItemNBT.getShort("id") == data.getShort("id") && cellItemNBT.getShort("damage") == data.getShort("Damage") && cellItemNBT.getCompoundTag("Data").equals(data.getCompoundTag("Data"))) {
				similiarItemNBT = cellItemNBT;
				NBTMatchFound = true;
				break;
			}
		}
		if(NBTMatchFound) {
			if(similiarItemNBT.getByte("Count") + data.getByte("Count") <= 64) {
				similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + data.getByte("Count")));
				cellPartitionData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				cellData.setCompoundTag("partition"+partitionIndex, cellPartitionData);
				discStack.setItemData(cellData);
			}
			else {
				int remainder = similiarItemNBT.getByte("Count") + data.getByte("Count") - 64;
				similiarItemNBT.setByte("Count", (byte)64);
				cellPartitionData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				itemNBT.setByte("Count", (byte)remainder);
				itemNBT.setShort("id", (short)data.getShort("id"));
				itemNBT.setShort("Damage", (short)data.getShort("Damage"));
				itemNBT.setCompoundTag("Data", (NBTTagCompound)data.getCompoundTag("Data"));
				cellPartitionData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
				cellData.setCompoundTag("partition"+partitionIndex, cellPartitionData);
				discStack.setItemData(cellData);
			}
		} else {
			//for (int i = 0;i < 2;i++) {
			cellPartitionData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), data);
			cellData.setCompoundTag("partition"+partitionIndex, cellPartitionData);
			//}
			discStack.setItemData(cellData);
		}*/
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = discStack.getItemData();
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
			if(cellItemNBT.getShort("id") == data.getShort("id") && cellItemNBT.getShort("damage") == data.getShort("Damage")){ //&& cellItemNBT.getCompoundTag("Data").equals(data.getCompoundTag("Data"))) {
				similiarItemNBT = cellItemNBT;
				NBTMatchFound = true;
				break;
			}
		}
		if(NBTMatchFound) {
			if(similiarItemNBT.getByte("Count") + data.getByte("Count") <= 64) {
				similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + data.getByte("Count")));
				similiarItemNBT.setInteger("disc", partitionIndex);
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				discStack.setItemData(cellData);
			}
			else {
				int remainder = similiarItemNBT.getByte("Count") + data.getByte("Count") - 64;
				similiarItemNBT.setByte("Count", (byte)64);
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				itemNBT.setByte("Count", (byte)remainder);
				itemNBT.setShort("id", (short)data.getShort("id"));
				itemNBT.setShort("Damage", (short)data.getShort("Damage"));
				itemNBT.setInteger("disc", partitionIndex);
				itemNBT.setCompoundTag("Data", (NBTTagCompound)data.getCompoundTag("Data"));
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
				discStack.setItemData(cellData);
			}
		} else {
			//for (int i = 0;i < 2;i++) {
			data.setInteger("disc", partitionIndex);
			cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), data);
			//}
			discStack.setItemData(cellData);
		}
	}
	
	//@SuppressWarnings("unused")
	public static void addDataToDisc(NBTTagCompound data, ItemStack discStack) {
    	ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = discStack.getItemData();
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
			if(cellItemNBT.getShort("id") == data.getShort("id") && cellItemNBT.getShort("damage") == data.getShort("Damage") && cellItemNBT.getCompoundTag("Data").equals(data.getCompoundTag("Data"))) {
				similiarItemNBT = cellItemNBT;
				NBTMatchFound = true;
				break;
			}
		}
		if(NBTMatchFound) {
			if(similiarItemNBT.getByte("Count") + data.getByte("Count") <= 64) {
				similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + data.getByte("Count")));
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				discStack.setItemData(cellData);
			}
			else {
				int remainder = similiarItemNBT.getByte("Count") + data.getByte("Count") - 64;
				similiarItemNBT.setByte("Count", (byte)64);
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				itemNBT.setByte("Count", (byte)remainder);
				itemNBT.setShort("id", (short)data.getShort("id"));
				itemNBT.setShort("Damage", (short)data.getShort("Damage"));
				itemNBT.setCompoundTag("Data", (NBTTagCompound)data.getCompoundTag("Data"));
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
				discStack.setItemData(cellData);
			}
		} else {
			//for (int i = 0;i < 2;i++) {
			cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), data);
			//}
			discStack.setItemData(cellData);
		}
    }
    
    @SuppressWarnings("unused")
	public static void addStackToDisc(ItemStack item, ItemStack discStack) {
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = discStack.getItemData();
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
				discStack.setItemData(cellData);
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
				discStack.setItemData(cellData);
			}
		} else {
			itemNBT.setByte("Count", (byte)item.stackSize);
			itemNBT.setShort("id", (short)item.itemID);
			itemNBT.setShort("Damage", (short)item.getItemDamage());
			itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
			//for (int i = 0;i < 2;i++) {
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
			//}
			discStack.setItemData(cellData);
		}
    }

    public static void addStackToPartitionedDisc(ItemStack item, ItemStack discStack, int partitionIndex) {
		System.out.println((new StringBuilder()).append("Adding ").append(item.toString()).append(" to partition ").append(partitionIndex).append(" of ").append(discStack.toString()));
		ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
		NBTTagCompound cellData = discStack.getItemData().copy();
		Object[] cellDataArray = cellData.getValues().toArray();
		int cellDataSize = cellData.getKeys().size();
		System.out.println("Data size:"+String.valueOf(cellDataSize));
;		if(cellDataSize >= disc.getMaxStackCapacity()) {
			return;
		}
		NBTTagCompound itemNBT = (new NBTTagCompound());
		NBTTagCompound similiarItemNBT = (new NBTTagCompound());
		boolean NBTMatchFound = false;
		int i1;
		for(i1 = 0;i1<cellDataArray.length;i1++) {
			NBTTagCompound cellItemNBT = (NBTTagCompound) cellDataArray[i1];
			if(cellItemNBT.getShort("id") == item.itemID && cellItemNBT.getShort("damage") == item.getItemDamage() && cellItemNBT.getCompoundTag("Data").equals(item.getItemData())) {
				if(cellItemNBT.getByte("Count") != 64){
					similiarItemNBT = cellItemNBT;
					NBTMatchFound = true;
					System.out.println("Found similiar item at "+String.valueOf(i1));
					break;
				}
			}
		}
		if(NBTMatchFound) {
			System.out.println(similiarItemNBT.toStringExtended());
			if(similiarItemNBT.getByte("Count") + item.stackSize <= 64) {
				System.out.println("Less than 64");
				itemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + item.stackSize));
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setInteger("disc", partitionIndex);
				//similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + item.stackSize));
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), itemNBT);
				discStack.setItemData(cellData);
			}
			else if (similiarItemNBT.getByte("Count") + item.stackSize > 64) {
				System.out.println("More than 64");
				int total = similiarItemNBT.getByte("Count") + item.stackSize;
				int remainder = total - 64;
				System.out.println((new StringBuilder()).append("Total: ").append(total));
				System.out.println((new StringBuilder()).append("Remainder: ").append(remainder));
				similiarItemNBT.setByte("Count", (byte)64);
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				itemNBT.setByte("Count", (byte)remainder);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setInteger("disc", partitionIndex);
				itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData().copy());
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
				discStack.setItemData(cellData);
			}
		} else {
			itemNBT.setByte("Count", (byte)item.stackSize);
			itemNBT.setShort("id", (short)item.itemID);
			itemNBT.setShort("Damage", (short)item.getItemDamage());
			itemNBT.setInteger("disc", partitionIndex);
			itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData().copy());
			//for (int i = 0;i < 2;i++) {
				cellData.setCompoundTag((new StringBuilder()).append(cellDataSize+1).toString(), itemNBT);
			//}
			discStack.setItemData(cellData);
		}
    }
    
    public static ArrayList<?> convertRecipeToArray(NBTTagCompound recipe){
    	Object[] recipeArray = recipe.getValues().toArray();
    	ArrayList<ItemStack> recipeList = new ArrayList<ItemStack>();
    	for (int i = 0;i<recipeArray.length;i++) {
    		NBTTagCompound itemNBT = recipe.getCompoundTag(((Integer)i).toString());
    		if (itemNBT.getValues().size() <= 0) {
    			recipeList.add(i, null);
    			continue;
    		}
    		ItemStack item = (new ItemStack(itemNBT.getShort("id"),itemNBT.getByte("Count"),itemNBT.getShort("Damage"),itemNBT.getCompoundTag("Data")));
    		recipeList.add(item);
    	}
    	return recipeList;
    }
    
    public static NBTTagCompound convertRecipeToNBT(ArrayList<ItemStack> recipe) {
    	NBTTagCompound recipeNBT = (new NBTTagCompound());
    	//System.out.println(recipe.size());
    	for(int i = 0;i<recipe.size();i++) {
    		NBTTagCompound itemNBT = (new NBTTagCompound());
    		ItemStack item = recipe.get(i);
    		if (item == null) {
    			recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
    			continue;
    		}
    		itemNBT.setByte("Count", (byte)item.stackSize);
			itemNBT.setShort("id", (short)item.itemID);
			itemNBT.setShort("Damage", (short)item.getItemDamage());
			itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
			recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
    	}
    	return recipeNBT;
    }

	public static void saveDisc(ItemStack disc, IInventory inv, int page){
		//System.out.printf("Saving contents of page %d of inventory %s to disc %s%n",page,inv.toString(),disc.toString());
		if(disc == null){
			return;
		}
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < 37;i++){
			ItemStack item = inv.getStackInSlot(i);
			NBTTagCompound itemNBT = new NBTTagCompound();
			if(item != null){
				itemNBT.setByte("Count", (byte)item.stackSize);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
				discNBT.setCompoundTag(String.valueOf(i+(page*36)),itemNBT);
			} else {
				discNBT.removeTag(String.valueOf(i+(page*36)));
			}
		}
		//System.out.printf("Data: %s%n",discNBT.toStringExtended());
		disc.setItemData(discNBT);
	}

	public static void saveDisc(ItemStack disc, IInventory inv){
		//System.out.printf("Saving contents of entire inventory %s to disc %s%n",inv.toString(),disc.toString());
		//System.out.printf("Inv contents: %s%n", Arrays.toString(((InventoryDigital) inv).inventoryContents));
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < inv.getSizeInventory();i++){
			ItemStack item = inv.getStackInSlot(i);
			NBTTagCompound itemNBT = new NBTTagCompound();
			if(item != null){
				itemNBT.setByte("Count", (byte)item.stackSize);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
				discNBT.setCompoundTag(String.valueOf(i),itemNBT);
			} else {
				discNBT.removeTag(String.valueOf(i));
			}
		}
		//System.out.printf("Data: %s%n",discNBT.toStringExtended());
		disc.setItemData(discNBT);
	}

	public static void saveDisc(TileEntityDigitalController controller){
		ItemStack disc = controller.network_disc;
		IInventory inv = controller.network_inv;
		//System.out.printf("Saving contents of entire inventory %s to disc %s%n",inv.toString(),disc.toString());
		//System.out.printf("Inv contents: %s%n", Arrays.toString(((InventoryDigital) inv).inventoryContents));
		if(controller.network_disc == null && !controller.isActive() && !controller.clearing){
			return;
		}
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < inv.getSizeInventory();i++){
			ItemStack item = inv.getStackInSlot(i);
			NBTTagCompound itemNBT = new NBTTagCompound();
			if(item != null){
				itemNBT.setByte("Count", (byte)item.stackSize);
				itemNBT.setShort("id", (short)item.itemID);
				itemNBT.setShort("Damage", (short)item.getItemDamage());
				itemNBT.setCompoundTag("Data", (NBTTagCompound)item.getItemData());
				discNBT.setCompoundTag(String.valueOf(i),itemNBT);
			} else {
				discNBT.removeTag(String.valueOf(i));
			}
		}
		//System.out.printf("Data: %s%n",discNBT.toStringExtended());
		disc.setItemData(discNBT);
	}

	public static void loadDisc(ItemStack disc, IInventory inv, int page){
		//System.out.printf("Loading contents of page %d of disc %s to inventory %s%n",page,disc.toString(),inv.toString());
		NBTTagCompound discNBT = disc.getItemData();
		for(int i = 1; i < 37;i++){
			if(discNBT.hasKey(String.valueOf(i+(page*36)))){
				ItemStack item = new ItemStack(discNBT.getCompoundTag(String.valueOf(i+(page*36))));
				inv.setInventorySlotContents(i,item);
			}
		}

	}

	public static void clearDigitalInv(IInventory inv){
		//System.out.printf("Clearing digital inventory %s%n",inv.toString());
		for (int i = 1; i < inv.getSizeInventory(); i++){
			inv.setInventorySlotContents(i,null);
		}
	}

	public static boolean addCraftRequest(ItemStack item, int count, TileEntityDigitalController controller){
		if(controller == null){
			return false;
		}
		if(controller.itemAssembly.get(item).get(0) instanceof TileEntityInterface){
			TileEntityInterface itemInterface = (TileEntityInterface) controller.itemAssembly.get(item).get(0);
			EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
			entityplayer.addChatMessage("Requesting processing of "+count+"x "+StringTranslate.getInstance().translateNamedKey(item.getItemName()));
			int networkItemCount = controller.network_inv.getItemCount(item.itemID,item.getItemDamage());
			int requestItemCount = item.stackSize * count;
			if(networkItemCount >= requestItemCount){
				for(int i = 0;i<count;i++) {
					controller.assemblyQueue.add(item);
				}
			} else {
				entityplayer.addChatMessage("Request failed! Not enough resources.");
			}
			return false;
		}
		EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
		entityplayer.addChatMessage("Requesting "+count+"x of "+item.stackSize+"x "+StringTranslate.getInstance().translateNamedKey(item.getItemName()));
		CraftingManager crafter = CraftingManager.getInstance();
		TileEntityAssembler assembler = (TileEntityAssembler) controller.itemAssembly.get(item).get(0);
		int assemblerSlot = (int) controller.itemAssembly.get(item).get(1);
		ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(assembler.getStackInSlot(assemblerSlot).getItemData());
		ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
		HashMap<ArrayList<Integer>, Integer> requirements = new HashMap<ArrayList<Integer>, Integer>();
		HashMap<Item,ItemStack> assemblyItems = new HashMap<>();
		int s = 0;
		for (Map.Entry<ItemStack, List<Object>> entry : controller.itemAssembly.entrySet()) {
			assemblyItems.put(entry.getKey().getItem(),entry.getKey());
		}
		for (Object value : recipe) {
			if (value != null) {
				ArrayList<Integer> item1 = new ArrayList<>();
				item1.add(((ItemStack)value).itemID);
				item1.add(((ItemStack)value).getItemDamage());
				if (!requirements.containsKey(item1)){
					requirements.put(item1, 1);
				} else {
					requirements.replace(item1,requirements.get(item1),requirements.get(item1)+1);
				}
			}
		}
		for (Map.Entry<ArrayList<Integer>, Integer> entry : requirements.entrySet()) {
			ArrayList<Integer>  key = entry.getKey();
			Integer value = entry.getValue();
			entry.setValue(entry.getValue() * count);
		}
		System.out.println("Craft requirements: "+requirements.toString());
		for (Map.Entry<ArrayList<Integer> , Integer> i1 : requirements.entrySet()) {
			ItemStack stack = new ItemStack(i1.getKey().get(0),1,i1.getKey().get(1));
			int networkItemCount = controller.network_inv.getItemCount(i1.getKey().get(0),i1.getKey().get(1));
			if(networkItemCount >= requirements.get(i1.getKey())){
				s++;
			} else {
				if(assemblyItems.containsKey(stack.getItem())){
					ItemStack assemblyStack = assemblyItems.get(stack.getItem());
					int reqCount = requirements.get(i1.getKey())/assemblyStack.stackSize;
					if(reqCount <= 0) reqCount = 1;
					System.out.println("Calling subrequest for: "+assemblyStack.toString());
					boolean result = addCraftRequest(assemblyStack,reqCount,controller);
					if(result){
						s++;
					} else {
						entityplayer.addChatMessage("Request failed! Subrequest failed.");
						return false;
					}
				} else {
					entityplayer.addChatMessage("Request failed! Not enough resources and no valid subrequests could be made.");
					return false;

				}
			}
		}
		if(s == requirements.size()){
			entityplayer.addChatMessage("Request successful!");
			for(int i = 0;i<count;i++){
				controller.assemblyQueue.add(item);
			}
			return true;
		} else {
			entityplayer.addChatMessage("Request failed! Not enough resources.");
			return false;
		}
	}

	public static NBTTagCompound stacksToNBT(ItemStack[] stacks){

		return null;
	}

	public static ItemStack[] NBTToStacks(NBTTagCompound NBT){

		return new ItemStack[0];
	}

}
