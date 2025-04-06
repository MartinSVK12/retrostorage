package sunsetsatellite.retrostorage.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class SlotViewOnly extends Slot {

    public int variableIndex = 0;

    public SlotViewOnly(Container iinventory, int id, int x, int y) {
        super(iinventory, id, x, y);
        variableIndex = id;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return false;
    }

    @Override
    public void onTake(ItemStack itemstack) {

    }

    @Override
    public @Nullable ItemStack remove(int i) {
        return null;
    }

    @Override
    public void set(@Nullable ItemStack itemstack) {

    }


    @Override
    public void sortSlotInventory() {

    }


    @Override
    public boolean enableDragAndPickup() {
        return false;
    }

    @Override
    public boolean allowItemInteraction() {
        return false;
    }
}
