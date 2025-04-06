package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.IItemStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.UnlimitedItemStack;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.INetworkItemStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileEntityDiscDrive extends TileEntityNetworkDevice
        implements Container, INetworkItemStorage {

    private ItemStack[] contents;
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    private int maxStacks = 0;
    private int maxItems = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public TileEntityDiscDrive() {
        contents = new ItemStack[3];
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    @Override
    public ItemStack getItem(int i) {
        return contents[i];
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].stackSize <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                setChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if (contents[i].stackSize == 0) {
                contents[i] = null;
            }
            setChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        if (worldObj != null && worldObj.isClientSide) return;
        if (getItem(0) != null && discsUsed.size() < maxDiscs) {
            if (getItem(0).getItem() instanceof ItemStorageDisc) {
                ItemStorageDisc item = (ItemStorageDisc) getItem(0).getItem();
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
            maxStacks -= Math.min(maxStacks, ((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
            maxItems -= Math.min(maxItems, ((ItemStorageDisc) disc.getItem()).getMaxItemCapacity());
            disc.stackSize = 1;
            setItem(1, disc);
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.discDrive";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readFromNBT(CompoundTag compoundTag) {
        super.readFromNBT(compoundTag);
        ListTag listTag = compoundTag.getList("Items");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag compoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = compoundTag1.getByte("Slot") & 0xff;
            if (j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(compoundTag1);
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
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {

                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(CompoundTag1);
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
    public ItemStack add(ItemStack stack) {
        ArrayList<ItemStack> list = new ArrayList<>(getStacks());
        if (stack == null || !DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return stack;
        }
        int index = find(stack.itemID, stack.getMetadata(), stack.getData());
        if (index != -1) {
            ItemStack invStack = list.get(index);
            if (!invStack.getData().equals(stack.getData())) {
                index = -1;
            }
        }
        if (index != -1) {
            if (getAmount() + stack.stackSize <= getItemCapacity()) {
                ItemStack invStack = list.get(index);
                invStack.stackSize += stack.stackSize;
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return null;
            } else {
                long remainder = (getAmount() + stack.stackSize) - getItemCapacity();
                ItemStack split = stack.splitStack((int) remainder);
                ItemStack invStack = list.get(index);
                invStack.stackSize += stack.stackSize;
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return split;
            }
        } else {
            if (getAmount() + stack.stackSize <= getItemCapacity() && getStackAmount() + 1 <= getStackCapacity()) {
                ((UnlimitedItemStack) (Object) stack).setUnlimited(true);
                list.add(stack);
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return null;
            } else if (getAmount() + stack.stackSize > getItemCapacity()) {
                long remainder = (getAmount() + stack.stackSize) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).setUnlimited(true);
                ItemStack split = stack.splitStack((int) remainder);
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
        } else if(invStack.isItemEqual(stack) && invStack.getData().equals(stack.getData())) {
            if (getAmount() + stack.stackSize > getItemCapacity()) {
                long remainder = (getAmount() + stack.stackSize) - getItemCapacity();
                ((UnlimitedItemStack) (Object) stack).setUnlimited(true);
                ItemStack split = stack.splitStack((int) remainder);
                invStack.stackSize += stack.stackSize;
                DiscManipulator.saveToDiscs(discsUsed,list);
                inventoryChanged();
                return split.stackSize <= 0 ? null : split;
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

        return Collections.unmodifiableList(Catalyst.condenseItemList(newStacks));
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
        return getStacks().stream().mapToInt((S)->S.stackSize).sum();
    }
    
    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        ArrayList<ItemStack> list = new ArrayList<>(getStacks());
        if (slot >= list.size() || !DiscManipulator.canSaveAllToDiscs(discsUsed,list)) {
            return null;
        }
        ItemStack stack = list.get(slot);
        if (stack == null) return null;
        if (strict && amount > stack.stackSize) {
            return null;
        } else if (!strict) {
            amount = Math.min(amount, stack.stackSize);
            if (!unlimited) amount = Math.min(amount, stack.getItem().getItemStackLimit(stack));
            ItemStack splitStack = stack.splitStack((int) amount);
            if (stack.stackSize <= 0) {
                list.remove(slot);
            }
            DiscManipulator.saveToDiscs(discsUsed,list);
            inventoryChanged();
            return splitStack;
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
        return remove(slot, stack.getItem().getItemStackLimit(stack), strict, unlimited);
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
        return Collections.unmodifiableList(Catalyst.condenseItemList(leftovers));
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
        List<ItemStack> list = getStacks();
        return list.stream().anyMatch((S) -> S.itemID == id && S.getMetadata() == meta);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, CompoundTag data, long amount) {
        List<ItemStack> list = getStacks();
        return list.stream().anyMatch((S) -> S.itemID == id && S.getMetadata() == meta && S.stackSize >= amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            boolean contains = containsAtLeast(stack.itemID, stack.getMetadata(), stack.getData(), stack.stackSize);
            if (!contains) return false;
        }
        return true;
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        for (ItemStack stack : stacks) {
            boolean contains = containsAtLeast(stack.itemID, stack.getMetadata(), stack.getData(), stack.stackSize);
            if (!contains) return false;
        }
        return true;
    }

    @Override
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
        List<ItemStack> list = getStacks();
        return list.stream().mapToInt((S) -> {
            if (S.itemID == id && S.getMetadata() == meta) {
                return S.stackSize;
            }
            return 0;
        }).sum();
    }

    @Override
    public long count(int id) {
        List<ItemStack> list = getStacks();
        return list.stream().mapToInt((S) -> {
            if (S.itemID == id) {
                return S.stackSize;
            }
            return 0;
        }).sum();
    }

    @Override
    public int find(int id, int meta, CompoundTag data) {
        List<ItemStack> list = getStacks();
        for (int i = 0; i < list.size(); i++) {
            ItemStack content = list.get(i);
            if ((content.getMetadata() == meta || meta == -1) && content.itemID == id) {
                if(content.getData().equals(data) || data == null) {
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
    public ItemStack get(int id, int meta, CompoundTag data) {
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
        return Collections.unmodifiableList(Catalyst.condenseItemList(DiscManipulator.viewDiscs(discsUsed)));
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }
}
