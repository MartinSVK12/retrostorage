package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.MachineEssentials;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.item.StorageDiscItem;
import sunsetsatellite.retrostorage.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscDriveBlockEntity extends NetworkDeviceBlockEntity
        implements Inventory, NetworkItemStorage {

    private ItemStack[] contents;
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    private int maxStacks = 0;
    private int maxItems = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public DiscDriveBlockEntity() {
        contents = new ItemStack[3];
    }

    public int size() {
        return contents.length;
    }

    public ItemStack getStack(int i) {
        return contents[i];
    }

    public ItemStack removeStack(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].count <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].split(j);
            if (contents[i].count == 0) {
                contents[i] = null;
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
            if (getStack(0).getItem() instanceof StorageDiscItem) {
                StorageDiscItem item = (StorageDiscItem) getStack(0).getItem();
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
            maxStacks -= Math.min(maxStacks, ((StorageDiscItem) disc.getItem()).getMaxStackCapacity());
            maxItems -= Math.min(maxItems, ((StorageDiscItem) disc.getItem()).getMaxItemCapacity());
            disc.count = 1;
            setStack(1, disc);
        }
    }

    public void setStack(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();

    }

    public void markDirty() {
        super.markDirty();
    }

    public String getName() {
        return "Disc Drive";
    }

    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        NbtList listTag = compoundTag.getList("Items");
        contents = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound compoundTag1 = (NbtCompound) listTag.get(i);
            int j = compoundTag1.getByte("Slot") & 0xff;
            if (j < contents.length) {
                contents[j] = new ItemStack(compoundTag1);
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
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {

                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                contents[i].writeNbt(tag1);
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
    public ItemStack add(ItemStack stack) {
        ArrayList<ItemStack> list = new ArrayList<>(getStacks());
        if (stack == null || !DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return stack;
        }
        int index = find(stack.itemId, stack.getDamage(), stack.getStationNbt());
        if (index != -1) {
            ItemStack invStack = list.get(index);
            if (!invStack.getStationNbt().equals(stack.getStationNbt())) {
                index = -1;
            }
        }
        if (index != -1) {
            if (getAmount() + stack.count <= getItemCapacity()) {
                ItemStack invStack = list.get(index);
                invStack.count += stack.count;
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return null;
            } else {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ItemStack split = stack.split((int) remainder);
                ItemStack invStack = list.get(index);
                invStack.count += stack.count;
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return split;
            }
        } else {
            if (getAmount() + stack.count <= getItemCapacity() && getStackAmount() + 1 <= getStackCapacity()) {
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                list.add(stack);
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return null;
            } else if (getAmount() + stack.count > getItemCapacity()) {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                ItemStack split = stack.split((int) remainder);
                list.add(stack);
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return split;
            }
        }
        return stack;
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        ArrayList<ItemStack> list = new ArrayList<>(getStacks());
        if (stack == null || !DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return stack;
        }
        if(index >= list.size()) {
            return stack;
        }
        ItemStack invStack = list.get(index);
        if (invStack == null){
            list.add(index, stack);
            DiscManipulator.saveToDiscs(discsUsed,list);
            inventoryChanged();
            return null;
        } else if(invStack.isItemEqual(stack) && invStack.getStationNbt().equals(stack.getStationNbt())) {
            if (getAmount() + stack.count > getItemCapacity()) {
                long remainder = (getAmount() + stack.count) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).retrostorage$setUnlimited(true);
                ItemStack split = stack.split((int) remainder);
                invStack.count += stack.count;
                DiscManipulator.saveToDiscs(discsUsed,list);
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
        return maxItems;
    }

    @Override
    public long getStackCapacity() {
        return maxStacks;
    }

    @Override
    public long getStackAmount() {
        return getAmount() / 64;
    }

    @Override
    public long getAmount() {
        return getStacks().stream().mapToInt((S)->S.count).sum();
    }
    
    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        ArrayList<ItemStack> list = new ArrayList<>(getStacks());
        if (slot >= list.size() || !DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return null;
        }
        ItemStack stack = list.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.count) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.count);
            if (!unlimited) amount = Math.min(amount, stack.getItem().getMaxCount());
            ItemStack split = stack.split((int) amount);
            if (stack.count <= 0) {
                list.remove(slot);
            }
            DiscManipulator.saveToDiscs(discsUsed,list);
            inventoryChanged();
            return split;
        }
        return null;
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        List<ItemStack> list = getStacks();
        if (slot >= list.size() || DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return null;
        }
        ItemStack stack = list.get(slot);
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
        List<ItemStack> list = getStacks();
        return list.stream().anyMatch((S) -> S.itemId == id && S.getDamage() == meta);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, NbtCompound data, long amount) {
        List<ItemStack> list = getStacks();
        return list.stream().anyMatch((S) -> S.itemId == id && S.getDamage() == meta && S.count >= amount);
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
        List<ItemStack> list = getStacks();
        return list.stream().mapToInt((S) -> {
            if (S.itemId == id && S.getDamage() == meta) {
                return S.count;
            }
            return 0;
        }).sum();
    }

    @Override
    public long count(int id) {
        List<ItemStack> list = getStacks();
        return list.stream().mapToInt((S) -> {
            if (S.itemId == id) {
                return S.count;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id, int meta, NbtCompound data) {
        List<ItemStack> list = getStacks();
        for (int i = 0; i < list.size(); i++) {
            ItemStack content = list.get(i);
            if ((content.getDamage() == meta || meta == -1) && content.itemId == id) {
                if(content.getStationNbt().equals(data) || data == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ItemStack get(int index) {
        List<ItemStack> list = getStacks();
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, NbtCompound data) {
        return get(find(id, meta, data));
    }

    @Override
    public ItemStack getLast() {
        List<ItemStack> list = getStacks();
        return list.get(list.size() - 1);
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
        return Collections.unmodifiableList(MachineEssentials.condenseItemList(DiscManipulator.viewDiscs(discsUsed)));
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
