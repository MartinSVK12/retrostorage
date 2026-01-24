package sunsetsatellite.retrostorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class GhostSlot extends Slot {
    public GhostSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
}
