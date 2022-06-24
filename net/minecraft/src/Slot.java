package net.minecraft.src;

public class Slot {
	protected final int slotIndex;
	protected final IInventory inventory;
	public int slotNumber;
	public int xDisplayPosition;
	public int yDisplayPosition;

	public Slot(IInventory iInventory1, int i2, int i3, int i4) {
		this.inventory = iInventory1;
		this.slotIndex = i2;
		this.xDisplayPosition = i3;
		this.yDisplayPosition = i4;
	}

	public void onPickupFromSlot(ItemStack itemStack1) {
		this.onSlotChanged();
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return true;
	}

	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	public boolean getHasStack() {
		return this.getStack() != null;
	}

	public void putStack(ItemStack itemStack1) {
		this.inventory.setInventorySlotContents(this.slotIndex, itemStack1);
		this.onSlotChanged();
	}

	public void onSlotChanged() {
		this.inventory.onInventoryChanged();
	}

	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	public int getBackgroundIconIndex() {
		return -1;
	}

	public ItemStack decrStackSize(int i1) {
		return this.inventory.decrStackSize(this.slotIndex, i1);
	}

	public boolean canTakeFromSlot() {
		return true;
	}
}
