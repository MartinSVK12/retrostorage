package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.block.FluidHandler;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.io.FluidInventoryWrapper;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.IFluidStackList;
import sunsetsatellite.retrostorage.api.NetworkFluidStorage;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class FluidStorageBusBlockEntity extends NetworkDeviceBlockEntity implements NetworkFluidStorage, ScreenActionListener {
    public FluidInventoryWrapper inventory = new FluidInventoryWrapper(null);
    public int priority = 0;

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public @UnmodifiableView List<FluidStack> getStacks() {
        return inventory.getStacks();
    }

    @Override
    public IFluidStackList copy() {
        return inventory.copy();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void inventoryChanged() {
        inventory.inventoryChanged();
    }

    @Override
    public int getLastSlot() {
        return inventory.getLastSlot();
    }

    @Override
    public FluidStack getLast() {
        return inventory.getLast();
    }

    @Override
    public FluidStack getById(int id) {
        return inventory.getById(id);
    }

    @Override
    public FluidStack get(int index) {
        return inventory.get(index);
    }

    @Override
    public int find(int id) {
        return inventory.find(id);
    }

    @Override
    public int count(int id) {
        return inventory.count(id);
    }

    @Override
    public Set<Fluid> getDisallowedFluids() {
        return inventory.getDisallowedFluids();
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks) {
        return inventory.returnMissing(stacks);
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        return inventory.containsAtLeast(stacks);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks) {
        return inventory.containsAtLeast(stacks);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
        return inventory.containsAtLeast(id, amount);
    }

    @Override
    public boolean contains(int id) {
        return inventory.contains(id);
    }

    @Override
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        return inventory.exportAll(stacks, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict) {
        return inventory.move(what, where, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict) {
        return inventory.move(what, where, strict);
    }

    public FluidStack removeUntil(int id, int amount, boolean strict) {
        return inventory.removeUntil(id, amount, strict);
    }

    @Override
    public boolean removeAll(List<FluidStack> stacks, boolean strict) {
        return inventory.removeAll(stacks, strict);
    }

    @Override
    public FluidStack remove(int slot, boolean strict) {
        return inventory.remove(slot, strict);
    }

    @Override
    public FluidStack removeById(int id, int amount, boolean strict) {
        return inventory.removeById(id, amount, strict);
    }

    @Override
    public FluidStack remove(int slot, int amount, boolean strict) {
        return inventory.remove(slot, amount, strict);
    }

    @Override
    public int getFluidAmount() {
        return inventory.getFluidAmount();
    }

    @Override
    public int getFluidStackAmount() {
        return inventory.getFluidStackAmount();
    }

    @Override
    public int getMaxFluidStackSize() {
        return inventory.getMaxFluidStackSize();
    }

    @Override
    public int getMaxFluidAmount() {
        return inventory.getMaxFluidAmount();
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks) {
        return inventory.addAll(stacks);
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(FluidStackList stacks) {
        return inventory.addAll(stacks);
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        return inventory.add(index, stack);
    }

    @Override
    public FluidStack add(FluidStack stack) {
        return inventory.add(stack);
    }

    @Override
    public String getName() {
        return "container.retrostorage.fluidStorageBus";
    }

    @Override
    public void tick() {
        super.tick();
        int side = world.getBlockState(x, y, z).get(HORIZONTAL_FACING).getOpposite().getId();
        BlockEntity blockEntity = Direction.getDirectionFromSide(side).getTileEntity(world, this);
        if (blockEntity instanceof FluidHandler inv) {
            inventory.connected = inv;
        } else {
            inventory.connected = null;
        }
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
    public void buttonClicked(int id, int button, int channel) {
        if (id == 0) {
            priority += 1;
        } else if (id == 1) {
            priority -= 1;
        }
    }
}
