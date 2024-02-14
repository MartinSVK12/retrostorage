package sunsetsatellite.retrostorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface DigitalInventoryBase extends Inventory {
    public void ejectAll(World world, int x, int y, int z);
    public void eject(World world, int x, int y, int z, int slot);

    public int getAmountOfUsedSlots();

    ItemStack[] getContents();
}
