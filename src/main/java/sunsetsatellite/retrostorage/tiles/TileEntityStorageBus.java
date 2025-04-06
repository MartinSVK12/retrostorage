package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.core.util.io.IItemStackList;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.util.INetworkItemStorage;

import java.util.ArrayList;
import java.util.List;

public class TileEntityStorageBus extends TileEntityNetworkDevice implements INetworkItemStorage {

    public InventoryWrapper wrapper = new InventoryWrapper(null);
    private int priority = 0;

    @Override
    public void tick() {
        super.tick();
        wrapper.connected = getConnectedTileEntity(Container.class);
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
        return wrapper.add(stack);
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        return wrapper.add(index, stack);
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(ItemStackList stacks) {
        return wrapper.addAll(stacks);
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(List<ItemStack> stacks) {
        return wrapper.addAll(stacks);
    }

    @Override
    public long getItemCapacity() {
        return wrapper.getItemCapacity();
    }

    @Override
    public long getStackCapacity() {
        return wrapper.getStackCapacity();
    }

    @Override
    public long getStackAmount() {
        return wrapper.getStackAmount();
    }

    @Override
    public long getAmount() {
        return wrapper.getAmount();
    }

    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        return wrapper.remove(slot, amount, strict, unlimited);
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        return wrapper.remove(slot, strict, unlimited);
    }

    @Override
    public ItemStack remove(int id, int meta, long amount, CompoundTag data, boolean strict, boolean unlimited) {
        return wrapper.remove(id, meta, amount, data, strict, unlimited);
    }

    @Override
    public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return wrapper.removeAll(stacks, strict, unlimited);
    }

    @Override
    public List<ItemStack> move(ItemStackList what, ItemStackList where, boolean strict) {
        return wrapper.move(what, where, strict);
    }

    @Override
    public List<ItemStack> move(List<ItemStack> what, ItemStackList where, boolean strict) {
        return wrapper.move(what, where, strict);
    }

    @Override
    public List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return wrapper.exportAll(stacks, strict, unlimited);
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int slot, long amount, boolean strict) {
        return wrapper.eject(world, x, y, z, slot, amount, strict);
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int id, int meta, CompoundTag data, long amount, boolean strict) {
        return wrapper.eject(world, x, y, z, id, meta, data, amount, strict);
    }

    @Override
    public void ejectAll(World world, int x, int y, int z) {
        wrapper.ejectAll(world, x, y, z);
    }

    @Override
    public boolean contains(int id, int meta, CompoundTag data) {
        return wrapper.contains(id, meta, data);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, CompoundTag data, long amount) {
        return wrapper.containsAtLeast(id, meta, data, amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> comparedTo) {
        return wrapper.containsAtLeast(comparedTo);
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        return wrapper.containsAtLeast(stacks);
    }

    @Override
    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        return wrapper.returnMissing(stacks);
    }

    @Override
    public long count(int id, int meta, CompoundTag data) {
        return wrapper.count(id, meta, data);
    }

    @Override
    public long count(int id) {
        return wrapper.count(id);
    }

    @Override
    public int find(int id, int meta, CompoundTag data) {
        return wrapper.find(id, meta, data);
    }

    @Override
    public ItemStack get(int index) {
        return wrapper.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, CompoundTag data) {
        return wrapper.get(id, meta, data);
    }

    @Override
    public ItemStack getLast() {
        return wrapper.getLast();
    }

    @Override
    public void inventoryChanged() {
        wrapper.inventoryChanged();
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    @Override
    public IItemStackList copy() {
        return wrapper.copy();
    }

    @Override
    public @UnmodifiableView List<ItemStack> getStacks() {
        return wrapper.getStacks();
    }

    @Override
    public boolean isEmpty() {
        return wrapper.isEmpty();
    }
}
