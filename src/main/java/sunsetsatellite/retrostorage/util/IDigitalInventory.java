package sunsetsatellite.retrostorage.util;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;

import java.util.ArrayList;
import java.util.List;

public interface IDigitalInventory {
    ItemStack addAndReturnOverflow(ItemStack stack);

    boolean addAll(ItemStackList stacks);

    boolean addAll(List<ItemStack> stacks);

    boolean canAdd(ItemStack stack);

    void updateSizes(TileEntityDiscDrive drive);

    void resetSizes();

    int getMaxItemSize();

    int getMaxStackSize();

    int sizeStacks();

    int sizeItems();

    boolean add(ItemStack stack);

    //if strict is true, method returns null if amount is more than actually present
    ItemStack remove(int slot, int amount, boolean strict, boolean unlimited);

    ItemStack remove(int slot, boolean strict, boolean unlimited);

    ItemStack remove(int id, int meta, int amount, boolean strict, boolean unlimited);

    boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited);

    boolean move(ItemStackList what, ItemStackList where, boolean strict);

    boolean eject(World world, int x, int y, int z, int slot, int amount, boolean strict);

    boolean eject(World world, int x, int y, int z, int id, int meta, int amount, boolean strict);

    void ejectAll(World world, int x, int y, int z);

    boolean contains(int id, int meta);

    boolean containsAtLeast(int id, int meta, int amount);

    boolean containsAtLeast(List<ItemStack> stacks);

    boolean containsAtLeast(ItemStackList stacks);

    ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks);

    int count(int id, int meta);

    int find(int id, int meta);

    ItemStack get(int index);

    ItemStack get(int id, int meta);

    ItemStack getLast();

    int getLastSlot();

    void inventoryChanged();

    void clear();

    IDigitalInventory copy();

    boolean isEmpty();
}
