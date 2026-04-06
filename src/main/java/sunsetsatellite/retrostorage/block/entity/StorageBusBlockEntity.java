package sunsetsatellite.retrostorage.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.io.IItemStackList;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.api.AttachesToMachines;
import sunsetsatellite.retrostorage.api.NetworkItemStorage;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

import java.util.ArrayList;
import java.util.List;

import static net.modificationstation.stationapi.api.state.property.Properties.FACING;

public class StorageBusBlockEntity extends NetworkDeviceBlockEntity implements NetworkItemStorage, ScreenActionListener, AttachesToMachines {

    @Override
    public ItemStack add(ItemStack stack) {
        return inventory.add(stack);
    }

    @Override
    public ItemStack add(int index, ItemStack stack) {
        return inventory.add(index, stack);
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(ItemStackList stacks) {
        return inventory.addAll(stacks);
    }

    @Override
    public @UnmodifiableView List<ItemStack> addAll(List<ItemStack> stacks) {
        return inventory.addAll(stacks);
    }

    @Override
    public long getItemCapacity() {
        return inventory.getItemCapacity();
    }

    @Override
    public long getStackCapacity() {
        return inventory.getStackCapacity();
    }

    @Override
    public long getStackAmount() {
        return inventory.getStackAmount();
    }

    @Override
    public long getAmount() {
        return inventory.getAmount();
    }

    @Override
    public ItemStack remove(int slot, long amount, boolean strict, boolean unlimited) {
        return inventory.remove(slot, amount, strict, unlimited);
    }

    @Override
    public ItemStack remove(int slot, boolean strict, boolean unlimited) {
        return inventory.remove(slot, strict, unlimited);
    }

    @Override
    public ItemStack remove(int id, int meta, long amount, NbtCompound data, boolean strict, boolean unlimited) {
        return inventory.remove(id, meta, amount, data, strict, unlimited);
    }

    public ItemStack removeUntil(int id, int meta, long amount, NbtCompound data, boolean strict, boolean unlimited) {
        return inventory.removeUntil(id, meta, amount, data, strict, unlimited);
    }

    @Override
    public boolean removeAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return inventory.removeAll(stacks, strict, unlimited);
    }

    @Override
    public @UnmodifiableView List<ItemStack> move(ItemStackList what, ItemStackList where, boolean strict) {
        return inventory.move(what, where, strict);
    }

    @Override
    public @UnmodifiableView List<ItemStack> move(List<ItemStack> what, ItemStackList where, boolean strict) {
        return inventory.move(what, where, strict);
    }

    @Override
    public List<ItemStack> exportAll(List<ItemStack> stacks, boolean strict, boolean unlimited) {
        return inventory.exportAll(stacks, strict, unlimited);
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int slot, long amount, boolean strict) {
        return inventory.eject(world, x, y, z, slot, amount, strict);
    }

    @Override
    public boolean eject(World world, int x, int y, int z, int id, int meta, NbtCompound data, long amount, boolean strict) {
        return inventory.eject(world, x, y, z, id, meta, data, amount, strict);
    }

    @Override
    public void ejectAll(World world, int x, int y, int z) {
        inventory.ejectAll(world, x, y, z);
    }

    @Override
    public boolean contains(int id, int meta, NbtCompound data) {
        return inventory.contains(id, meta, data);
    }

    @Override
    public boolean containsAtLeast(int id, int meta, NbtCompound data, long amount) {
        return inventory.containsAtLeast(id, meta, data, amount);
    }

    @Override
    public boolean containsAtLeast(List<ItemStack> comparedTo) {
        return inventory.containsAtLeast(comparedTo);
    }

    @Override
    public boolean containsAtLeast(ItemStackList stacks) {
        return inventory.containsAtLeast(stacks);
    }

    @Override
    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        return inventory.returnMissing(stacks);
    }

    @Override
    public long count(int id, int meta, NbtCompound data) {
        return inventory.count(id, meta, data);
    }

    @Override
    public long count(int id) {
        return inventory.count(id);
    }

    @Override
    public int find(int id, int meta, NbtCompound data) {
        return inventory.find(id, meta, data);
    }

    @Override
    public ItemStack get(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack get(int id, int meta, NbtCompound data) {
        return inventory.get(id, meta, data);
    }

    @Override
    public ItemStack getLast() {
        return inventory.getLast();
    }

    @Override
    public void inventoryChanged() {
        inventory.inventoryChanged();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public IItemStackList copy() {
        return inventory.copy();
    }

    @Override
    public @UnmodifiableView List<ItemStack> getStacks() {
        return inventory.getStacks();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public InventoryWrapper inventory = new InventoryWrapper(null);
    public int priority = 0;

    @Override
    public String getName() {
        return "container.retrostorage.storageBus";
    }

    @Override
    public void tick() {
        super.tick();
        int side = world.getBlockState(x, y, z).get(FACING).getId();
        BlockEntity blockEntity = Direction.getDirectionFromSide(side).getTileEntity(world, this);
        if (blockEntity instanceof Inventory inv) {
            inventory.connected = inv;
        } else {
            inventory.connected = null;
        }
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
    public void buttonClicked(int id, int button, int channel) {
        if (id == 0) {
            priority += 1;
        } else if (id == 1) {
            priority -= 1;
        }
    }

    @Override
    public BlockEntity getAttachedMachine() {
        if(inventory != null && inventory.connected instanceof BlockEntity tile){
            return tile;
        }
        return null;
    }
}
