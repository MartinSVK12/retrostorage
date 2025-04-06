package sunsetsatellite.retrostorage.tiles;

import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.FluidInventoryWrapper;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.IFluidStackList;
import sunsetsatellite.retrostorage.util.INetworkFluidStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileEntityFluidStorageBus extends TileEntityNetworkDevice implements INetworkFluidStorage {

    public FluidInventoryWrapper wrapper = new FluidInventoryWrapper(null);
    private int priority = 0;

    @Override
    public void tick() {
        super.tick();
        wrapper.connected = getConnectedTileEntity(IFluidInventory.class);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public FluidStack add(FluidStack stack) {
        return wrapper.add(stack);
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        return wrapper.add(index, stack);
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(FluidStackList stacks) {
        return wrapper.addAll(stacks);
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks) {
        return wrapper.addAll(stacks);
    }

    @Override
    public int getMaxFluidAmount() {
        return wrapper.getMaxFluidAmount();
    }

    @Override
    public int getMaxFluidStackSize() {
        return wrapper.getMaxFluidStackSize();
    }

    @Override
    public int getFluidStackAmount() {
        return wrapper.getFluidStackAmount();
    }

    @Override
    public int getFluidAmount() {
        return wrapper.getFluidAmount();
    }

    @Override
    public FluidStack remove(int slot, int amount, boolean strict) {
        return wrapper.remove(slot, amount, strict);
    }

    @Override
    public FluidStack removeById(int id, int amount, boolean strict) {
        return wrapper.removeById(id, amount, strict);
    }

    @Override
    public FluidStack remove(int slot, boolean strict) {
        return wrapper.remove(slot, strict);
    }

    @Override
    public boolean removeAll(List<FluidStack> stacks, boolean strict) {
        return wrapper.removeAll(stacks, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict) {
        return wrapper.move(what, where, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict) {
        return wrapper.move(what, where, strict);
    }

    @Override
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        return wrapper.exportAll(stacks, strict);
    }

    @Override
    public boolean contains(int id) {
        return wrapper.contains(id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
        return wrapper.containsAtLeast(id, amount);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks) {
        return wrapper.containsAtLeast(stacks);
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        return wrapper.containsAtLeast(stacks);
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks) {
        return wrapper.returnMissing(stacks);
    }

    @Override
    public Set<Fluid> getDisallowedFluids() {
        return wrapper.getDisallowedFluids();
    }

    @Override
    public int count(int id) {
        return wrapper.count(id);
    }

    @Override
    public int find(int id) {
        return wrapper.find(id);
    }

    @Override
    public FluidStack get(int index) {
        return wrapper.get(index);
    }

    @Override
    public FluidStack getById(int id) {
        return wrapper.getById(id);
    }

    @Override
    public FluidStack getLast() {
        return wrapper.getLast();
    }

    @Override
    public int getLastSlot() {
        return wrapper.getLastSlot();
    }

    @Override
    public void inventoryChanged() {
        wrapper.inventoryChanged();
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    @Override
    public IFluidStackList copy() {
        return wrapper.copy();
    }

    @Override
    public @UnmodifiableView List<FluidStack> getStacks() {
        return wrapper.getStacks();
    }

    @Override
    public boolean isEmpty() {
        return wrapper.isEmpty();
    }
}
