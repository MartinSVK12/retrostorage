package sunsetsatellite.retrostorage.util;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.List;

public interface IItemStackList {

    /**
     * @param stack The stack to add
     * @return The remaining amount that couldn't be stored or <code>null</code> if there was no overflow or <code>stack</code> itself was <code>null</code>.
     */
    ItemStack add(ItemStack stack);

    /**
     * @param index Index to insert the item at
     * @param stack The stack to add.
     * @return The remaining amount that couldn't be stored or <code>null</code> if there was no overflow or <code>stack</code> itself was <code>null</code>.
     */
    ItemStack add(int index, ItemStack stack);

    /**
     * @param stacks The stacks to be added
     * @return List of stacks that couldn't be added.
     */
    @UnmodifiableView List<ItemStack> addAll(ItemStackList stacks);

    /**
     * @param stacks The stacks to be added
     * @return List of stacks that couldn't be added.
     */
    @UnmodifiableView List<ItemStack> addAll(List<ItemStack> stacks);

//    /**
//     * @param stacks The stacks to add
//     * @return <code>true</code> if operation was successful, false otherwise.
//     */
//    boolean addAll(ItemStackList stacks);
//
//    /**
//     * @param stacks The stacks to add
//     * @return <code>true</code> if operation was successful, false otherwise.
//     */
//    boolean addAll(List<ItemStack> stacks);
//
//    /**
//     * @param stack The stack to add
//     * @return <code>true</code> if stack can be added, false otherwise.
//     */
//    boolean canAdd(ItemStack stack);
//
//    /**
//     * @param stack The stack to add
//     * @return <code>true</code> if operation was successful, false otherwise.
//     */
//    boolean add(ItemStack stack);

    /**
     * @return Maximum items that can be stored in this list.
     */
    long getItemCapacity();

    /**
     * @return Maximum amount of stacks that can be stored in this list.
     * <br> Usually <code>{@link IItemStackList#getAmount()} / 64</code>.
     */
    long getStackCapacity();

    /**
     * @return Amount of stacks currently in the list.
     * <br> Usually <code>{@link IItemStackList#getAmount()} / 64</code>.
     */
    long getStackAmount();

    /**
     * @return Amounts of items currently in the list.
     */
    long getAmount();

    /**
     * @param slot Slot ID to remove from
     * @param amount Amount to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly <code>amount</code>
     * @param unlimited If <code>false</code>, amount will be limited to {@link Item#getItemStackLimit()}
     * @return The removed item stack.
     */
    ItemStack remove(int slot, long amount, boolean strict, boolean unlimited);

    /**
     * @param slot Slot ID to remove from
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()}
     * @param unlimited If <code>false</code>, amount will be limited to {@link Item#getItemStackLimit()}
     * @return The removed item stack.
     */
    ItemStack remove(int slot, boolean strict, boolean unlimited);

    /**
     * @param id The item ID to remove
     * @param meta The item metadata to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()}
     * @param unlimited If <code>false</code>, amount will be limited to {@link Item#getItemStackLimit()}
     * @return The removed item stack.
     */
    ItemStack remove(int id, int meta, long amount, CompoundTag data, boolean strict, boolean unlimited);

    /**
     * @param stacks List of stacks to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()} for each stack
     * @param unlimited If <code>false</code>, amount will be limited to {@link Item#getItemStackLimit()} for each stack
     * @return <code>true</code> if operation was successful, false otherwise.
     */
    boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited);

    /**
     * @param what   List of stacks to move
     * @param where  List to move stacks to
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()} for each stack
     * @return <code>true</code> if operation was successful, false otherwise.
     */
    @UnmodifiableView List<ItemStack> move(List<ItemStack> what, ItemStackList where, boolean strict);

    /**
     * @param what   List of stacks to move
     * @param where  List to move stacks to
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()} for each stack
     * @return <code>true</code> if operation was successful, false otherwise.
     */
    @UnmodifiableView List<ItemStack> move(ItemStackList what, ItemStackList where, boolean strict);

    /**
     * @param stacks List of stacks to move
     * @param strict If <code>true</code>, method fails if amount in list is not strictly {@link Item#getItemStackLimit()} for each stack
     * @param unlimited If <code>false</code>, amount will be limited to {@link Item#getItemStackLimit()} for each stack
     * @return List of the moved items.
     */
    List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited);

    /**
     * Drops item stack at the specified slot id into the world.
     */
    boolean eject(World world, int x, int y, int z, int slot, long amount, boolean strict);

    /**
     * Drops item stack at the specified item id and metadata into the world.
     */
    boolean eject(World world, int x, int y, int z, int id, int meta, CompoundTag data, long amount, boolean strict);

    /**
     * Drops contents of this list into the world.
     */
    void ejectAll(World world, int x, int y, int z);

    /**
     * @param id The item ID to search for
     * @param meta The item metadata to search for
     * @return <code>true</code> if list contains any of specified item, false otherwise.
     */
    boolean contains(int id, int meta, CompoundTag data);

    /**
     * @param id The item ID to search for
     * @param meta The item metadata to search for
     * @param amount The amount to check
     * @return <code>true</code> if list contains at least <code>amount</code> of specified item, false otherwise.
     */
    boolean containsAtLeast(int id, int meta, CompoundTag data, long amount);

    /**
     * @param stacks List of stacks to check
     * @return <code>true</code> if list contains all the items of <code>stacks</code>, false otherwise.
     */
    boolean containsAtLeast(List<ItemStack> stacks);

    /**
     * @param stacks List of stacks to check
     * @return <code>true</code> if list contains all the items of <code>stacks</code>, false otherwise.
     */
    boolean containsAtLeast(ItemStackList stacks);

    /**
     * @param stacks List of stacks to check against
     * @return List of stacks that this list either doesn't contain at all or doesn't contain a sufficient quantity of.
     */
    ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks);

    /**
     * @param id The item ID to search for
     * @param meta The item metadata to search for
     * @return Amounts of the specified item in the list.
     */
    long count(int id, int meta, CompoundTag data);

    /**
     * @param id The item ID to search for
     * @return Amounts of the specified item in the list.
     */
    long count(int id);

    /**
     * @param id The item ID to search for
     * @param meta The item metadata to search for
     * @return Position in the list or <code>-1</code> if item couldn't be found.
     */
    int find(int id, int meta, CompoundTag data);

    /**
     * @param index The slot ID to get the stack from
     * @return The found stack or <code>null</code>.
     */
    ItemStack get(int index);

    /**
     * @param id The item ID to search for
     * @param meta The item metadata to search for
     * @return The found stack or <code>null</code>.
     */
    ItemStack get(int id, int meta, CompoundTag data);

    /**
     * @return Last stack of the list.
     */
    ItemStack getLast();

    /**
     * Called when the list changes.
     */
    void inventoryChanged();

    /**
     * Deletes all items from this list.
     */
    void clear();

    /**
     * @return Copy of this list.
     */
    IItemStackList copy();

    /**
     * @return Standard unmodifiable java list made from the contents of this list.
     */
    @UnmodifiableView List<ItemStack> getStacks();

    /**
     * @return <code>true</code> if list is empty, false otherwise.
     */
    boolean isEmpty();
}
