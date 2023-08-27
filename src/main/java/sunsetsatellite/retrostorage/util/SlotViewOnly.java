package sunsetsatellite.retrostorage.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotViewOnly extends Slot {

    public int variableIndex = 0;
    public SlotViewOnly(IInventory iinventory, int id, int x, int y) {
        super(iinventory, id, x, y);
        variableIndex = id;
    }

    public boolean canPutStackInSlot(ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack itemstack) {}

    @Override
    public boolean enableDragAndPickup() {
        return false;
    }

    @Override
    public boolean allowItemInteraction() {
        return false;
    }
}
