package sunsetsatellite.retrostorage.util;

import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.*;

public class FluidInventoryWrapper implements IFluidStackList{

    public IFluidInventory connected;

    public FluidInventoryWrapper(IFluidInventory inventory) {
        connected = inventory;
    }

    @Override
    public FluidStack add(FluidStack stack) {
        if(stack == null || connected == null) return stack;

        int n = stack.amount;

        for (int i = 0; i < connected.getFluidInventorySize(); i++) {
            FluidStack invStack = connected.getFluidInSlot(i);
            if(invStack == null) {
                int amount = Math.min(stack.amount, connected.getFluidCapacityForSlot(i));
                n -= amount;
                connected.setFluidInSlot(i, stack.splitStack(amount));
                if(n <= 0) break;
            } else if(invStack.isFluidEqual(stack)) {
                int remaining = Math.min(n,connected.getFluidCapacityForSlot(i) - invStack.amount);
                n -= remaining;
                invStack.amount += remaining;
                if(n <= 0) break;
            }
        }

        if(n <= 0){
            return null;
        }

        return new FluidStack(stack.fluid,n);
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        return connected.insertFluid(index, stack);
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(FluidStackList stacks) {
        return addAll(stacks.getStacks());
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks) {
        ArrayList<FluidStack> newStacks = new ArrayList<>();

        for (FluidStack stack : stacks) {
            newStacks.add(add(stack));
        }

        return Collections.unmodifiableList(RetroStorage.condenseFluidList(newStacks));
    }

    @Override
    public int getMaxFluidAmount() {
        if(connected == null) return 0;
        int n = 0;
        for (int i = 0; i < connected.getFluidInventorySize(); i++) {
            n += connected.getFluidCapacityForSlot(i);
        }
        return n;
    }

    @Override
    public int getMaxFluidStackSize() {
        if(connected == null) return 0;
        return connected.getFluidInventorySize();
    }

    @Override
    public int getFluidStackAmount() {
        List<FluidStack> contents = getStacks();
        return contents.size();
    }

    @Override
    public int getFluidAmount() {
        List<FluidStack> contents = getStacks();
        return contents.stream().mapToInt((C) -> C.amount).sum();
    }

    @Override
    public FluidStack remove(int slot, int amount, boolean strict) {
        List<FluidStack> contents = RetroStorage.collectFluidStacks(connected);
        if (slot >= contents.size()) {
            return null;
        }
        FluidStack stack = contents.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.amount) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.amount);
            FluidStack splitStack = stack.splitStack(amount);
            if (stack.amount <= 0) {
                connected.setFluidInSlot(0,null);
            }
            inventoryChanged();
            return splitStack;
        }
        return null;
    }

    @Override
    public FluidStack removeById(int id, int amount, boolean strict) {
        int slot = find(id);
        if (slot != -1) {
            return remove(slot, amount, strict);
        }
        return null;
    }

    @Override
    public FluidStack remove(int slot, boolean strict) {
        List<FluidStack> contents = RetroStorage.collectFluidStacks(connected);
        if (slot >= contents.size()) {
            return null;
        }
        FluidStack stack = contents.get(slot);
        if (stack == null) return null;
        return remove(slot, Integer.MAX_VALUE, strict);
    }

    @Override
    public boolean removeAll(List<FluidStack> stacks, boolean strict) {
        for (FluidStack stack : stacks) {
            FluidStack removed = removeById(stack.fluid.getFirstId(), stack.amount, strict);
            if (removed == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict) {
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = remove(stack.fluid.getFirstId(),stack.amount,strict);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            FluidStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(RetroStorage.condenseFluidList(leftovers));
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict) {
        return move(what.getStacks(),where,strict);
    }

    @Override
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        ArrayList<FluidStack> list = new ArrayList<>();
        for (FluidStack stack : stacks) {
            FluidStack removed = remove(stack.fluid.getFirstId(),stack.amount,strict);
            if (removed != null) {
                list.add(removed);
            }
        }
        return list;
    }

    @Override
    public boolean contains(int id) {
        List<FluidStack> contents = getStacks();
        return contents.stream().anyMatch((S) -> S.fluid.getFirstId() == id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
        List<FluidStack> contents = getStacks();
        return contents.stream().anyMatch((S) -> S.fluid.getFirstId() == id && S.amount >= amount);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks) {
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.fluid.getFirstId(), stack.amount);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.fluid.getFirstId(), stack.amount);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks) {
        ArrayList<FluidStack> missing = new ArrayList<>();
        for (FluidStack stack : stacks) {
            int c = count(stack.fluid.getFirstId());
            if (c <= 0) {
                missing.add(stack.copy());
            } else if (c != stack.amount) {
                FluidStack copy = stack.copy();
                copy.amount -= c;
                missing.add(stack.copy());
            }
        }
        return missing;
    }

    @Override
    public Set<Fluid> getDisallowedFluids() {
        return new HashSet<>();
    }

    @Override
    public int count(int id) {
        List<FluidStack> contents = getStacks();
        return contents.stream().mapToInt((S) -> {
            if (S.fluid.getFirstId() == id) {
                return S.amount;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id) {
        List<FluidStack> contents = getStacks();
        for (int i = 0; i < contents.size(); i++) {
            FluidStack content = contents.get(i);
            if (content.fluid.getFirstId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public FluidStack get(int index) {
        List<FluidStack> contents = getStacks();
        if (index < 0 || index >= contents.size()) {
            return null;
        }
        return contents.get(index);
    }

    @Override
    public FluidStack getById(int id) {
        return get(find(id));
    }

    @Override
    public FluidStack getLast() {
        return getStacks().get(getStacks().size() - 1);
    }

    @Override
    public int getLastSlot() {
        return getStacks().size() - 1;
    }

    @Override
    public void inventoryChanged() {

    }

    /**
     * Unsupported in this class, will always throw {@link UnsupportedOperationException}!
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported in this class, will always throw {@link UnsupportedOperationException}!
     */
    @Override
    public IFluidStackList copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @UnmodifiableView List<FluidStack> getStacks() {
        return RetroStorage.collectAndCondenseFluidStacks(connected);
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
