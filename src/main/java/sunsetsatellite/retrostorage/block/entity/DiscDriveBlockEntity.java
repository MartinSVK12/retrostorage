package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.io.IItemStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.api.NetworkItemStorage;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.item.StorageDiscItem;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscDriveBlockEntity extends NetworkDeviceBlockEntity implements ManagedItemHandlerWithInventory, NetworkItemStorage, BlockEntityInit, ScreenActionListener {

    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    public ItemStackList networkStacks;

    private int maxStacks = 0;
    private int maxItems = 0;
    public int maxDiscs = 16;
    private int priority = 0;

    public DiscDriveBlockEntity() {
        addItemSlot();
        addItemSlot();
    }

    @Override
    public void init(BlockState blockState) {

    }

    @Override
    public void tick() {
        if (world != null && world.isRemote) return;
        if (getStack(0) != null && discsUsed.size() < maxDiscs) {
            if (getStack(0).getItem() instanceof StorageDiscItem disc) {
                maxStacks += disc.getMaxStackCapacity();
                maxItems += disc.getMaxItemCapacity();
                discsUsed.add(getStack(0));
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
        maxItems = nbt.getInt("MaxItems");
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
        nbt.putInt("MaxItems", maxItems);
    }

    @Override
    public int size() {
        return getItemSlots(null);
    }

    @Override
    public ItemStack getStack(int slot) {
        return getItem(slot, null);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return extractItem(slot, amount, null);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        setItem(stack, slot, null);
    }

    @Override
    public String getName() {
        return "container.retrostorage.discDrive";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return super.canUse(player);
    }

    public ItemStack add(ItemStack stack) {
        getStacks();
        if (!DiscManipulator.canSaveAllToDiscs(discsUsed, networkStacks.getStacks())) return stack;
        ItemStack remainder = networkStacks.add(stack);
        DiscManipulator.saveToDiscs(discsUsed, networkStacks.getStacks());
        return remainder;
    }

    public ItemStack add(int index, ItemStack stack) {
        getStacks();
        if (!DiscManipulator.canSaveAllToDiscs(discsUsed, networkStacks.getStacks())) return stack;
        ItemStack remainder = networkStacks.add(index, stack);
        DiscManipulator.saveToDiscs(discsUsed, networkStacks.getStacks());
        return remainder;
    }

    public boolean contains(int id, int meta, NbtCompound data) {
        getStacks();
        return networkStacks.contains(id, meta, data);
    }

    public boolean containsAtLeast(List<ItemStack> stacks) {
        getStacks();
        return networkStacks.containsAtLeast(stacks);
    }

    public void inventoryChanged() {
        networkStacks.inventoryChanged();
    }

    public boolean containsAtLeast(ItemStackList stacks) {
        getStacks();
        return networkStacks.containsAtLeast(stacks);
    }

    @UnmodifiableView
    public List<ItemStack> addAll(ItemStackList stacks) {
        return networkStacks.addAll(stacks);
    }

    @UnmodifiableView
    public List<ItemStack> addAll(List<ItemStack> stacks) {
        return networkStacks.addAll(stacks);
    }

    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        getStacks();
        if (!DiscManipulator.canSaveAllToDiscs(discsUsed, networkStacks.getStacks())) return null;
        ItemStack removed = networkStacks.remove(slot, amount, strict, unlimited);
        DiscManipulator.saveToDiscs(discsUsed, networkStacks.getStacks());
        return removed;
    }

    public boolean eject(World world, int x, int y, int z, int slot, long amount, boolean strict) {
        return networkStacks.eject(world, x, y, z, slot, amount, strict);
    }

    public long count(int id) {
        getStacks();
        return networkStacks.count(id);
    }

    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        getStacks();
        if (!DiscManipulator.canSaveAllToDiscs(discsUsed, networkStacks.getStacks())) return null;
        ItemStack removed = networkStacks.remove(slot, strict, unlimited);
        DiscManipulator.saveToDiscs(discsUsed, networkStacks.getStacks());
        return removed;
    }

    @UnmodifiableView
    public List<ItemStack> move(ItemStackList what, ItemStackList where, boolean strict) {
        return networkStacks.move(what, where, strict);
    }

    public boolean containsAtLeast(int id, int meta, NbtCompound data, long amount) {
        getStacks();
        return networkStacks.containsAtLeast(id, meta, data, amount);
    }

    public boolean eject(World world, int x, int y, int z, int id, int meta, NbtCompound data, long amount, boolean strict) {
        return networkStacks.eject(world, x, y, z, id, meta, data, amount, strict);
    }

    public ItemStack getLast() {
        getStacks();
        return networkStacks.getLast();
    }

    @UnmodifiableView
    public List<ItemStack> move(List<ItemStack> what, ItemStackList where, boolean strict) {
        return networkStacks.move(what, where, strict);
    }

    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        return networkStacks.returnMissing(stacks);
    }

    public ItemStack get(int index) {
        getStacks();
        return networkStacks.get(index);
    }

    public ItemStack remove(int id, int meta, long amount, NbtCompound data, boolean strict, boolean unlimited) {
        getStacks();
        if (!DiscManipulator.canSaveAllToDiscs(discsUsed, networkStacks.getStacks())) return null;
        ItemStack removed = networkStacks.remove(id, meta, amount, data, strict, unlimited);
        DiscManipulator.saveToDiscs(discsUsed, networkStacks.getStacks());
        return removed;
    }

    public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return networkStacks.removeAll(stacks, strict, unlimited);
    }

    public void ejectAll(World world, int x, int y, int z) {
        networkStacks.ejectAll(world, x, y, z);
    }

    public int find(int id, int meta, NbtCompound data) {
        getStacks();
        return networkStacks.find(id, meta, data);
    }

    public long count(int id, int meta, NbtCompound data) {
        getStacks();
        return networkStacks.count(id, meta, data);
    }

    public ItemStack get(int id, int meta, NbtCompound data) {
        getStacks();
        return networkStacks.get(id, meta, data);
    }

    public List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return networkStacks.exportAll(stacks, strict, unlimited);
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
        return getStacks().stream().mapToInt((S) -> S.count).sum();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IItemStackList copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @UnmodifiableView List<ItemStack> getStacks() {
        ArrayList<ItemStack> stacks = Catalyst.condenseItemList(DiscManipulator.viewDiscs(discsUsed));
        networkStacks = new ItemStackList(stacks, getItemCapacity(), getStackCapacity());
        return Collections.unmodifiableList(stacks);
    }

    @Override
    public boolean isEmpty() {
        return getStacks().isEmpty();
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 0) {
            if (getStack(1) == null) {
                removeLastDisc();
            }
        }
    }
}
