package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.energy.EnergyHandler;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.*;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.packet.RequestControllerContentsUpdatePacket;
import sunsetsatellite.retrostorage.packet.RequestControllerCraftingQueuePacket;
import sunsetsatellite.retrostorage.packet.RequestControllerUpdatePacket;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestCraftingPacket;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;
import java.util.stream.Collectors;

public class DigitalControllerBlockEntity extends NetworkDeviceBlockEntity implements NetworkController {

    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasks = new ArrayList<>();

    public EnergyHandler externalEnergy;

    public double energy = 0;
    public boolean active = false;

    public DigitalControllerBlockEntity() {
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void tick() {
        super.tick();
        //energy = Integer.MAX_VALUE;
        if (world != null && world.isRemote) return;
        externalEnergy = getConnectedTileEntity(EnergyHandler.class);
        if (externalEnergy != null) {
            long draw = getEnergyConsumption();
            if (externalEnergy.getEnergyStored() >= draw) {
                externalEnergy.removeEnergy((int) draw);
                active = true;
            } else {
                active = false;
            }
        } else {
            long draw = getEnergyConsumption();
            if (energy >= draw) {
                energy -= draw;
                active = true;
            } else {
                active = false;
            }
        }
        if (active) {
            processCraftingTasks();
        }
    }

    public long energyConsumptionCache = 0;

    @Override
    public long getEnergyConsumption() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return energyConsumptionCache;
        }
        return getAttachedFluidStorage().size() + getAttachedStorage().size() + getProcessors().size() + getCoprocessors().size();
    }

    public ArrayDeque<CraftingTask> requestQueueCache = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasksCache = new ArrayList<>();

    @Override
    public ArrayDeque<CraftingTask> getRequestQueue() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerCraftingQueuePacket(x, y, z));
            return requestQueueCache;
        }
        return requestQueue;
    }

    @Override
    public ArrayList<CraftingTask> getCurrentTasks() {
        if (world != null && world.isRemote) {
            return currentTasksCache;
        }
        return currentTasks;
    }

    @Override
    public void clearRequestQueue() {
        if (world != null && world.isRemote) return;
        requestQueue.clear();
        for (CraftingTask task : currentTasks) {
            task.onCancelled();
        }
        for (Processor processor : getProcessors()) {
            processor.setFocus(null, null);
        }
        currentTasks.clear();
    }

    @Override
    public void processCraftingTasks() {
        if (world != null && world.isRemote) return;
        if (currentTasks.size() < getCoprocessors().size() + 1) {
            for (CraftingTask task : requestQueue) {
                if (!task.isStarted()) {
                    currentTasks.add(task);
                    task.start();
                    break;
                }
            }
        }
        if (!currentTasks.isEmpty()) {
            ArrayList<CraftingTask> removing = new ArrayList<>();
            for (CraftingTask task : currentTasks) {
                if (task.update()) {
                    requestQueue.remove(task);
                    removing.add(task);
                }
            }
            for (CraftingTask task : removing) {
                currentTasks.remove(task);
            }
        }
    }

    @Override
    public @UnmodifiableView Set<NetworkItemStorage> getAttachedStorage() {
        if (world != null && world.isRemote) return new HashSet<>();
        if (network == null) return new HashSet<>();
        HashSet<NetworkItemStorage> set = new HashSet<>(network.search(NetworkItemStorage.class));
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<NetworkFluidStorage> getAttachedFluidStorage() {
        if (world != null && world.isRemote) return new HashSet<>();
        if (network == null) return new HashSet<>();
        HashSet<NetworkFluidStorage> set = new HashSet<>(network.search(NetworkFluidStorage.class));
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<Coprocessor> getCoprocessors() {
        if (world != null && world.isRemote) return new HashSet<>();
        if (network == null) return new HashSet<>();
        HashSet<Coprocessor> set = new HashSet<>(network.search(Coprocessor.class));
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<Processor> getProcessors() {
        if (world != null && world.isRemote) return new HashSet<>();
        if (network == null) return new HashSet<>();
        HashSet<Processor> set = new HashSet<>(network.search(Processor.class));
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView List<NetworkCraftable> getCraftables() {
        if (world != null && world.isRemote) return new ArrayList<>();
        if (network == null) return new ArrayList<>();
        ArrayList<NetworkCraftable> list = new ArrayList<>();
        for (Processor processor : getProcessors()) {
            list.addAll(processor.getCraftables());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView Map<Processor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap() {
        if (world != null && world.isRemote) return new HashMap<>();
        if (network == null) return new HashMap<>();
        HashMap<Processor, @UnmodifiableView List<NetworkCraftable>> map = new HashMap<>();
        for (Processor processor : getProcessors()) {
            map.put(processor, processor.getCraftables());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void requestCrafting(CraftingTask task) {
        if (task != null) {
            RetroStorage.LOGGER.debug("Requesting: " + task.getCraftable().getOutput());
            if (world != null && world.isRemote) {
                PacketHelper.send(new RequestCraftingPacket(x, y, z, task));
                return;
            }
            requestQueue.add(task);
        }
    }

    @Override
    public Processor findProcessor(NetworkCraftable craftable) {
        if (world != null && world.isRemote) return null;
        ArrayList<Processor> instances = new ArrayList<>(getProcessors());

        for (Processor processor : instances) {
            if (processor.getCraftables().contains(craftable)) {
                return processor;
            }
        }
        return null;
    }

    @Override
    public Processor findProcessorWithNode(ProcessNode node) {
        if (world != null && world.isRemote) return null;
        for (Processor processor : getProcessors()) {
            if (processor.getWorkingNode() == node) {
                return processor;
            }
        }
        return null;
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerContentsUpdatePacket(x, y, z));
            return itemCache;
        }
        return getAllItems(RetroStorage::sortById);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerContentsUpdatePacket(x, y, z));
            return fluidCache;
        }
        return getAllFluids(RetroStorage::sortByIdFluid);
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction) {
        if (world != null && world.isRemote) return new ArrayList<>();
        if (network == null) return new ArrayList<>();
        ArrayList<ItemStack> list = new ArrayList<>();
        for (NetworkItemStorage storage : getAttachedStorage()) {
            list.addAll(storage.getStacks());
        }
        List<ItemStack> sorted = Catalyst.condenseItemList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<NetworkItemStorage, @UnmodifiableView List<ItemStack>> getItemMap() {
        if (world != null && world.isRemote) return new HashMap<>();
        if (network == null) return new HashMap<>();
        HashMap<NetworkItemStorage, @UnmodifiableView List<ItemStack>> map = new HashMap<>();
        for (NetworkItemStorage storage : getAttachedStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids(Comparator<? super FluidStack> sortingFunction) {
        if (world != null && world.isRemote) return new ArrayList<>();
        if (network == null) return new ArrayList<>();
        ArrayList<FluidStack> list = new ArrayList<>();
        for (NetworkFluidStorage storage : getAttachedFluidStorage()) {
            list.addAll(storage.getStacks());
        }
        List<FluidStack> sorted = Catalyst.condenseFluidList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<NetworkFluidStorage, @UnmodifiableView List<FluidStack>> getFluidMap() {
        if (network == null) return new HashMap<>();
        HashMap<NetworkFluidStorage, @UnmodifiableView List<FluidStack>> map = new HashMap<>();
        for (NetworkFluidStorage storage : getAttachedFluidStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    public void readNbt(NbtCompound NbtCompound) {
        super.readNbt(NbtCompound);
        energy = NbtCompound.getDouble("Energy");
        active = NbtCompound.getBoolean("Active");
    }

    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtDouble nbt = new NbtDouble(energy);
        tag.put("Energy", nbt);
        tag.putBoolean("Active", active);
    }

    public long itemCapacityCache = 0;
    public long itemStackCapacityCache = 0;
    public long itemAmountCache = 0;
    public long itemStackAmountCache = 0;
    public long fluidCapacityCache = 0;
    public long fluidStackCapacityCache = 0;
    public long fluidAmountCache = 0;
    public long fluidStackAmountCache = 0;
    public @UnmodifiableView List<ItemStack> itemCache = Collections.unmodifiableList(new ArrayList<>());
    public @UnmodifiableView List<FluidStack> fluidCache = Collections.unmodifiableList(new ArrayList<>());

    @Override
    public long getItemCapacity() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return itemCapacityCache;
        }
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getItemCapacity).sum();
    }


    @Override
    public long getStackCapacity() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return itemStackCapacityCache;
        }
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getStackCapacity).sum();
    }


    @Override
    public long getStackAmount() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return itemStackAmountCache;
        }
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getStackAmount).sum();
    }


    @Override
    public long getAmount() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return itemAmountCache;
        }
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getAmount).sum();
    }

    @Override
    public long getFluidCapacity() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return fluidCapacityCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getMaxFluidAmount).sum();
    }


    @Override
    public long getFluidStackCapacity() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return fluidStackCapacityCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getMaxFluidStackSize).sum();
    }


    @Override
    public long getFluidStackAmount() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return fluidStackAmountCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getFluidStackAmount).sum();
    }


    @Override
    public long getFluidAmount() {
        if (world != null && world.isRemote) {
            PacketHelper.send(new RequestControllerUpdatePacket(x, y, z));
            return fluidAmountCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getFluidAmount).sum();
    }

    @Override
    public ItemStack addItemToNetwork(ItemStack stack) {
        if (world != null && world.isRemote) return stack;

        List<NetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(NetworkItemStorage::getPriority)));

        for (NetworkItemStorage nas : storages) {
            if (stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> addItemsToNetwork(List<ItemStack> stacks) {
        if (world != null && world.isRemote) return stacks;
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack stack : stacks) {
            leftovers.add(addItemToNetwork(stack));
        }
        return Collections.unmodifiableList(Catalyst.condenseItemList(leftovers));
    }

    @Override
    public ItemStack removeItemFromNetwork(int id, int meta, NbtCompound data, long amount) {
        if (id == 0) return null;
        if (world != null && world.isRemote) return null;

        ItemStack stack = new ItemStack(id, 0, meta);
        if (data == null) data = new NbtCompound();
        StationNBTSetter.cast(stack).setStationNbt(data);

        long remaining = amount;

        List<NetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(NetworkItemStorage::getPriority)));

        for (NetworkItemStorage nas : storages) {
            ItemStack removed = nas.remove(id, meta, remaining, data, false, true);
            if (removed != null) {
                stack.count += removed.count;
                remaining -= removed.count;
                if (stack.count >= amount) break;
            }
        }

        return stack.count == 0 ? null : stack;
    }

    @Override
    public FluidStack addFluidToNetwork(FluidStack stack) {
        if (world != null && world.isRemote) return stack;
        List<NetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(NetworkFluidStorage::getPriority)));

        for (NetworkFluidStorage nas : storages) {
            if (stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks) {
        if (world != null && world.isRemote) return stacks;
        ArrayList<FluidStack> leftovers = new ArrayList<>();
        for (FluidStack stack : stacks) {
            leftovers.add(addFluidToNetwork(stack));
        }
        return Collections.unmodifiableList(Catalyst.condenseFluidList(leftovers));
    }

    @Override
    public FluidStack removeFluidFromNetwork(int id, long amount) {
        if (id == 0) return null;
        if (world != null && world.isRemote) return null;

        FluidStack stack = new FluidStack(FluidRegistry.get(id), 0);

        long remaining = amount;

        List<NetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(NetworkFluidStorage::getPriority)));

        for (NetworkFluidStorage nas : storages) {
            FluidStack removed = nas.removeById(id, (int) remaining, false);
            if (removed != null) {
                stack.amount += removed.amount;
                remaining -= removed.amount;
                if (stack.amount >= amount) break;
            }
        }

        return stack.amount == 0 ? null : stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> moveItems(ItemStackList what, ItemStackList where) {
        return moveItems(what.getStacks(), where);
    }

    @Override
    public @UnmodifiableView List<ItemStack> moveItems(List<ItemStack> what, ItemStackList where) {
        if (world != null && world.isRemote) return what;
        ArrayList<ItemStack> leftovers = new ArrayList<>();

        for (ItemStack stack : what) {
            ItemStack removed = removeItemFromNetwork(stack.itemId, stack.getDamage(), stack.getStationNbt(), stack.count);
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
    public @UnmodifiableView List<FluidStack> moveFluids(FluidStackList what, FluidStackList where) {
        return moveFluids(what.getStacks(), where);
    }

    @Override
    public @UnmodifiableView List<FluidStack> moveFluids(List<FluidStack> what, FluidStackList where) {
        if (world != null && world.isRemote) return what;
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = removeFluidFromNetwork(stack.fluid.getFlowingBlock().id, stack.amount);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            FluidStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(Catalyst.condenseFluidList(leftovers));
    }

    @Override
    public long countItems(int id, int meta, NbtCompound data) {
        if (world != null && world.isRemote) return 0;
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().filter((S) -> S.itemId == id && (S.getDamage() == meta || meta == -1) && (data == null || S.getStationNbt().equals(data))).mapToInt((S) -> S.count).sum();
    }

    @Override
    public long countFluids(int id) {
        if (world != null && world.isRemote) return 0;
        List<FluidStack> fluids = getAllFluids();
        return fluids.stream().filter(S -> S.fluid.getFlowingBlock().id == id).mapToLong(S -> S.amount).sum();
    }

    @Override
    public String getName() {
        return "container.retrostorage.digitalController";
    }
}
