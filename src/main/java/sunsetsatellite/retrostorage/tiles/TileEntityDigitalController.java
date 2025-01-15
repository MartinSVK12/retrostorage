package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IConduitTile;
import sunsetsatellite.catalyst.core.util.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;
import java.util.stream.Collectors;

//TODO: energy usage
//TODO: load data from old drives
public class TileEntityDigitalController extends TileEntityNetworkDevice implements INetworkController {

    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasks = new ArrayList<>();

    public TileEntityDigitalController() {
    }

    @Override
    public void tick() {
        super.tick();
        externalEnergy = getConnectedTileEntity(TileEntityEnergyAcceptor.class);
        processCraftingTasks();
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
    public @UnmodifiableView Set<INetworkItemStorage> getAttachedStorage() {
        if(network == null) return new HashSet<>();
        HashSet<INetworkItemStorage> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    set.addAll(network.search(((IConduitTile) tileEntity).getPosition(), INetworkItemStorage.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<INetworkFluidStorage> getAttachedFluidStorage() {
        if(network == null) return new HashSet<>();
        HashSet<INetworkFluidStorage> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    set.addAll(network.search(((IConduitTile) tileEntity).getPosition(), INetworkFluidStorage.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<ICoprocessor> getCoprocessors() {
        if(network == null) return new HashSet<>();
        HashSet<ICoprocessor> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    set.addAll(network.search(((IConduitTile) tileEntity).getPosition(), ICoprocessor.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView Set<IProcessor> getProcessors() {
        if(network == null) return new HashSet<>();
        HashSet<IProcessor> set = new HashSet<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    set.addAll(network.search(((IConduitTile) tileEntity).getPosition(), IProcessor.class));
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public @UnmodifiableView List<NetworkCraftable> getCraftables() {
        if(network == null) return new ArrayList<>();
        ArrayList<NetworkCraftable> list = new ArrayList<>();
        for (IProcessor processor : getProcessors()) {
            list.addAll(processor.getCraftables());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView Map<IProcessor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap() {
        if(network == null) return new HashMap<>();
        HashMap<IProcessor, @UnmodifiableView List<NetworkCraftable>> map = new HashMap<>();
        for (IProcessor processor : getProcessors()) {
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
    public IProcessor findProcessor(NetworkCraftable craftable) {
        ArrayList<IProcessor> instances = new ArrayList<>(getProcessors());

        for (IProcessor processor : instances) {
            if (processor.getCraftables().contains(craftable)) {
                return processor;
            }
        }
        return null;
    }

    @Override
    public IProcessor findProcessorWithNode(ProcessNode node) {
        for (IProcessor processor : getProcessors()) {
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
        for (INetworkItemStorage storage : getAttachedStorage()) {
            list.addAll(storage.getStacks());
        }
        List<ItemStack> sorted = Catalyst.condenseItemList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<INetworkItemStorage,@UnmodifiableView List<ItemStack>> getItemMap(){
        if(network == null) return new HashMap<>();
        HashMap<INetworkItemStorage,@UnmodifiableView List<ItemStack>> map = new HashMap<>();
        for (INetworkItemStorage storage : getAttachedStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids(Comparator<? super FluidStack> sortingFunction) {
        if(network == null) return new ArrayList<>();
        ArrayList<FluidStack> list = new ArrayList<>();
        for (INetworkFluidStorage storage : getAttachedFluidStorage()) {
            list.addAll(storage.getStacks());
        }
        List<FluidStack> sorted = RetroStorage.condenseFluidList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public @UnmodifiableView Map<INetworkFluidStorage,@UnmodifiableView List<FluidStack>> getFluidMap(){
        if(network == null) return new HashMap<>();
        HashMap<INetworkFluidStorage,@UnmodifiableView List<FluidStack>> map = new HashMap<>();
        for (INetworkFluidStorage storage : getAttachedFluidStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        energy = CompoundTag.getDouble("Energy");
    }

    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        DoubleTag nbt = new DoubleTag(energy);
        CompoundTag.put("Energy", nbt);
    }

    public double energy = 0;

    public TileEntityEnergyAcceptor externalEnergy;


    @Override
    public long getItemCapacity() {
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getItemCapacity).sum();
    }


    @Override
    public long getStackCapacity() {
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getStackCapacity).sum();
    }


    @Override
    public long getStackAmount() {
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getStackAmount).sum();
    }


    @Override
    public long getAmount() {
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getAmount).sum();
    }

    @Override
    public long getFluidCapacity() {
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getMaxFluidAmount).sum();
    }


    @Override
    public long getFluidStackCapacity() {
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getMaxFluidStackSize).sum();
    }


    @Override
    public long getFluidStackAmount() {
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getFluidStackAmount).sum();
    }


    @Override
    public long getFluidAmount() {
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getFluidAmount).sum();
    }

    @Override
    public ItemStack addItemToNetwork(ItemStack stack) {

        List<INetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Comparator.comparingInt(INetworkItemStorage::getPriority));

        for (INetworkItemStorage nas : storages) {
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
        return Collections.unmodifiableList(Catalyst.condenseItemList(leftovers));
    }

    @Override
    public ItemStack removeItemFromNetwork(int id, int meta, CompoundTag data, long amount) {
        if(id == 0) return null;

        ItemStack stack = new ItemStack(id,0,meta,data);

        long remaining = amount;

        List<INetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Comparator.comparingInt(INetworkItemStorage::getPriority));

        for (INetworkItemStorage nas : storages) {
            ItemStack removed = nas.remove(id, meta, remaining, data, false, true);
            if(removed != null) {
                stack.stackSize += removed.stackSize;
                remaining -= removed.stackSize;
                if(stack.stackSize >= amount) break;
            }
        }

        return stack.stackSize == 0 ? null : stack;
    }

    @Override
    public FluidStack addFluidToNetwork(FluidStack stack) {
        List<INetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Comparator.comparingInt(INetworkFluidStorage::getPriority));

        for (INetworkFluidStorage nas : storages) {
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
        return Collections.unmodifiableList(RetroStorage.condenseFluidList(leftovers));
    }

    @Override
    public FluidStack removeFluidFromNetwork(int id, long amount) {
        if(id == 0) return null;

        FluidStack stack = new FluidStack((BlockFluid) Block.getBlock(id),0);

        long remaining = amount;

        List<INetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Comparator.comparingInt(INetworkFluidStorage::getPriority));

        for (INetworkFluidStorage nas : storages) {
            FluidStack removed = nas.removeById(id, (int) remaining, false);
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
            ItemStack removed = removeItemFromNetwork(stack.itemID, stack.getMetadata(), stack.getData(), stack.stackSize);
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
        return moveFluids(what.getStacks(),where);
    }

    @Override
    public @UnmodifiableView List<FluidStack> moveFluids(List<FluidStack> what, FluidStackList where) {
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = removeFluidFromNetwork(stack.liquid.id,stack.amount);
            if (removed == null) {
                leftovers.add(stack);
                continue;
            }
            FluidStack addLeftover = where.add(removed);
            leftovers.add(addLeftover);
        }
        return Collections.unmodifiableList(RetroStorage.condenseFluidList(leftovers));
    }

    @Override
    public long countItems(int id, int meta, CompoundTag data) {
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().filter((S)->S.itemID == id && (S.getMetadata() == meta || meta == -1) && (data == null || S.getData().equals(data))).mapToInt((S)->S.stackSize).sum();
    }

    @Override
    public long countFluids(int id) {
        List<FluidStack> fluids = getAllFluids();
        return fluids.stream().filter(S -> S.liquid.id == id).mapToLong(S -> S.amount).sum();
    }
}
