package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.IFluidStackList;
import sunsetsatellite.retrostorage.api.NetworkFluidStorage;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.item.FluidStorageDiscItem;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FluidDiscDriveBlockEntity extends NetworkDeviceBlockEntity implements ManagedItemHandlerWithInventory, NetworkFluidStorage {

    public ArrayList<ItemStack> discsUsed = new ArrayList<>();

    @Override
    public FluidStack add(FluidStack stack) {
        getStacks();
        if (!DiscManipulator.canSaveAllToFluidDiscs(discsUsed, networkStacks.getStacks())) return null;
        FluidStack added = networkStacks.add(stack);
        DiscManipulator.saveToFluidDiscs(discsUsed, networkStacks.getStacks());
        return added;
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        getStacks();
        if (!DiscManipulator.canSaveAllToFluidDiscs(discsUsed, networkStacks.getStacks())) return null;
        FluidStack added = networkStacks.add(index, stack);
        DiscManipulator.saveToFluidDiscs(discsUsed, networkStacks.getStacks());
        return added;
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(FluidStackList stacks) {
        return networkStacks.addAll(stacks);
    }

    @Override
    public @UnmodifiableView List<FluidStack> addAll(List<FluidStack> stacks) {
        return networkStacks.addAll(stacks);
    }

    @Override
    public FluidStack remove(int slot, int amount, boolean strict) {
        getStacks();
        if (!DiscManipulator.canSaveAllToFluidDiscs(discsUsed, networkStacks.getStacks())) return null;
        FluidStack remove = networkStacks.remove(slot, amount, strict);
        DiscManipulator.saveToFluidDiscs(discsUsed, networkStacks.getStacks());
        return remove;
    }

    @Override
    public FluidStack removeById(int id, int amount, boolean strict) {
        getStacks();
        if (!DiscManipulator.canSaveAllToFluidDiscs(discsUsed, networkStacks.getStacks())) return null;
        FluidStack removed = networkStacks.removeById(id, amount, strict);
        DiscManipulator.saveToFluidDiscs(discsUsed, networkStacks.getStacks());
        return removed;
    }

    @Override
    public FluidStack remove(int slot, boolean strict) {
        getStacks();
        if (!DiscManipulator.canSaveAllToFluidDiscs(discsUsed, networkStacks.getStacks())) return null;
        FluidStack removed = networkStacks.remove(slot, strict);
        DiscManipulator.saveToFluidDiscs(discsUsed, networkStacks.getStacks());
        return removed;
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(List<FluidStack> what, FluidStackList where, boolean strict) {
        return networkStacks.move(what, where, strict);
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict) {
        return networkStacks.move(what, where, strict);
    }

    @Override
    public boolean removeAll(List<FluidStack> stacks, boolean strict) {
        return networkStacks.removeAll(stacks, strict);
    }

    @Override
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        return networkStacks.exportAll(stacks, strict);
    }

    @Override
    public boolean contains(int id) {
        getStacks();
        return networkStacks.contains(id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
        getStacks();
        return networkStacks.containsAtLeast(id, amount);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks) {
        getStacks();
        return networkStacks.containsAtLeast(stacks);
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        getStacks();
        return networkStacks.containsAtLeast(stacks);
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks) {
        getStacks();
        return networkStacks.returnMissing(stacks);
    }

    @Override
    public Set<Fluid> getDisallowedFluids() {
        getStacks();
        return networkStacks.getDisallowedFluids();
    }

    @Override
    public int count(int id) {
        getStacks();
        return networkStacks.count(id);
    }

    @Override
    public int find(int id) {
        getStacks();
        return networkStacks.find(id);
    }

    @Override
    public FluidStack get(int index) {
        getStacks();
        return networkStacks.get(index);
    }

    @Override
    public FluidStack getById(int id) {
        getStacks();
        return networkStacks.getById(id);
    }

    @Override
    public FluidStack getLast() {
        getStacks();
        return networkStacks.getLast();
    }

    @Override
    public int getLastSlot() {
        getStacks();
        return networkStacks.getLastSlot();
    }

    public FluidStackList networkStacks;

    private int maxStacks = 0;
    private int maxFluidAmount = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public FluidDiscDriveBlockEntity() {
        addItemSlot();
        addItemSlot();
    }

    @Override
    public void tick() {
        if (world != null && world.isRemote) return;
        if (getStack(0) != null && discsUsed.size() < maxDiscs) {
            if (getStack(0).getItem() instanceof FluidStorageDiscItem disc) {
                maxStacks += disc.getMaxStackCapacity();
                maxFluidAmount += disc.getMaxItemCapacity();
                discsUsed.add(getStack(0));
                setStack(0, null);
            }
        }
    }

    public void removeLastDisc() {
        if (!discsUsed.isEmpty()) {
            ItemStack disc = discsUsed.get(0).copy();
            discsUsed.remove(0);
            maxStacks -= Math.min(maxStacks, ((FluidStorageDiscItem) disc.getItem()).getMaxStackCapacity());
            maxFluidAmount -= Math.min(maxFluidAmount, ((FluidStorageDiscItem) disc.getItem()).getMaxItemCapacity());
            disc.count = 1;
            setStack(1, disc);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList listTag = nbt.getList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag = (NbtCompound) listTag.get(i);
            discsUsed.add(new ItemStack(tag));
        }
        maxStacks = nbt.getInt("MaxStacks");
        maxFluidAmount = nbt.getInt("MaxItems");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtList listTag = new NbtList();
        for (ItemStack stack : discsUsed) {
            if (stack == null) continue;
            NbtCompound tag = new NbtCompound();
            stack.writeNbt(tag);
            listTag.add(tag);
        }
        nbt.put("DiscsUsed", listTag);
        nbt.putInt("MaxStacks", maxStacks);
        nbt.putInt("MaxItems", maxFluidAmount);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return canUse(player);
    }

    @Override
    public String getName() {
        return "container.retrostorage.fluidDiscDrive";
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
    public void inventoryChanged() {

    }

    @Override
    public int getMaxFluidAmount() {
        return maxFluidAmount;
    }

    @Override
    public int getMaxFluidStackSize() {
        return maxStacks;
    }

    @Override
    public int getFluidStackAmount() {
        getStacks();
        return networkStacks.getFluidStackAmount();
    }

    @Override
    public int getFluidAmount() {
        getStacks();
        return networkStacks.getFluidAmount();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IFluidStackList copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @UnmodifiableView List<FluidStack> getStacks() {
        ArrayList<FluidStack> stacks = Catalyst.condenseFluidList(DiscManipulator.viewFluidDiscs(discsUsed));
        networkStacks = new FluidStackList(stacks, getMaxFluidAmount(), getMaxFluidStackSize());
        return Collections.unmodifiableList(stacks);
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
