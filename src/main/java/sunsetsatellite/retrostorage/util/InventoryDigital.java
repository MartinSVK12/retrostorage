package sunsetsatellite.retrostorage.util;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

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
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && this.inventoryContents[i2].getMetadata() == itemDamage) {
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
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && this.inventoryContents[i2].getMetadata() == itemDamage) {
				i += this.inventoryContents[i2].stackSize;
			}
		}

		return i;
	}

	public int storeItemStack(ItemStack stack) {
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == stack.itemID && this.inventoryContents[i2].tag.equals(stack.tag) && this.inventoryContents[i2].isStackable() && this.inventoryContents[i2].stackSize < this.inventoryContents[i2].getMaxStackSize() && this.inventoryContents[i2].stackSize < this.getInventoryStackLimit() && (!this.inventoryContents[i2].getHasSubtypes() || this.inventoryContents[i2].getMetadata() == stack.getMetadata())) {
				return i2;
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
		int i2 = stack.itemID;
		int i3 = stack.stackSize;
		int i4 = this.storeItemStack(stack);
		if(i4 < 0) {
			i4 = this.getFirstEmptyStack();
		}

		if(i4 < 0) {
			return i3;
		} else {
			if(this.inventoryContents[i4] == null) {
				this.inventoryContents[i4] = new ItemStack(i2, 0, stack.getMetadata(), stack.tag);
			}

			int i5 = i3;
			if(i3 > this.inventoryContents[i4].getMaxStackSize() - this.inventoryContents[i4].stackSize) {
				i5 = this.inventoryContents[i4].getMaxStackSize() - this.inventoryContents[i4].stackSize;
			}

			if(i5 > this.getInventoryStackLimit() - this.inventoryContents[i4].stackSize) {
				i5 = this.getInventoryStackLimit() - this.inventoryContents[i4].stackSize;
			}

			if(i5 == 0) {
				return i3;
			} else {
				i3 -= i5;
				this.inventoryContents[i4].stackSize += i5;
				this.inventoryContents[i4].animationsToGo = 5;
				return i3;
			}
		}
	}

	public boolean addItemStackToInventory(ItemStack stack) {
		int i2;
		if(stack.isItemDamaged()) {
			i2 = this.getFirstEmptyStack();
			if(i2 >= 0) {
				this.inventoryContents[i2] = ItemStack.copyItemStack(stack);
				this.inventoryContents[i2].animationsToGo = 5;
				stack.stackSize = 0;
				return true;
			} else {
				return false;
			}
		} else {
			do {
				i2 = stack.stackSize;
				stack.stackSize = this.storePartialItemStack(stack);
			} while(stack.stackSize > 0 && stack.stackSize < i2);

			return stack.stackSize < i2;
		}
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

	public int getSizeInventory() {
		return this.slotsCount;
	}

	public String getInvName() {
		return this.inventoryTitle;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {

	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return true;
	}
}
