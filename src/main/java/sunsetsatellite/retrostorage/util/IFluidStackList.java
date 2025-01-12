package sunsetsatellite.retrostorage.util;

import net.minecraft.core.block.BlockFluid;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IFluidStackList {
    FluidStack add(FluidStack stack);

    @UnmodifiableView List<FluidStack> addAll(FluidStackList stacks);

    @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks);

    /*boolean addAll(FluidStackList stacks);

    boolean addAll(List<FluidStack> stacks);

    boolean canAdd(FluidStack stack);*/

    int getMaxFluidAmount();

    int getMaxFluidStackSize();

    int sizeStacks();

    int sizeItems();

    //boolean add(FluidStack stack);

    //if strict is true, method returns null if amount is more than actually present
    FluidStack remove(int slot, int amount, boolean strict);

    FluidStack removeById(int id, int amount, boolean strict);

    FluidStack remove(int slot, boolean strict);

    boolean move(List<FluidStack> what, FluidStackList where, boolean strict);

    boolean removeAll(List<FluidStack> stacks, boolean strict);

    boolean move(FluidStackList what, FluidStackList where, boolean strict);

    List<FluidStack> moveAll(List<FluidStack> stacks, boolean strict);

    boolean contains(int id);

    boolean containsAtLeast(int id, int amount);

    boolean containsAtLeast(List<FluidStack> stacks);

    boolean containsAtLeast(FluidStackList stacks);

    ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks);

    Set<BlockFluid> getDisallowedFluids();

    int count(int id);

    int find(int id);

    FluidStack get(int index);

    FluidStack getById(int id);

    FluidStack getLast();

    int getLastSlot();

    void inventoryChanged();

    void clear();

    IFluidStackList copy();

    /**
     * @return Standard unmodifiable java list made from the contents of this list.
     */
    @UnmodifiableView
    List<FluidStack> getStacks();

    boolean isEmpty();
}
