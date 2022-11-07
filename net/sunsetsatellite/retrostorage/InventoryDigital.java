package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInvBasic;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryDigital implements IInventory {
	public String inventoryTitle;
	private int slotsCount;
	public TileEntityDigitalController owner;
	public ItemStack[] inventoryContents;
	private List field_20073_d;

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
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && this.inventoryContents[i2].getItemDamage() == itemDamage) {
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
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemID && this.inventoryContents[i2].getItemDamage() == itemDamage) {
				i += this.inventoryContents[i2].stackSize;
			}
		}

		return i;
	}

	public int storeItemStack(ItemStack itemStack1) {
		for(int i2 = 0; i2 < this.inventoryContents.length; ++i2) {
			if(this.inventoryContents[i2] != null && this.inventoryContents[i2].itemID == itemStack1.itemID && this.inventoryContents[i2].getItemData().equals(itemStack1.getItemData()) && this.inventoryContents[i2].isStackable() && this.inventoryContents[i2].stackSize < this.inventoryContents[i2].getMaxStackSize() && this.inventoryContents[i2].stackSize < this.getInventoryStackLimit() && (!this.inventoryContents[i2].getHasSubtypes() || this.inventoryContents[i2].getItemDamage() == itemStack1.getItemDamage())) {
				return i2;
			}
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for(int i1 = 0; i1 < this.inventoryContents.length; ++i1) {
			if(i1 > owner.network_drive.virtualDriveMaxStacks){
				return -1;
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

	public int storePartialItemStack(ItemStack itemStack1) {
		int i2 = itemStack1.itemID;
		int i3 = itemStack1.stackSize;
		int i4 = this.storeItemStack(itemStack1);
		if(i4 < 0) {
			i4 = this.getFirstEmptyStack();
		}

		if(i4 < 0) {
			return i3;
		} else {
			if(this.inventoryContents[i4] == null) {
				this.inventoryContents[i4] = new ItemStack(i2, 0, itemStack1.getItemDamage(), itemStack1.getItemData());
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

	public boolean addItemStackToInventory(ItemStack itemStack1) {
		int i2;
		if(itemStack1.isItemDamaged()) {
			i2 = this.getFirstEmptyStack();
			if(i2 >= 0) {
				this.inventoryContents[i2] = ItemStack.copyItemStack(itemStack1);
				this.inventoryContents[i2].animationsToGo = 5;
				itemStack1.stackSize = 0;
				return true;
			} else {
				return false;
			}
		} else {
			do {
				i2 = itemStack1.stackSize;
				itemStack1.stackSize = this.storePartialItemStack(itemStack1);
			} while(itemStack1.stackSize > 0 && itemStack1.stackSize < i2);

			return itemStack1.stackSize < i2;
		}
	}

	public ItemStack getStackInSlot(int i1) {
		return this.inventoryContents.length >= i1 && i1 != -1 ? this.inventoryContents[i1] : null;
	}

	public ItemStack decrStackSize(int i1, int i2) {
		if(this.inventoryContents[i1] != null) {
			ItemStack itemStack3;
			if(this.inventoryContents[i1].stackSize <= i2) {
				itemStack3 = this.inventoryContents[i1];
				this.inventoryContents[i1] = null;
				this.onInventoryChanged();
				return itemStack3;
			} else {
				itemStack3 = this.inventoryContents[i1].splitStack(i2);
				if(this.inventoryContents[i1].stackSize == 0) {
					this.inventoryContents[i1] = null;
				}

				this.onInventoryChanged();
				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i1, ItemStack itemStack2) {
		this.inventoryContents[i1] = itemStack2;
		if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
			itemStack2.stackSize = this.getInventoryStackLimit();
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
		/*if(this.field_20073_d != null) {
			for(int i1 = 0; i1 < this.field_20073_d.size(); ++i1) {
				((IInvBasic)this.field_20073_d.get(i1)).func_20134_a(this);
			}
		}*/

	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return true;
	}
}
