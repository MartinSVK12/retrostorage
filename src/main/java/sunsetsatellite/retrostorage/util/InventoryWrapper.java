package sunsetsatellite.retrostorage.util;


import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.MachineEssentials;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InventoryWrapper implements IItemStackList {

    public Inventory connected;

    public InventoryWrapper(Inventory inventory) {
        connected = inventory;
    }

    @Override
    public ItemStack add(ItemStack stack) {
        if(stack == null || connected == null) return stack;

        int n = stack.count;

        for (int i = 0; i < connected.size(); i++) {
            ItemStack invStack = connected.getStack(i);
            if(invStack == null) {
                int amount = Math.min(stack.count, stack.getMaxCount());
                n -= amount;
                connected.setStack(i, stack.split(amount));
                if(n <= 0) break;
            } else if(invStack.isItemEqual(stack)) {
                int remaining = Math.min(n,invStack.getMaxCount() - invStack.count);
                n -= remaining;
                invStack.count += remaining;
                if(n <= 0) break;
            }
        }

        if(n <= 0){
            return null;
        }
        ;
        ItemStack result = ItemStack.clone(stack);
        result.count = n;
        return result;
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        if(stack == null || connected == null) return stack;

        ItemStack invStack = connected.getStack(index);
        if(invStack == null) {
            ItemStack split = stack.split(Math.min(stack.count,stack.getMaxCount()));
            connected.setStack(index, split);
            return stack.count <= 0 ? null : stack;
        } else if(invStack.isItemEqual(stack)) {
            int remaining = Math.min(stack.count,invStack.getMaxCount() - invStack.count);
            ItemStack split = stack.split(remaining);
            invStack.count += split.count;
            return  stack.count <= 0 ? null : stack;
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

        return Collections.unmodifiableList(MachineEssentials.condenseItemList(newStacks));
    }

    @Override
    public long getItemCapacity() {
        return connected != null ? (long) connected.size() * connected.getMaxCountPerStack() : 0;
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
        return MachineEssentials.collectAndCondenseStacks(connected).stream().filter(Objects::nonNull).mapToInt((S)->S.count).sum();
    }

    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        if(connected == null) return null;
        List<ItemStack> stacks = MachineEssentials.collectStacks(connected);
        if (slot >= stacks.size()) {
            return null;
        }
        ItemStack stack = stacks.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.count) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.count);
            if (!unlimited) amount = Math.min(amount, stack.getItem().getMaxCount());
            ItemStack split = stack.split((int) amount);
            if (stack.count <= 0) {
                connected.setStack(slot,null);
            }
            inventoryChanged();
            return split;
        }
        return null;
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        List<ItemStack> stacks = MachineEssentials.collectStacks(connected);
        if (slot >= stacks.size()) {
            return null;
        }
        ItemStack stack = stacks.get(slot);
        if (stack == null) return null;
        return remove(slot, stack.getItem().getMaxCount(), strict, unlimited);
    }

    @Override
    public ItemStack remove(int id, int meta, long amount, NbtCompound data, boolean strict, boolean unlimited) {
        int index = find(id, meta, data);
        if (index != -1) {
            return remove(index, amount, strict, unlimited);
        }
        return null;
    }

    @Override
    public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        for (ItemStack stack : stacks) {
            ItemStack removed = remove(stack.itemId, stack.getDamage(), stack.count, stack.getStationNbt(), strict, unlimited);
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
            ItemStack removed = remove(stack.itemId, stack.getDamage(), stack.count, stack.getStationNbt(), strict, true);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            ItemStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(MachineEssentials.condenseItemList(leftovers));
    }

    @Override
    public List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : stacks) {
            ItemStack removed = remove(stack.itemId, stack.getDamage(), stack.count, stack.getStationNbt(), strict, unlimited);
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
            float f = world.random.nextFloat() * 0.8F + 0.1F;
            float f1 = world.random.nextFloat() * 0.8F + 0.1F;
            float f2 = world.random.nextFloat() * 0.8F + 0.1F;
            ItemEntity entityitem = new ItemEntity(world, (float) x + f, (float) y + f1, (float) z + f2, content);
            float f3 = 0.05F;
            entityitem.velocityX = (float) world.random.nextGaussian() * f3;
            entityitem.velocityY = (float) world.random.nextGaussian() * f3 + 0.2F;
            entityitem.velocityZ = (float) world.random.nextGaussian() * f3;
            world.spawnEntity(entityitem);
            inventoryChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int id, int meta, NbtCompound data, long amount, boolean strict) {
        ItemStack content = remove(id, meta, amount, data, strict, false);
        if (content != null) {
            float f = world.random.nextFloat() * 0.8F + 0.1F;
            float f1 = world.random.nextFloat() * 0.8F + 0.1F;
            float f2 = world.random.nextFloat() * 0.8F + 0.1F;
            ItemEntity entityitem = new ItemEntity(world, (float) x + f, (float) y + f1, (float) z + f2, content);
            float f3 = 0.05F;
            entityitem.velocityX = (float) world.random.nextGaussian() * f3;
            entityitem.velocityY = (float) world.random.nextGaussian() * f3 + 0.2F;
            entityitem.velocityZ = (float) world.random.nextGaussian() * f3;
            world.spawnEntity(entityitem);
            inventoryChanged();
            return true;
        }
        return false;
    }

    @Override
    public void ejectAll(World world, int x, int y, int z) {
        for (ItemStack content : getStacks()) {
            if(content == null) continue;
            eject(world,x,y,z,content.itemId,content.getDamage(),content.getStationNbt(),content.count,false);
        }
    }

    @Override
    public boolean contains(int id, int meta, NbtCompound data) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().anyMatch(stack -> stack.itemId == id && stack.getDamage() == id);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, NbtCompound data, long amount) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().anyMatch((stack) -> stack.itemId == id && stack.getDamage() == id && stack.count >= amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> comparedTo) {
        List<ItemStack> networkItems = getStacks();
        return networkItems.stream().filter(Objects::nonNull)
                .anyMatch((networkStack)->comparedTo.stream().filter(Objects::nonNull)
                        .anyMatch((comparedToStack) -> networkStack.isItemEqual(comparedToStack) && networkStack.count >= comparedToStack.count));
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        return containsAtLeast(stacks.getStacks());
    }

    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        ArrayList<ItemStack> missing = new ArrayList<>();
        for (ItemStack stack : stacks) {
            long c = count(stack.itemId, stack.getDamage(), stack.getStationNbt());
            if (c <= 0) {
                missing.add(stack.copy());
            } else if (c != stack.count) {
                ItemStack copy = stack.copy();
                copy.count -= (int) c;
                missing.add(stack.copy());
            }
        }
        return missing;
    }

    @Override
    public long count(int id, int meta, NbtCompound data) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().filter((S)->S.itemId == id && S.getDamage() == meta).mapToInt((S)->S.count).sum();
    }

    @Override
    public long count(int id) {
        List<ItemStack> stacks = getStacks();
        return stacks.stream().filter((S)->S.itemId == id).mapToInt((S)->S.count).sum();
    }


    @Override
    public int find(int id, int meta, NbtCompound data) {
        List<ItemStack> stacks = MachineEssentials.collectStacks(connected);
        for (int i = 0; i < stacks.size(); i++) {
            if(stacks.get(i) == null) continue;
            if(stacks.get(i).itemId == id && (stacks.get(i).getDamage() == meta || meta == -1)) {
                if(stacks.get(i).getStationNbt().equals(data) || data == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ItemStack get(int index) {
        List<ItemStack> stacks = MachineEssentials.collectStacks(connected);
        if (index < 0 || index >= stacks.size()) {
            return null;
        }
        return stacks.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, NbtCompound data) {
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
        return MachineEssentials.collectAndCondenseStacks(connected);
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }

}
