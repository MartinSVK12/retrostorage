package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.item.FluidStorageDiscItem;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.IFluidStackList;
import sunsetsatellite.retrostorage.util.NetworkFluidStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FluidDiscDriveBlockEntity extends NetworkDeviceBlockEntity
        implements Inventory, NetworkFluidStorage {

    private ItemStack[] discStorage;
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    private int maxStacks = 0;
    private int maxItems = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public FluidDiscDriveBlockEntity() {
        discStorage = new ItemStack[3];
    }

    public int size() {
        return discStorage.length;
    }

    public ItemStack getStack(int i) {
        return discStorage[i];
    }

    public ItemStack removeStack(int i, int j) {
        if (discStorage[i] != null) {
            if (discStorage[i].count <= j) {
                ItemStack itemstack = discStorage[i];
                discStorage[i] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = discStorage[i].split(j);
            if (discStorage[i].count == 0) {
                discStorage[i] = null;
            }
            markDirty();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void tick() {
        super.tick();
        if (getStack(0) != null && discsUsed.size() < maxDiscs) {
            if (getStack(0).getItem() instanceof FluidStorageDiscItem) {
                FluidStorageDiscItem item = (FluidStorageDiscItem) getStack(0).getItem();
                maxStacks += item.getMaxStackCapacity();
                maxItems += item.getMaxItemCapacity();
                ItemStack stack = getStack(0);
                discsUsed.add(stack.copy());
                setStack(0, null);
            }
        }
    }

    public void removeLastDisc() {
        if (!discsUsed.isEmpty()) {
            ItemStack disc = discsUsed.get(0).copy();
            discsUsed.remove(0);
            maxStacks -= Math.min(maxStacks, ((FluidStorageDiscItem) disc.getItem()).getMaxStackCapacity());
            maxItems -= Math.min(maxItems, ((FluidStorageDiscItem) disc.getItem()).getMaxItemCapacity());
            disc.count = 1;
            setStack(1, disc);
        }
    }

    public void setStack(int i, ItemStack itemstack) {
        discStorage[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();

    }

    public void markDirty() {
        super.markDirty();
    }

    public String getName() {
        return "Fluid Disc Drive";
    }

    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        NbtList listTag = compoundTag.getList("Items");
        discStorage = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound compoundTag1 = (NbtCompound) listTag.get(i);
            int j = compoundTag1.getByte("Slot") & 0xff;
            if (j < discStorage.length) {
                discStorage[j] = new ItemStack(compoundTag1);
            }
        }
        listTag = compoundTag.getList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            discsUsed.add(new ItemStack(tag1));
        }
        maxStacks = compoundTag.getInt("MaxStacks");
        maxItems = compoundTag.getInt("MaxItems");

        //backwards compatibility
        if (!discsUsed.isEmpty() && compoundTag.contains("Disc")) {
            NbtCompound tag = compoundTag.getCompound("Disc");
            discsUsed.get(0).getStationNbt().put("Disc",tag);
        }
    }

    public void writeNbt(NbtCompound compoundTag) {
        super.writeNbt(compoundTag);
        NbtList listTag = new NbtList();
        for (int i = 0; i < discStorage.length; i++) {
            if (discStorage[i] != null) {

                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                discStorage[i].writeNbt(tag1);
                listTag.add(tag1);
            }
        }
        compoundTag.put("Items", listTag);
        listTag = new NbtList();
        for (int i = 0; i < discsUsed.size(); i++) {
            if (discsUsed.get(i) != null) {
                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                discsUsed.get(i).writeNbt(tag1);
                listTag.add(tag1);
            }
        }
        compoundTag.put("DiscsUsed", listTag);
        compoundTag.put("MaxStacks", new NbtInt(maxStacks));
        compoundTag.put("MaxItems", new NbtInt(maxItems));
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        return super.canPlayerUse(entityplayer);
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
        List<FluidStack> contents = new ArrayList<>(getStacks());
        if (stack == null) {
            return null;
        }
        int index = find(stack.fluid.blockId());
        if (index != -1) {
            if ((getFluidAmount() + stack.amount > 0) && getFluidAmount() + stack.amount <= getMaxFluidAmount()) {
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);
                inventoryChanged();
                return null;
            } else {
                int remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.split(remainder);
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);
                inventoryChanged();
                return split;
            }
        } else {
            if ((getFluidAmount() + stack.amount > 0) && getFluidAmount() + stack.amount <= getMaxFluidAmount() && getFluidStackAmount() + 1 <= getMaxFluidStackSize()) {
                contents.add(stack);
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);;
                inventoryChanged();
                return null;
            } else if (getFluidAmount() + stack.amount > getMaxFluidAmount()) {
                int remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.split(remainder);
                contents.add(stack);
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);
                inventoryChanged();
                return split;
            }
        }
        return stack;
    }

    @Override
    public FluidStack add(int index, FluidStack stack) {
        List<FluidStack> contents = new ArrayList<>(getStacks());
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
                FluidStack split = stack.split((int) remainder);
                invStack.amount += stack.amount;
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);
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

        return Collections.unmodifiableList(MachineEssentials.condenseFluidList(newStacks));
    }

    @Override
    public int getMaxFluidAmount() {
        return maxItems;
    }

    @Override
    public int getMaxFluidStackSize() {
        return maxStacks;
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
        List<FluidStack> contents = new ArrayList<>(getStacks());
        if (slot >= contents.size()) {
            return null;
        }
        FluidStack stack = contents.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.amount) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.amount);
            FluidStack split = stack.split(amount);
            if (stack.amount <= 0) {
                contents.remove(slot);
            }
            DiscManipulator.saveToFluidDiscs(discsUsed,contents);
            inventoryChanged();
            return split;
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
        List<FluidStack> contents = getStacks();
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
            FluidStack removed = removeById(stack.fluid.blockId(), stack.amount, strict);
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
            FluidStack removed = remove(stack.fluid.blockId(),stack.amount,strict);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            FluidStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(MachineEssentials.condenseFluidList(leftovers));
    }

    @Override
    public @UnmodifiableView List<FluidStack> move(FluidStackList what, FluidStackList where, boolean strict) {
        return move(what.getStacks(),where,strict);
    }

    @Override
    public List<FluidStack> exportAll(List<FluidStack> stacks, boolean strict) {
        ArrayList<FluidStack> list = new ArrayList<>();
        for (FluidStack stack : stacks) {
            FluidStack removed = remove(stack.fluid.blockId(),stack.amount,strict);
            if (removed != null) {
                list.add(removed);
            }
        }
        return list;
    }

    @Override
    public boolean contains(int id) {
        List<FluidStack> contents = getStacks();
        return contents.stream().anyMatch((S) -> S.fluid.blockId() == id);
    }

    @Override
    public boolean containsAtLeast(int id, int amount) {
        List<FluidStack> contents = getStacks();
        return contents.stream().anyMatch((S) -> S.fluid.blockId() == id && S.amount >= amount);
    }

    @Override
    public boolean containsAtLeast(List<FluidStack> stacks) {
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.fluid.blockId(), stack.amount);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public boolean containsAtLeast(FluidStackList stacks) {
        for (FluidStack stack : stacks) {
            boolean contains = containsAtLeast(stack.fluid.blockId(), stack.amount);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public ArrayList<FluidStack> returnMissing(ArrayList<FluidStack> stacks) {
        ArrayList<FluidStack> missing = new ArrayList<>();
        for (FluidStack stack : stacks) {
            int c = count(stack.fluid.blockId());
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
    public Set<FluidType> getDisallowedFluids() {
        return RetroStorage.DISALLOWED_FLUIDS;
    }

    @Override
    public int count(int id) {
        List<FluidStack> contents = getStacks();
        return contents.stream().mapToInt((S) -> {
            if (S.fluid.blockId() == id) {
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
            if (content.fluid.blockId() == id) {
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
        return Collections.unmodifiableList(MachineEssentials.condenseFluidList(DiscManipulator.viewFluidDiscs(discsUsed)));
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
