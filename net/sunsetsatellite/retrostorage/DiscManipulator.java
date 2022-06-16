package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;

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

	@Deprecated
    public static void readFromAssembler(ItemStack discStack, int page, TileEntityInNetworkWithInv reader, TileEntityAssembler asm) {
    	Object[] data = discStack.getItemData().getValues().toArray();
		int i = 3;

		for (int j = 0 /*+ page*36*/;j < asm.getSizeInventory()+1;j++) {
	        if(j < asm.getSizeInventory()) {
	        	//NBTTagCompound itemNBT = (NBTTagCompound) data[j];
	        	ItemStack recipeDisc = asm.getStackInSlot(j);
	        	CraftingManager crafter = CraftingManager.getInstance();
	        	if(recipeDisc != null) {
	    			if (recipeDisc.getItem() == mod_RetroStorage.recipeDisc) {
	    				ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(recipeDisc.getItemData());
	    				ItemStack item = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
	    				reader.setInventorySlotContents(i, item);
	    			}
	        	}
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
				if(cellItemNBT.getByte("Count") != 64){
					similiarItemNBT = cellItemNBT;
					NBTMatchFound = true;
					break;
				}
			}
		}
		if(NBTMatchFound) {
			System.out.println("Found similiar item in network");
			if(similiarItemNBT.getByte("Count") + item.stackSize <= 64) {
				similiarItemNBT.setByte("Count", (byte)(similiarItemNBT.getByte("Count") + item.stackSize));
				cellData.setCompoundTag((new StringBuilder()).append(i1+1).toString(), similiarItemNBT);
				similiarItemNBT.setInteger("disc", partitionIndex);
				discStack.setItemData(cellData);
			}
			else if (similiarItemNBT.getByte("Count") + item.stackSize > 64) {
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
}
