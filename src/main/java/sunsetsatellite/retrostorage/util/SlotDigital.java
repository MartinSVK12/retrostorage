package sunsetsatellite.retrostorage.util;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotDigital extends Slot {

    public int variableIndex = 0;
    IDigitalInventory inventory;

    public SlotDigital(InventoryDigital inventory, int id, int x, int y) {
        super(inventory, id, x, y);
        this.variableIndex = id;
        this.inventory = inventory;
    }

    public boolean hasStack() {
        return getStack() != null;
    }

    public int getSlotStackLimit() {
        return Integer.MAX_VALUE;
    }

    public ItemStack getStack() {
        return inventory.get(variableIndex);
    }

    public boolean isHere(final IInventory iinventory, final int i) {
        return iinventory == inventory && i == variableIndex;
    }

    public void onPickupFromSlot(final ItemStack itemstack) {
        onSlotChanged();
    }

    public void onSlotChanged() {
        inventory.inventoryChanged();
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        return inventory.canAdd(itemstack);
    }

    public void putStack(final ItemStack itemstack) {
        if (itemstack == null) {
            if (inventory.get(variableIndex) == null) return;
            int limit = inventory.get(variableIndex).getItem().getItemStackLimit();
            ItemStack removed = inventory.remove(variableIndex, limit, false, false);
            if (removed != null) {
                onSlotChanged();
            }
        }
        boolean success = inventory.add(itemstack);
        if (success) {
            onSlotChanged();
        }
    }

    @Override
    public ItemStack decrStackSize(int i) {
        return this.inventory.remove(this.variableIndex, i,false,false);
    }

    public boolean getIsDiscovered(EntityPlayer player) {
        return true;
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
