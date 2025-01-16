package sunsetsatellite.retrostorage.util;


import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IFluidStackList {
    /**
     * @param stack The stack to add
     * @return The remaining amount that couldn't be stored or <code>null</code> if there was no overflow or <code>stack</code> itself was <code>null</code>.
     */
    FluidStack add(FluidStack stack);

    /**
     * @param index Index to insert the fluid at
     * @param stack The stack to add.
     * @return The remaining amount that couldn't be stored or <code>null</code> if there was no overflow or <code>stack</code> itself was <code>null</code>.
     */
    FluidStack add(int index, FluidStack stack);

    /**
     * @param stacks The stacks to be added
     * @return List of stacks that couldn't be added.
     */
    @UnmodifiableView
    List<FluidStack> addAll(FluidStackList stacks);

    /**
     * @param stacks The stacks to be added
     * @return List of stacks that couldn't be added.
     */
    @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks);

    /**
     * @return Maximum fluid amount that can be stored in this list.
     */
    int getMaxFluidAmount();

    /**
     * @return Maximum amount of stacks that can be stored in this list.
     */
    int getMaxFluidStackSize();

    /**
     * @return Amount of fluids stacks currently in the list.
     */
    int getFluidStackAmount();

    /**
     * @return Amounts of all fluids currently in the list.
     */
    int getFluidAmount();

    /**
     * @param slot The fluid slot to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot
     * @return The removed fluid stack.
     */
    FluidStack remove(int slot, int amount, boolean strict);

    /**
     * @param id The fluid ID to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot
     * @return The removed fluid stack.
     */
    FluidStack removeById(int id, int amount, boolean strict);

    FluidStack remove(int slot, boolean strict);

    /**
     * @param stacks List of stacks to remove
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot for each stack
     * @return <code>true</code> if operation was successful, false otherwise.
     */
    boolean removeAll(List<FluidStack> stacks, boolean strict);

    /**
     * @param what   List of stacks to move
     * @param where  List to move stacks to
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot for each stack
     * @return The list of stacks that couldn't be moved.
     */
    @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict);

    /**
     * @param what   List of stacks to move
     * @param where  List to move stacks to
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot for each stack
     * @return The list of stacks that couldn't be moved.
     */
    @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict);

    /**
     * @param stacks List of stacks to move
     * @param strict If <code>true</code>, method fails if amount in list is not strictly the max capacity for the slot for each stack
     * @return List of the moved items.
     */
    List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict);

    /**
     * @param id The item ID to search for
     * @return <code>true</code> if list contains any of specified fluid, false otherwise.
     */
    boolean contains(int id);

    /**
     * @param id The fluid ID to search for
     * @param amount The amount to check
     * @return <code>true</code> if list contains at least <code>amount</code> of specified fluid, false otherwise.
     */
    boolean containsAtLeast(int id, int amount);

    /**
     * @param stacks List of stacks to check
     * @return <code>true</code> if list contains all the items of <code>stacks</code>, false otherwise.
     */
    boolean containsAtLeast(List<FluidStack> stacks);

    /**
     * @param stacks List of stacks to check
     * @return <code>true</code> if list contains all the fluids of <code>stacks</code>, false otherwise.
     */
    boolean containsAtLeast(FluidStackList stacks);

    /**
     * @param stacks List of stacks to check against
     * @return List of stacks that this list either doesn't contain at all or doesn't contain a sufficient quantity of.
     */
    ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks);

    /**
     * @return Returns a set of fluids this list will not accept.
     */
    Set<FluidType> getDisallowedFluids();

    /**
     * @param id The fluid ID to search for
     * @return Amounts of the specified fluid in the list.
     */
    int count(int id);

    /**
     * @param id The fluid ID to search for
     * @return Position in the list or <code>-1</code> if fluid couldn't be found.
     */
    int find(int id);

    /**
     * @param index The slot ID to get the stack from
     * @return The found stack or <code>null</code>.
     */
    FluidStack get(int index);

    /**
     * @param id The fluids ID to search for
     * @return The found stack or <code>null</code>.
     */
    FluidStack getById(int id);

    /**
     * @return Last stack of the list.
     */
    FluidStack getLast();

    /**
     * @return Last slot of the list.
     */
    int getLastSlot();

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
    IFluidStackList copy();

    /**
     * @return Standard unmodifiable java list made from the contents of this list.
     */
    @UnmodifiableView
    List<FluidStack> getStacks();

    /**
     * @return <code>true</code> if list is empty, false otherwise.
     */
    boolean isEmpty();
}
