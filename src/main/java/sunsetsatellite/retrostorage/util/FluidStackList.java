package sunsetsatellite.retrostorage.util;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidDiscDrive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FluidStackList implements IDigitalFluidInventory, Iterable<FluidStack> {
    protected final ArrayList<FluidStack> contents;
    private final int maxFluidAmount = Integer.MAX_VALUE;
    private final int maxFluidStackSize = Integer.MAX_VALUE;

    public FluidStackList(ArrayList<FluidStack> contents) {
        this.contents = contents;
    }

    public FluidStackList() {
        this.contents = new ArrayList<>();
    }

    @Override
    public boolean add(FluidStack stack){
        if(stack == null){
            return false;
        }
        stack = stack.copy();
        int index = find(stack.liquid.id);
        if (index != -1) {
            if(sizeItems()+stack.amount <= getMaxFluidAmount()){
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                inventoryChanged();
                return true;
            }
        } else {
            if(sizeItems()+stack.amount <= getMaxFluidAmount() && sizeStacks()+1 <= getMaxFluidStackSize()){
                contents.add(stack);
                inventoryChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public FluidStack addAndReturnOverflow(FluidStack stack){
        if(stack == null){
            return null;
        }
        int index = find(stack.liquid.id);
        if (index != -1) {
            if(sizeItems()+stack.amount <= getMaxFluidAmount()){
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                inventoryChanged();
                return null;
            } else {
                int remainder = (sizeItems()+stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack(remainder);
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                inventoryChanged();
                return split;
            }
        } else {
            if(sizeItems()+stack.amount <= getMaxFluidAmount() && sizeStacks()+1 <= getMaxFluidStackSize()){
                contents.add(stack);
                inventoryChanged();
                return null;
            } else if (sizeItems() + stack.amount > getMaxFluidAmount()) {
                int remainder = (sizeItems()+stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack(remainder);
                contents.add(stack);
                inventoryChanged();
                return split;
            }
        }
        return stack;
    }

    @Override
    public boolean addAll(FluidStackList stacks) {
        boolean allSuccessful = true;
        ArrayList<FluidStack> toRemove = new ArrayList<>();
        for (FluidStack stack : stacks) {
            boolean success = add(stack);
            if (!success) {
                allSuccessful = false;
                continue;
            }
            toRemove.add(stack);
        }
        for (FluidStack stack : toRemove) {
            FluidStack removed = stacks.remove(stack.liquid.id, false);
            if(removed == null){
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }

    @Override
    public boolean addAll(List<FluidStack> stacks) {
        boolean allSuccessful = true;
        ArrayList<FluidStack> toRemove = new ArrayList<>();
        for (FluidStack stack : stacks) {
            boolean success = add(stack);
            if (!success) {
                allSuccessful = false;
            }
            toRemove.add(stack);
        }
        for (FluidStack stack : toRemove) {
            stacks.remove(stack);
        }
        return allSuccessful;
    }

    @Override
    public boolean canAdd(FluidStack stack){
        int index = find(stack.liquid.id);
        if (index != -1) {
            return sizeItems() + stack.amount <= getMaxFluidAmount();
        } else {
            return sizeItems() + stack.amount <= getMaxFluidAmount() && sizeStacks() + 1 <= getMaxFluidStackSize();
        }
    }

    @Override
    public void updateSizes(TileEntityFluidDiscDrive drive){

    }

    @Override
    public void resetSizes(){

    }

    @Override
    public int getMaxFluidAmount(){
        return maxFluidAmount;
    }

    @Override
    public int getMaxFluidStackSize(){
        return maxFluidStackSize;
    }

    @Override
    public int sizeStacks(){
        return contents.size();
    }

    @Override
    public int sizeItems(){
        return contents.stream().mapToInt((C)-> C.amount).sum();
    }

    //if strict is true, method returns null if amount is more than actually present
    @Override
    public FluidStack remove(int slot, int amount, boolean strict){
        if(slot >= contents.size()){
            return null;
        }
        FluidStack stack = contents.get(slot);
        if(stack == null) return null;
        if(strict && amount > stack.amount){
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.amount);
            FluidStack splitStack = stack.splitStack(amount);
            if(stack.amount <= 0){
                contents.remove(slot);
            }
            inventoryChanged();
            return splitStack;
        }
        return null;
    }

    @Override
    public FluidStack remove(int slot, boolean strict){
        if(slot >= contents.size()){
            return null;
        }
        FluidStack stack = contents.get(slot);
        if(stack == null) return null;
        return remove(slot,Integer.MAX_VALUE,strict);
    }

    @Override
    public boolean move(FluidStackList what, FluidStackList where, boolean strict){
        boolean allSuccessful = true;
        for (FluidStack stack : what) {
            FluidStack removed = remove(stack.liquid.id, stack.amount, strict);
            if(removed == null){
                allSuccessful = false;
                continue;
            }
            boolean success = where.add(removed);
            if(!success){
                allSuccessful = false;
                continue;
            }
            removed = what.remove(stack.liquid.id, stack.amount, strict);
            if(removed == null){
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }

    @Override
    public boolean move(List<FluidStack> what, FluidStackList where, boolean strict){
        boolean allSuccessful = true;
        for (FluidStack stack : what) {
            FluidStack removed = remove(stack.liquid.id, stack.amount, strict);
            if(removed == null){
                allSuccessful = false;
                continue;
            }
            boolean success = where.add(removed);
            if(!success){
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }

    @Override
    public boolean removeAll(List<FluidStack> stacks, boolean strict){
        for (FluidStack stack : stacks) {
            FluidStack removed = remove(stack.liquid.id, stack.amount, strict);
            if(removed == null){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<FluidStack> moveAll(List<FluidStack> stacks, boolean strict){
        ArrayList<FluidStack> list = new ArrayList<>();
        for (FluidStack stack : stacks) {
            FluidStack removed = remove(stack.liquid.id, stack.amount, strict);
            if(removed != null){
                list.add(removed);
            }
        }
        return list;
    }

    @Override
    public boolean contains(int id){
        return contents.stream().anyMatch((S)-> S.liquid.id == id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount){
        return contents.stream().anyMatch((S)-> S.liquid.id == id && S.amount >= amount);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks){
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.liquid.id,stack.amount);
            if(!contains) return false;
        }
        return true;
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.liquid.id,stack.amount);
            if(!contains) return false;
        }
        return true;
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks){
        ArrayList<FluidStack> missing = new ArrayList<>();
        for (FluidStack stack : stacks) {
            int c = count(stack.liquid.id);
            if(c <= 0){
                missing.add(stack.copy());
            } else if(c != stack.amount) {
                FluidStack copy = stack.copy();
                copy.amount -= c;
                missing.add(stack.copy());
            }
        }
        return missing;
    }

    @Override
    public int count(int id){
        return contents.stream().mapToInt((S)->{
            if(S.liquid.id == id) {
                return S.amount;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id){
        for (int i = 0; i < contents.size(); i++) {
            FluidStack content = contents.get(i);
            if(content.liquid.id == id){
                return i;
            }
        }
        return -1;
    }

    @Override
    public FluidStack get(int index){
        if(index < 0 || index >= contents.size()){
            return null;
        }
        return contents.get(index);
    }

    @Override
    public FluidStack getById(int id){
        return get(find(id));
    }

    @Override
    public FluidStack getLast(){
        return contents.get(contents.size()-1);
    }

    @Override
    public int getLastSlot(){
        return contents.size()-1;
    }

    @Override
    public void inventoryChanged(){
    }

    @Override
    public void clear(){
        contents.clear();
        inventoryChanged();
    }

    @Override
    public IDigitalFluidInventory copy() {
        FluidStackList inv = new FluidStackList();
        inv.contents.stream().map(FluidStack::copy).forEach(inv.contents::add);
        return inv;
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
}

