package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemFluidStorageDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.catalyst.fluids.util.IFluidStackList;
import sunsetsatellite.retrostorage.util.INetworkFluidStorage;

import java.util.*;

public class TileEntityFluidDiscDrive extends TileEntityNetworkDevice
        implements Container, INetworkFluidStorage {

    private ItemStack[] discStorage;
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    private int maxStacks = 0;
    private int maxItems = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public TileEntityFluidDiscDrive() {
        discStorage = new ItemStack[3];
    }

    @Override
    public int getContainerSize() {
        return discStorage.length;
    }

    @Override
    public ItemStack getItem(int i) {
        return discStorage[i];
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (discStorage[i] != null) {
            if (discStorage[i].stackSize <= j) {
                ItemStack itemstack = discStorage[i];
                discStorage[i] = null;
                setChanged();
                return itemstack;
            }
            ItemStack itemstack1 = discStorage[i].splitStack(j);
            if (discStorage[i].stackSize == 0) {
                discStorage[i] = null;
            }
            setChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        if (getItem(0) != null && discsUsed.size() < maxDiscs) {
            if (getItem(0).getItem() instanceof ItemFluidStorageDisc) {
                ItemFluidStorageDisc item = (ItemFluidStorageDisc) getItem(0).getItem();
                maxStacks += item.getMaxStackCapacity();
                maxItems += item.getMaxItemCapacity();
                ItemStack stack = getItem(0);
                discsUsed.add(stack.copy());
                setItem(0, null);
            }
        }
    }

    public void removeLastDisc() {
        if (!discsUsed.isEmpty()) {
            ItemStack disc = discsUsed.get(0).copy();
            discsUsed.remove(0);
            maxStacks -= Math.min(maxStacks, ((ItemFluidStorageDisc) disc.getItem()).getMaxStackCapacity());
            maxItems -= Math.min(maxItems, ((ItemFluidStorageDisc) disc.getItem()).getMaxItemCapacity());
            disc.stackSize = 1;
            setItem(1, disc);
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        discStorage[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.fluidDiscDirve";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readFromNBT(CompoundTag compoundTag) {
        super.readFromNBT(compoundTag);
        ListTag listTag = compoundTag.getList("Items");
        discStorage = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag compoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = compoundTag1.getByte("Slot") & 0xff;
            if (j < discStorage.length) {
                discStorage[j] = ItemStack.readItemStackFromNbt(compoundTag1);
            }
        }
        listTag = compoundTag.getList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            discsUsed.add(ItemStack.readItemStackFromNbt(CompoundTag1));
        }
        maxStacks = compoundTag.getInteger("MaxStacks");
        maxItems = compoundTag.getInteger("MaxItems");

        //backwards compatibility
        if (!discsUsed.isEmpty() && compoundTag.containsKey("Disc")) {
            CompoundTag tag = compoundTag.getCompound("Disc");
            discsUsed.get(0).getData().putCompound("Disc",tag);
        }
    }

    @Override
    public void writeToNBT(CompoundTag compoundTag) {
        super.writeToNBT(compoundTag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < discStorage.length; i++) {
            if (discStorage[i] != null) {

                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                discStorage[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }
        compoundTag.put("Items", listTag);
        listTag = new ListTag();
        for (int i = 0; i < discsUsed.size(); i++) {
            if (discsUsed.get(i) != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                discsUsed.get(i).writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }
        compoundTag.put("DiscsUsed", listTag);
        compoundTag.put("MaxStacks", new IntTag(maxStacks));
        compoundTag.put("MaxItems", new IntTag(maxItems));
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if (worldObj.getTileEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

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
        int index = find(stack.fluid.getFirstId());
        if (index != -1) {
            if ((getFluidAmount() + stack.amount > 0) && getFluidAmount() + stack.amount <= getMaxFluidAmount()) {
                FluidStack invStack = contents.get(index);
                invStack.amount += stack.amount;
                DiscManipulator.saveToFluidDiscs(discsUsed,contents);
                inventoryChanged();
                return null;
            } else {
                int remainder = (getFluidAmount() + stack.amount) - getMaxFluidAmount();
                FluidStack split = stack.splitStack(remainder);
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
                FluidStack split = stack.splitStack(remainder);
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
                FluidStack split = stack.splitStack((int) remainder);
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

        return Collections.unmodifiableList(RetroStorage.condenseFluidList(newStacks));
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
            FluidStack splitStack = stack.splitStack(amount);
            if (stack.amount <= 0) {
                contents.remove(slot);
            }
            DiscManipulator.saveToFluidDiscs(discsUsed,contents);
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
        return RetroStorage.DISALLOWED_FLUIDS;
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
        return Collections.unmodifiableList(RetroStorage.condenseFluidList(DiscManipulator.viewFluidDiscs(discsUsed)));
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
