package sunsetsatellite.retrostorage.util;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InventoryWrapper implements IItemStackList {

    public IInventory connected;

    public InventoryWrapper(IInventory inventory) {
        connected = inventory;
    }

    @Override
    public ItemStack add(ItemStack stack) {
        if(stack == null || connected == null) return stack;

        int n = stack.stackSize;

        for (int i = 0; i < connected.getSizeInventory(); i++) {
            ItemStack invStack = connected.getStackInSlot(i);
            if(invStack == null) {
                int amount = Math.min(stack.stackSize, stack.getMaxStackSize(connected));
                n -= amount;
                connected.setInventorySlotContents(i, stack.splitStack(amount));
                if(n <= 0) break;
            } else if(invStack.isItemEqual(stack)) {
                int remaining = Math.min(n,invStack.getMaxStackSize(connected) - invStack.stackSize);
                n -= remaining;
                invStack.stackSize += remaining;
            }
        }

        if(n <= 0){
            return null;
        }

        return new ItemStack(stack.itemID,n,stack.getMetadata(),stack.getData());
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        if(stack == null || connected == null) return stack;

        ItemStack invStack = connected.getStackInSlot(index);
        if(invStack == null) {
            ItemStack split = stack.splitStack(Math.min(stack.stackSize,stack.getMaxStackSize(connected)));
            connected.setInventorySlotContents(index, split);
            return stack.stackSize <= 0 ? null : stack;
        } else if(invStack.isItemEqual(stack)) {
            int remaining = Math.min(stack.stackSize,invStack.getMaxStackSize(connected) - invStack.stackSize);
            ItemStack split = stack.splitStack(remaining);
            invStack.stackSize += split.stackSize;
            return  stack.stackSize <= 0 ? null : stack;
        }
        return stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(ItemStackList stacks) {
        return addAll(stacks.getStacks());
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(List<ItemStack> stacks) {
        ArrayList<ItemStack> newStacks = new ArrayList<>();

        for (ItemStack stack : stacks) {
            newStacks.add(add(stack));
        }

        return Collections.unmodifiableList(RetroStorage.condenseItemList(newStacks));
    }

    /*@Override
    public boolean addAll(ItemStackList stacks) {
        boolean allSuccessful = true;
        ArrayList<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack stack : stacks) {
            boolean success = add(stack);
            if (!success) {
                allSuccessful = false;
                continue;
            }
            toRemove.add(stack);
        }
        for (ItemStack stack : toRemove) {
            ItemStack removed = stacks.remove(stack.itemID, stack.getMetadata(), false, true);
            if (removed == null) {
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }

    @Override
    public boolean addAll(List<ItemStack> stacks) {
        boolean allSuccessful = true;
        ArrayList<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack stack : stacks) {
            boolean success = add(stack);
            if (!success) {
                allSuccessful = false;
            }
            toRemove.add(stack);
        }
        for (ItemStack stack : toRemove) {
            stacks.remove(stack);
        }
        return allSuccessful;
    }

    @Override
    public boolean canAdd(ItemStack stack) {
        if(stack == null || connected == null) return false;
        int n = stack.stackSize;
        ItemStack[] stacks = RetroStorage.collectStacks(connected).toArray(new ItemStack[connected.getSizeInventory()]);
        for (ItemStack invStack : stacks) {
            if(invStack == null) {
                n -= stack.getMaxStackSize(connected);
            } else if(invStack.isItemEqual(stack)) {
                n -= invStack.getMaxStackSize(connected) - invStack.stackSize;
            }
        }
        return n <= 0;
    }

    @Override
    public boolean add(ItemStack stack) {
        if(stack == null || connected == null) return false;

        int n = stack.stackSize;

        if (!canAdd(stack)) {
            return false;
        }

        for (int i = 0; i < connected.getSizeInventory(); i++) {
            ItemStack invStack = connected.getStackInSlot(i);
            if(invStack == null) {
                n -= stack.getMaxStackSize(connected);
                connected.setInventorySlotContents(i, stack);
            } else if(invStack.isItemEqual(stack)) {
                int remaining = Math.min(n,invStack.getMaxStackSize(connected) - invStack.stackSize);
                n -= remaining;
                stack.stackSize += remaining;
            }
        }

        return n == 0;
    }*/

    @Override
    public long getItemCapacity() {
        return connected != null ? (long) connected.getSizeInventory() * connected.getInventoryStackLimit() : 0;
    }

    @Override
    public long getStackCapacity() {
        return getItemCapacity() / 64;
    }

    @Override
    public long getStackAmount() {
        return getAmount() / 64;
    }

    @Override
    public long getAmount() {
        return RetroStorage.collectAndCondenseStacks(connected).stream().filter(Objects::nonNull).mapToInt((S)->S.stackSize).sum();
    }

    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        if(connected == null) return null;
        List<ItemStack> stacks = RetroStorage.collectStacks(connected);
        if (slot >= stacks.size()) {
            return null;
        }
        ItemStack stack = stacks.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.stackSize) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.stackSize);
            if (!unlimited) amount = Math.min(amount, stack.getItem().getItemStackLimit());
            ItemStack splitStack = stack.splitStack((int) amount);
            if (stack.stackSize <= 0) {
                connected.setInventorySlotContents(slot,null);
            }
            inventoryChanged();
            return splitStack;
        }
        return null;
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        List<ItemStack> stacks = RetroStorage.collectStacks(connected);
        if (slot >= stacks.size()) {
            return null;
        }
        ItemStack stack = stacks.get(slot);
        if (stack == null) return null;
        return remove(slot, stack.getItem().getItemStackLimit(), strict, unlimited);
    }

    @Override
    public ItemStack remove(int id, int meta, long amount, CompoundTag data, boolean strict, boolean unlimited) {
        int index = find(id, meta, data);
        if (index != -1) {
            return remove(index, amount, strict, unlimited);
        }
        return null;
    }

    @Override
    public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        for (ItemStack stack : stacks) {
            ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, stack.getData(), strict, unlimited);
            if (removed == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @UnmodifiableView List<ItemStack> move(ItemStackList what, ItemStackList where, boolean strict) {
        return move(what.getStacks(),where,strict);
    }

    @Override
    public @UnmodifiableView List<ItemStack> move(List<ItemStack> what, ItemStackList where, boolean strict) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();

        for (ItemStack stack : what) {
            ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, stack.getData(), strict, true);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            ItemStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(RetroStorage.condenseItemList(leftovers));
    }

    @Override
    public List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : stacks) {
            ItemStack removed = remove(stack.itemID, stack.getMetadata(), stack.stackSize, stack.getData(), strict, unlimited);
            if (removed != null) {
                list.add(removed);
            }
        }
        return list;
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int slot, long amount, boolean strict) {
        ItemStack content = remove(slot, amount, strict, false);
        if (content != null) {
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, content);
            float f3 = 0.05F;
            entityitem.xd = (float) world.rand.nextGaussian() * f3;
            entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
            inventoryChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int id, int meta, CompoundTag data, long amount, boolean strict) {
        ItemStack content = remove(id, meta, amount, data, strict, false);
        if (content != null) {
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, content);
            float f3 = 0.05F;
            entityitem.xd = (float) world.rand.nextGaussian() * f3;
            entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
            inventoryChanged();
            return true;
        }
        return false;
    }

    @Override
    public void ejectAll(World world, int x, int y, int z) {
        for (ItemStack content : getStacks()) {
            if(content == null) continue;
            eject(world,x,y,z,content.itemID,content.getMetadata(),content.getData(),content.stackSize,false);
        }
    }

    @Override
    public boolean contains(int id, int meta, CompoundTag data) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().anyMatch(stack -> stack.itemID == id && stack.getMetadata() == id);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, CompoundTag data, long amount) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().anyMatch((stack) -> stack.itemID == id && stack.getMetadata() == id && stack.stackSize >= amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> comparedTo) {
        List<ItemStack> networkItems = getStacks();
        return networkItems.stream().filter(Objects::nonNull)
                .anyMatch((networkStack)->comparedTo.stream().filter(Objects::nonNull)
                        .anyMatch((comparedToStack) -> networkStack.isItemEqual(comparedToStack) && networkStack.stackSize >= comparedToStack.stackSize));
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        return containsAtLeast(stacks.getStacks());
    }

    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        ArrayList<ItemStack> missing = new ArrayList<>();
        for (ItemStack stack : stacks) {
            long c = count(stack.itemID, stack.getMetadata(), stack.getData());
            if (c <= 0) {
                missing.add(stack.copy());
            } else if (c != stack.stackSize) {
                ItemStack copy = stack.copy();
                copy.stackSize -= (int) c;
                missing.add(stack.copy());
            }
        }
        return missing;
    }

    @Override
    public long count(int id, int meta, CompoundTag data) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().filter((S)->S.itemID == id && S.getMetadata() == meta).mapToInt((S)->S.stackSize).sum();
    }

    @Override
    public long count(int id) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().filter((S)->S.itemID == id).mapToInt((S)->S.stackSize).sum();
    }


    @Override
    public int find(int id, int meta, CompoundTag data) {
        List<ItemStack> stacks = RetroStorage.collectStacks(connected);
        for (int i = 0; i < stacks.size(); i++) {
            if(stacks.get(i) == null) continue;
            if(stacks.get(i).itemID == id && stacks.get(i).getMetadata() == meta) {
                if(stacks.get(i).getData().equals(data) || data == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ItemStack get(int index) {
        List<ItemStack> stacks = RetroStorage.collectStacks(connected);
        if (index < 0 || index >= stacks.size()) {
            return null;
        }
        return stacks.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, CompoundTag data) {
        return get(find(id, meta, data));
    }

    @Override
    public ItemStack getLast() {
        return getStacks().get(getStacks().size() - 1);
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
    public IItemStackList copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @UnmodifiableView List<ItemStack> getStacks() {
        return RetroStorage.collectAndCondenseStacks(connected);
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }

}
