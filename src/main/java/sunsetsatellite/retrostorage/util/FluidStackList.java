package sunsetsatellite.retrostorage.util;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.*;

public class FluidStackList implements IFluidStackList, Iterable<FluidStack> {
    protected final ArrayList<FluidStack> contents;
    private final int maxFluidAmount;
    private final int maxFluidStackSize;

    public FluidStackList(List<FluidStack> contents) {
        this.contents = new ArrayList<>(contents);
        this.maxFluidAmount = Integer.MAX_VALUE;
        this.maxFluidStackSize = Integer.MAX_VALUE;
    }

    public FluidStackList() {
        this.contents = new ArrayList<>();
        maxFluidAmount = Integer.MAX_VALUE;
        maxFluidStackSize = Integer.MAX_VALUE;
    }

    public FluidStackList(int maxFluidAmount, int maxFluidStackSize) {
        this.contents = new ArrayList<>();
        this.maxFluidAmount = maxFluidAmount;
        this.maxFluidStackSize = maxFluidStackSize;
    }

    @Override
    public FluidStack add(FluidStack stack) {
        if (stack == null) {
            return null;
        }
        int index = find(stack.fluid.getFirstId());
        if (index != -1) {
            if (getFluidAmount() + stack.amount <= getMaxFluidAmount()) {
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                inventoryChanged();
                return null;
            } else {
                int remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack(remainder);
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                inventoryChanged();
                return split;
            }
        } else {
            if (getFluidAmount() + stack.amount <= getMaxFluidAmount() && getFluidStackAmount() + 1 <= getMaxFluidStackSize()) {
                contents.add(stack);
                inventoryChanged();
                return null;
            } else if (getFluidAmount() + stack.amount > getMaxFluidAmount()) {
                int remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack(remainder);
                contents.add(stack);
                inventoryChanged();
                return split;
            }
        }
        return stack;
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        if(index >= contents.size()) {
            return stack;
        }
        FluidStack invStack = contents.get(index);
        if (invStack == null){
            contents.add(index, stack);
            inventoryChanged();
            return null;
        } else if(invStack.isFluidEqual(stack)) {
            if (getFluidAmount() + stack.amount > getMaxFluidAmount()) {
                long remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack((int) remainder);
                invStack.amount += stack.amount;
                inventoryChanged();
                return split.amount <= 0 ? null : split;
            }
        }
        return stack;
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
        return maxFluidAmount;
    }

    @Override
    public int getMaxFluidStackSize() {
        return maxFluidStackSize;
    }

    @Override
    public int getFluidStackAmount() {
        return contents.size();
    }

    @Override
    public int getFluidAmount() {
        return contents.stream().mapToInt((C) -> C.amount).sum();
    }

    //if strict is true, method returns null if amount is more than actually present
    @Override
    public FluidStack remove(int slot, int amount, boolean strict) {
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
                contents.remove(slot);
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
        if (slot >= contents.size()) {
            return null;
        }
        FluidStack stack = contents.get(slot);
        if (stack == null) return null;
        return remove(slot, Integer.MAX_VALUE, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict) {
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = removeById(stack.fluid.getFirstId(),stack.amount,strict);
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
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        ArrayList<FluidStack> list = new ArrayList<>();
        for (FluidStack stack : stacks) {
            FluidStack removed = removeById(stack.fluid.getFirstId(), stack.amount, strict);
            if (removed != null) {
                list.add(removed);
            }
        }
        return list;
    }

    @Override
    public boolean contains(int id) {
        return contents.stream().anyMatch((S) -> S.fluid.getFirstId() == id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
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
        return contents.stream().mapToInt((S) -> {
            if (S.fluid.getFirstId() == id) {
                return S.amount;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id) {
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
        return contents.get(contents.size() - 1);
    }

    @Override
    public int getLastSlot() {
        return contents.size() - 1;
    }

    @Override
    public void inventoryChanged() {
    }

    @Override
    public void clear() {
        contents.clear();
        inventoryChanged();
    }

    @Override
    public IFluidStackList copy() {
        FluidStackList inv = new FluidStackList(maxFluidAmount,maxFluidStackSize);
        inv.contents.stream().map(FluidStack::copy).forEach(inv.contents::add);
        return inv;
    }

    @Override
    public @UnmodifiableView List<FluidStack> getStacks() {
        return Collections.unmodifiableList(contents);
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public String toString() {
        return contents.toString();
    }

    @NotNull
    @Override
    public Iterator<FluidStack> iterator() {
        return contents.iterator();
    }

    public void writeToNbt(CompoundTag tag){
        for (int i = 0; i < contents.size(); i++) {
            FluidStack fluid = contents.get(i);
            CompoundTag fluidNBT = new CompoundTag();
            if (fluid != null) {
                fluid.writeToNBT(fluidNBT);
                tag.putCompound(String.valueOf(i), fluidNBT);
            } else {
                tag.getValue().remove(String.valueOf(i));
            }
        }
    }

    public void readFromNbt(CompoundTag tag){
        for (Tag<?> value : tag.getValues()) {
            CompoundTag fluidNBT = (CompoundTag) value;
            FluidStack stack = new FluidStack(fluidNBT);
            if(stack.amount > 0){
                contents.add(stack);
            }
        }
    }
}

