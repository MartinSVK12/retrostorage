package sunsetsatellite.retrostorage.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SlotVariable extends Slot {
    public int variableIndex = 0;
    public final int slotIndex;
    public final Inventory inventory;

    public SlotVariable(Inventory inventory, int id, int x, int y) {
        super( inventory, id, x, y);
        variableIndex = id;
        slotIndex = id;
        this.inventory = inventory;
    }

    public ItemStack getStack() {
        return this.inventory.getStack(this.variableIndex);
    }

    public ItemStack takeStack(int amount) {
        return this.inventory.removeStack(this.variableIndex, amount);
    }

    public void setStack(ItemStack stack) {
        this.inventory.setStack(this.variableIndex, stack);
        this.markDirty();
    }

    @Environment(value= EnvType.SERVER)
    public boolean equals(Inventory inventory, int index) {
        return inventory == this.inventory && index == this.variableIndex;
    }
}
