package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import net.teamterminus.machineessentials.network.NetworkComponent;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;
import java.util.stream.Collectors;

public class DigitalControllerBlockEntity extends NetworkDeviceBlockEntity implements NetworkController {

    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasks = new ArrayList<>();

    //public BlockEntityEnergyAcceptor externalEnergy;

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
        /*externalEnergy = getConnectedBlockEntity(BlockEntityEnergyAcceptor.class);
        if (externalEnergy == null) {
            int draw = getEnergyConsumption();
            if(externalEnergy.getEnergy() >= draw){
                externalEnergy.internalRemoveEnergy(draw);
                active = true;
            } else {
                active = false;
            }
        } else {
            active = false;
        }*/
        active = true;
        if(active){
            processCraftingTasks();
        }
    }

    @Override
    public int getEnergyConsumption() {
        return getAttachedFluidStorage().size() + getAttachedStorage().size() + getProcessors().size() + getCoprocessors().size();
    }

    @Override
    public ArrayDeque<CraftingTask> getRequestQueue() {
        return requestQueue;
    }

    @Override
    public ArrayList<CraftingTask> getCurrentTasks() {
        return currentTasks;
    }

    @Override
    public void clearRequestQueue() {
        requestQueue.clear();
        for (CraftingTask task : currentTasks) {
            task.onCancelled();
        }
        currentTasks.clear();
    }

    @Override
    public void processCraftingTasks(){
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
        if(network == null) return new HashSet<>();
        HashSet<NetworkItemStorage> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tileEntity = MachineEssentials.getBlockEntity(dir,world,this);
            if(tileEntity instanceof NetworkComponent) {
                if (((NetworkComponent) tileEntity).getType() == RetroStorage.RES_NETWORK) {
                    set.addAll(network.search(((NetworkComponent) tileEntity).getPosition(), NetworkItemStorage.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<NetworkFluidStorage> getAttachedFluidStorage() {
        if(network == null) return new HashSet<>();
        HashSet<NetworkFluidStorage> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tileEntity = MachineEssentials.getBlockEntity(dir,world,this);
            if(tileEntity instanceof NetworkComponent) {
                if (((NetworkComponent) tileEntity).getType() == RetroStorage.RES_NETWORK) {
                    set.addAll(network.search(((NetworkComponent) tileEntity).getPosition(), NetworkFluidStorage.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<Coprocessor> getCoprocessors() {
        if(network == null) return new HashSet<>();
        HashSet<Coprocessor> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tileEntity = MachineEssentials.getBlockEntity(dir,world,this);
            if(tileEntity instanceof NetworkComponent) {
                if (((NetworkComponent) tileEntity).getType() == RetroStorage.RES_NETWORK) {
                    set.addAll(network.search(((NetworkComponent) tileEntity).getPosition(), Coprocessor.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<Processor> getProcessors() {
        if(network == null) return new HashSet<>();
        HashSet<Processor> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tileEntity = MachineEssentials.getBlockEntity(dir,world,this);
            if(tileEntity instanceof NetworkComponent) {
                if (((NetworkComponent) tileEntity).getType() == RetroStorage.RES_NETWORK) {
                    set.addAll(network.search(((NetworkComponent) tileEntity).getPosition(), Processor.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView List<NetworkCraftable> getCraftables() {
        if(network == null) return new ArrayList<>();
        ArrayList<NetworkCraftable> list = new ArrayList<>();
        for (Processor processor : getProcessors()) {
            list.addAll(processor.getCraftables());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView Map<Processor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap() {
        if(network == null) return new HashMap<>();
        HashMap<Processor, @UnmodifiableView List<NetworkCraftable>> map = new HashMap<>();
        for (Processor processor : getProcessors()) {
            map.put(processor,processor.getCraftables());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void requestCrafting(CraftingTask task) {
        if (task != null) {
            RetroStorage.LOGGER.debug("Requesting: " + task.getCraftable().getOutput());
            requestQueue.add(task);
        }
    }

    @Override
    public Processor findProcessor(NetworkCraftable craftable) {
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
        for (Processor processor : getProcessors()) {
            if(processor.getWorkingNode() == node){
                return processor;
            }
        }
        return null;
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems() {
        return getAllItems(RetroStorage::sortById);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids() {
        return getAllFluids(RetroStorage::sortByIdFluid);
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction) {
        if(network == null) return new ArrayList<>();
        ArrayList<ItemStack> list = new ArrayList<>();
        for (NetworkItemStorage storage : getAttachedStorage()) {
            list.addAll(storage.getStacks());
        }
        List<ItemStack> sorted = MachineEssentials.condenseItemList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<NetworkItemStorage,@UnmodifiableView List<ItemStack>> getItemMap(){
        if(network == null) return new HashMap<>();
        HashMap<NetworkItemStorage,@UnmodifiableView List<ItemStack>> map = new HashMap<>();
        for (NetworkItemStorage storage : getAttachedStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids(Comparator<? super FluidStack> sortingFunction) {
        if(network == null) return new ArrayList<>();
        ArrayList<FluidStack> list = new ArrayList<>();
        for (NetworkFluidStorage storage : getAttachedFluidStorage()) {
            list.addAll(storage.getStacks());
        }
        List<FluidStack> sorted = MachineEssentials.condenseFluidList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<NetworkFluidStorage,@UnmodifiableView List<FluidStack>> getFluidMap(){
        if(network == null) return new HashMap<>();
        HashMap<NetworkFluidStorage,@UnmodifiableView List<FluidStack>> map = new HashMap<>();
        for (NetworkFluidStorage storage : getAttachedFluidStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energy = nbt.getDouble("Energy");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtDouble tag = new NbtDouble(energy);
        nbt.put("Energy", tag);
    }

    @Override
    public long getItemCapacity() {
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getItemCapacity).sum();
    }


    @Override
    public long getStackCapacity() {
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getStackCapacity).sum();
    }


    @Override
    public long getStackAmount() {
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getStackAmount).sum();
    }


    @Override
    public long getAmount() {
        return getAttachedStorage().stream().mapToLong(NetworkItemStorage::getAmount).sum();
    }

    @Override
    public long getFluidCapacity() {
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getMaxFluidAmount).sum();
    }


    @Override
    public long getFluidStackCapacity() {
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getMaxFluidStackSize).sum();
    }


    @Override
    public long getFluidStackAmount() {
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getFluidStackAmount).sum();
    }


    @Override
    public long getFluidAmount() {
        return getAttachedFluidStorage().stream().mapToLong(NetworkFluidStorage::getFluidAmount).sum();
    }

    @Override
    public ItemStack addItemToNetwork(ItemStack stack) {

        List<NetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Comparator.comparingInt(NetworkItemStorage::getPriority));

        for (NetworkItemStorage nas : storages) {
            if(stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> addItemsToNetwork(List<ItemStack> stacks) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack stack : stacks) {
            leftovers.add(addItemToNetwork(stack));
        }
        return Collections.unmodifiableList(MachineEssentials.condenseItemList(leftovers));
    }

    @Override
    public ItemStack removeItemFromNetwork(int id, int meta, NbtCompound data, long amount) {
        if(id == 0) return null;

        ItemStack stack = new ItemStack(id,0,meta);
        if(data != null) StationNBTSetter.cast(stack).setStationNbt(data.copy());

        long remaining = amount;

        List<NetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Comparator.comparingInt(NetworkItemStorage::getPriority));

        for (NetworkItemStorage nas : storages) {
            ItemStack removed = nas.remove(id, meta, remaining, data, false, true);
            if(removed != null) {
                stack.count += removed.count;
                remaining -= removed.count;
                if(stack.count >= amount) break;
            }
        }

        return stack.count == 0 ? null : stack;
    }

    @Override
    public FluidStack addFluidToNetwork(FluidStack stack) {
        List<NetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Comparator.comparingInt(NetworkFluidStorage::getPriority));

        for (NetworkFluidStorage nas : storages) {
            if(stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks) {
        ArrayList<FluidStack> leftovers = new ArrayList<>();
        for (FluidStack stack : stacks) {
            leftovers.add(addFluidToNetwork(stack));
        }
        return Collections.unmodifiableList(MachineEssentials.condenseFluidList(leftovers));
    }

    @Override
    public FluidStack removeFluidFromNetwork(FluidType id, long amount) {
        if(id == null) return null;

        FluidStack stack = new FluidStack(id,0);

        long remaining = amount;

        List<NetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Comparator.comparingInt(NetworkFluidStorage::getPriority));

        for (NetworkFluidStorage nas : storages) {
            FluidStack removed = nas.removeById(stack.fluid.blockId(), (int) remaining, false);
            if(removed != null) {
                stack.amount += removed.amount;
                remaining -= removed.amount;
                if(stack.amount >= amount) break;
            }
        }

        return stack.amount == 0 ? null : stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> moveItems(ItemStackList what, ItemStackList where) {
        return moveItems(what.getStacks(),where);
    }

    @Override
    public @UnmodifiableView List<ItemStack> moveItems(List<ItemStack> what, ItemStackList where) {
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
        return Collections.unmodifiableList(MachineEssentials.condenseItemList(leftovers));
    }

    @Override
    public @UnmodifiableView List<FluidStack> moveFluids(FluidStackList what, FluidStackList where) {
        return moveFluids(what.getStacks(),where);
    }

    @Override
    public @UnmodifiableView List<FluidStack> moveFluids(List<FluidStack> what, FluidStackList where) {
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = removeFluidFromNetwork(stack.fluid,stack.amount);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            FluidStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(MachineEssentials.condenseFluidList(leftovers));
    }

    @Override
    public long countItems(int id, int meta, NbtCompound data) {
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().filter((S)->S.itemId == id && (S.getDamage() == meta || meta == -1) /*&& (data == null || S.getStationNbt().equals(data))*/).mapToInt((S)->S.count).sum();
    }

    @Override
    public long countFluids(int id) {
        List<FluidStack> fluids = getAllFluids();
        return fluids.stream().filter(S -> S.fluid.blockId() == id).mapToLong(S -> S.amount).sum();
    }
}
