package sunsetsatellite.retrostorage.util;


import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.MachineEssentials;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

@SuppressWarnings({"UnreachableCode", "RedundantSuppression"})
public class ItemStackList implements IItemStackList, Iterable<ItemStack> {
    protected final ArrayList<ItemStack> contents;
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxItemSize = Integer.MAX_VALUE;
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxcount = Integer.MAX_VALUE;

    public ItemStackList() {
        contents = new ArrayList<>();
    }

    public ItemStackList(List<ItemStack> contents) {
        this.contents = new ArrayList<>(contents);
    }

    @Override
    public ItemStack add(ItemStack stack) {
        if (stack == null) {
            return stack;
        }
        int index = find(stack.itemId, stack.getDamage(), stack.getStationNbt());
        if (index != -1) {
            ItemStack invStack = contents.get(index);
            if (!invStack.getStationNbt().equals(stack.getStationNbt())) {
                index = -1;
            }
        }
        if (index != -1) {
            if (getAmount() + stack.count <= getItemCapacity()) {
                ItemStack invStack = contents.get(index);
                invStack.count += stack.count;
                inventoryChanged();
                return null;
            } else {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ItemStack split = stack.split((int) remainder);
                ItemStack invStack = contents.get(index);
                invStack.count += stack.count;
                inventoryChanged();
                return split;
            }
        } else {
            if (getAmount() + stack.count <= getItemCapacity() && getStackAmount() + 1 <= getStackCapacity()) {
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                contents.add(stack);
                inventoryChanged();
                return null;
            } else if (getAmount() + stack.count > getItemCapacity()) {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                ItemStack split = stack.split((int) remainder);
                contents.add(stack);
                inventoryChanged();
                return split;
            }
        }
        return stack;
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        if(index >= contents.size()) {
            return stack;
        }
        ItemStack invStack = contents.get(index);
        if (invStack == null){
            contents.add(index, stack);
            inventoryChanged();
            return null;
        } else if(invStack.isItemEqual(stack) && invStack.getStationNbt().equals(stack.getStationNbt())) {
            if (getAmount() + stack.count > getItemCapacity()) {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                ItemStack split = stack.split((int) remainder);
                invStack.count += stack.count;
                inventoryChanged();
                return split.count <= 0 ? null : split;
            }
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
        return maxItemSize;
    }

    @Override
    public long getStackCapacity() {
        return maxcount;
    }

    @Override
    public long getStackAmount() {
        return contents.stream().filter(Objects::nonNull).count();
    }

    @Override
    public long getAmount() {
        return contents.stream().mapToInt((C) -> C.count).sum();
    }

    //if strict is true, method returns null if amount is more than actually present
    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        if (slot >= contents.size()) {
            return null;
        }
        ItemStack stack = contents.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.count) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.count);
            if (!unlimited) amount = Math.min(amount, stack.getItem().getMaxCount());
            ItemStack split = stack.split((int) amount);
            if (stack.count <= 0) {
                contents.remove(slot);
            }
            inventoryChanged();
            return split;
        }
        return null;
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        if (slot >= contents.size()) {
            return null;
        }
        ItemStack stack = contents.get(slot);
        if (stack == null) return null;
        return remove(slot, stack.getItem().getMaxCount(), strict, unlimited);
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
        return contents.stream().anyMatch((S) -> S.itemId == id && S.getDamage() == meta);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, NbtCompound data, long amount) {
        return contents.stream().anyMatch((S) -> S.itemId == id && S.getDamage() == meta && S.count >= amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            boolean contains = containsAtLeast(stack.itemId, stack.getDamage(), stack.getStationNbt(), stack.count);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        for (ItemStack stack : stacks) {
            boolean contains = containsAtLeast(stack.itemId, stack.getDamage(), stack.getStationNbt(), stack.count);
            if (!contains) return false;
        }
        return true;
    }

    @Override
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
        return contents.stream().mapToInt((S) -> {
            if (S.itemId == id && S.getDamage() == meta) {
                return S.count;
            }
            return 0;
        }).sum();
    }

    @Override
    public long count(int id) {
        return contents.stream().mapToInt((S) -> {
            if (S.itemId == id) {
                return S.count;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id, int meta, NbtCompound data) {
        for (int i = 0; i < contents.size(); i++) {
            ItemStack content = contents.get(i);
            if ((content.getDamage() == meta || meta == -1) && content.itemId == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ItemStack get(int index) {
        if (index < 0 || index >= contents.size()) {
            return null;
        }
        return contents.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, NbtCompound data) {
        return get(find(id, meta, data));
    }

    @Override
    public ItemStack getLast() {
        return contents.get(contents.size() - 1);
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
    public IItemStackList copy() {
        ItemStackList inv = new ItemStackList();
        inv.contents.stream().map(ItemStack::copy).forEach(inv.contents::add);
        return inv;
    }

    @Override
    public List<ItemStack> getStacks() {
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
    public Iterator<ItemStack> iterator() {
        return contents.iterator();
    }
}
