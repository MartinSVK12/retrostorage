package sunsetsatellite.retrostorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SlotViewOnly extends SlotVariable {
    public SlotViewOnly(Inventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    public ItemStack takeStack(int amount) {
        return null;
    }

    public void setStack(ItemStack stack) {}

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
