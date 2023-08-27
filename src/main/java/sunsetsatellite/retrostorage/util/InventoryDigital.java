package sunsetsatellite.retrostorage.util;




import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

import java.util.ArrayList;

public class InventoryDigital implements IInventory {
	public String inventoryTitle;
	private final int slotsCount;
	public TileEntityDigitalController owner;
	public ItemStack[] inventoryContents;

	public InventoryDigital(String string1, TileEntityDigitalController controller) {
		this.inventoryTitle = string1;
		this.slotsCount = Short.MAX_VALUE*2;
		this.inventoryContents = new ItemStack[Short.MAX_VALUE*2];
		this.owner = controller;
	}

	public int getInventorySlotContainItem(int itemID) {
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID) {
				return i2;
			}
		}

		return -1;
	}

	public int getInventorySlotContainItem(int itemID, int itemDamage) {
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && (this.inventoryContents[i2].getMetadata() == itemDamage || itemDamage == -1)) {
				return i2;
			}
		}

		return -1;
	}

	public int getItemCount(int itemID){
		int i = 0;
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID) {
				i += this.inventoryContents[i2].stackSize;
			}
		}

		return i;
	}

	public int getItemCount(int itemID, int itemDamage){
		int i = 0;
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && (this.inventoryContents[i2].getMetadata() == itemDamage || itemDamage == -1)) {
				i += this.inventoryContents[i2].stackSize;
			}
		}

		return i;
	}

	public int storeItemStack(ItemStack stack) {
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null){
				if(this.inventoryContents[i2].itemID == stack.itemID){
					if((stack.tag == null && this.inventoryContents[i2].tag == null) || ((this.inventoryContents[i2].tag).equals(stack.tag))){
						if(this.inventoryContents[i2].isStackable()){
							if (this.inventoryContents[i2].stackSize < this.inventoryContents[i2].getMaxStackSize()){
								if(this.inventoryContents[i2].stackSize < this.getInventoryStackLimit()){
									if((!this.inventoryContents[i2].getHasSubtypes() || (this.inventoryContents[i2].getMetadata() == stack.getMetadata() || stack.getMetadata() == -1))){
										return i2;
									}
								}
							}
						}
					}
				}
			}
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for(int i1 = 0; i1 < this.inventoryContents.length; ++i1) {
			if(owner.network != null && owner.network.drive != null){
				if(i1 > owner.network.drive.virtualDriveMaxStacks){
					return -1;
				}
			}
			if(this.inventoryContents[i1] == null) {
				return i1;
			}
		}

		return -1;
	}

	public int getFirstOccupiedStack(){
		for(int i1 = 1; i1 < this.inventoryContents.length; ++i1) {
			if(this.inventoryContents[i1] != null) {
				return i1;
			}
		}

		return -1;
	}

	public int getLastOccupiedStack(){
		int i2 = -1;
		for(int i1 = 1; i1 < this.inventoryContents.length; ++i1) {
			if(this.inventoryContents[i1] != null) {
				i2 = i1;
			}
		}

		return i2;
	}

	public int getStackAmount(){
		int i2 = 0;
		for(int i1 = 1; i1 < this.inventoryContents.length; ++i1) {
			if(this.inventoryContents[i1] != null) {
				i2++;
			}
		}

		return i2;
	}


	public int storePartialItemStack(ItemStack stack) {
		int id = stack.itemID;
		int stackSize = stack.stackSize;
		if(stack.tag != null){
			stack.tag.setName("Data");
		}
		int i4 = this.storeItemStack(stack);
		if(i4 < 0) {
			i4 = this.getFirstEmptyStack();
		}

		if(i4 < 0) {
			return stackSize;
		} else {
			if(this.inventoryContents[i4] == null) {
				this.inventoryContents[i4] = new ItemStack(id, 0, stack.getMetadata(), stack.tag);
			}

			int i5 = stackSize;
			if(stackSize > this.inventoryContents[i4].getMaxStackSize() - this.inventoryContents[i4].stackSize) {
				i5 = this.inventoryContents[i4].getMaxStackSize() - this.inventoryContents[i4].stackSize;
			}

			if(i5 > this.getInventoryStackLimit() - this.inventoryContents[i4].stackSize) {
				i5 = this.getInventoryStackLimit() - this.inventoryContents[i4].stackSize;
			}

			if(i5 == 0) {
				return stackSize;
			} else {
				stackSize -= i5;
				this.inventoryContents[i4].stackSize += i5;
				this.inventoryContents[i4].animationsToGo = 5;
				return stackSize;
			}
		}
	}

	public boolean addItemStackToInventory(ItemStack itemstack) {
		int i;
		/*if (itemstack.isItemDamaged()) {
			i = this.getFirstEmptyStack();
			if (i >= 0) {
				this.inventoryContents[i] = ItemStack.copyItemStack(itemstack);
				this.inventoryContents[i].animationsToGo = 5;
				itemstack.stackSize = 0;
				this.onInventoryChanged();
				return true;
			} else {
				return false;
			}
		} else {*/
			do {
				i = itemstack.stackSize;
				itemstack.stackSize = this.storePartialItemStack(itemstack);
			} while(itemstack.stackSize > 0 && itemstack.stackSize < i);
			this.onInventoryChanged();
			return itemstack.stackSize < i;
		//}
	}

	public ItemStack getStackInSlot(int id) {
		return this.inventoryContents.length >= id && id != -1 ? this.inventoryContents[id] : null;
	}

	public ItemStack decrStackSize(int id, int amount) {
		if(this.inventoryContents[id] != null) {
			ItemStack itemStack3;
			if(this.inventoryContents[id].stackSize <= amount) {
				itemStack3 = this.inventoryContents[id];
				this.inventoryContents[id] = null;
				this.onInventoryChanged();
				return itemStack3;
			} else {
				itemStack3 = this.inventoryContents[id].splitStack(amount);
				if(this.inventoryContents[id].stackSize == 0) {
					this.inventoryContents[id] = null;
				}

				this.onInventoryChanged();
				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int id, ItemStack stack) {
		this.inventoryContents[id] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public boolean hasItems(ArrayList<ItemStack> stacks){
		for (ItemStack stack : stacks) {
			if (getItemCount(stack.itemID, stack.getMetadata()) < stack.stackSize) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<ItemStack> hasItemsReturnMissing(ArrayList<ItemStack> stacks){
		ArrayList<ItemStack> missing = new ArrayList<>();
		for (ItemStack stack : stacks) {
			if (getItemCount(stack.itemID, stack.getMetadata()) < stack.stackSize) {
				boolean alreadyMissing = false;
				for(ItemStack misStack : missing){
					if(misStack.isItemEqual(stack)){
						misStack.stackSize += stack.stackSize - getItemCount(stack.itemID, stack.getMetadata());
						alreadyMissing = true;
						break;
					}
				}
				if(alreadyMissing){
					continue;
				}
				ItemStack missingStack = stack.copy();
				missingStack.stackSize = stack.stackSize - getItemCount(stack.itemID, stack.getMetadata());
				missing.add(missingStack);
			}
		}
		return missing;
	}

	public boolean removeItems(ArrayList<ItemStack> stacks){
		boolean valid = hasItems(stacks);
		if(!valid){
			return false;
		}
		for (ItemStack stack : stacks){
			ItemStack copy = stack.copy();
			int slot;
			int count;
			int fullCount = getItemCount(stack.itemID,stack.getMetadata());
			if(fullCount >= stack.stackSize){
				do{
					fullCount = getItemCount(stack.itemID,stack.getMetadata());
					if(fullCount < copy.stackSize){
						return false;
					}
					slot = getInventorySlotContainItem(stack.itemID,stack.getMetadata());
					count = inventoryContents[slot].stackSize;
					ItemStack invStack = inventoryContents[slot];
					if(invStack.getItem().hasContainerItem()){
						addItemStackToInventory(new ItemStack(invStack.getItem().getContainerItem(),1));
					}
					decrStackSize(slot,Math.min(stack.stackSize,count));
					copy.stackSize -= Math.min(stack.stackSize,count);
				} while(copy.stackSize > 0);
			}
			else {
				return false;
			}
		}
		return true;
	}

	public int getSizeInventory() {
		return inventoryContents.length;
	}

	public String getInvName() {
		return this.inventoryTitle;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
		if(owner != null){
			if(owner.network.drive != null){
				DiscManipulator.saveDisc(owner.network.drive.virtualDisc,this);
			}
		}
	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return true;
	}
}
