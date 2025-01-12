package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IConduitTile;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;
import java.util.stream.Collectors;

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
    public @UnmodifiableView List<INetworkItemStorage> getAttachedStorage() {
        if(network == null) return new ArrayList<>();
        ArrayList<INetworkItemStorage> list = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    list.addAll(network.search(((IConduitTile) tileEntity).getPosition(), INetworkItemStorage.class));
                }
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView List<ICoprocessor> getCoprocessors() {
        if(network == null) return new ArrayList<>();
        ArrayList<ICoprocessor> list = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    list.addAll(network.search(((IConduitTile) tileEntity).getPosition(), ICoprocessor.class));
                }
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView List<IProcessor> getProcessors() {
        if(network == null) return new ArrayList<>();
        ArrayList<IProcessor> list = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = dir.getTileEntity(worldObj, this);
            if(tileEntity instanceof IConduitTile) {
                if (((IConduitTile) tileEntity).getConduitCapability() == ConduitCapability.RES_NETWORK) {
                    list.addAll(network.search(((IConduitTile) tileEntity).getPosition(), IProcessor.class));
                }
            }
        }
        return Collections.unmodifiableList(list);
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
    public @UnmodifiableView List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction) {
        if(network == null) return new ArrayList<>();
        ArrayList<ItemStack> list = new ArrayList<>();
        for (INetworkItemStorage storage : getAttachedStorage()) {
            list.addAll(storage.getStacks());
        }
        List<ItemStack> sorted = RetroStorage.condenseItemList(list).stream().sorted(sortingFunction).collect(Collectors.toList());
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
        return Collections.unmodifiableList(RetroStorage.condenseItemList(leftovers));
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

    //TODO: re-add fluid support!!
    @Override
    public FluidStack addFluidToNetwork(FluidStack stack) {
        return stack;
    }

    @Override
    public @UnmodifiableView List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks) {
        return Collections.emptyList();
    }

    @Override
    public FluidStack removeFluidFromNetwork(int id, long amount) {
        return null;
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
        return Collections.unmodifiableList(RetroStorage.condenseItemList(leftovers));
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
        return 0;
    }

    /*public boolean contains(int id, int meta) {
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().anyMatch(stack -> stack.itemID == id && stack.getMetadata() == id);
    }


    public boolean containsAtLeast(int id, int meta, long amount) {
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().anyMatch((stack) -> stack.itemID == id && stack.getMetadata() == id && stack.stackSize >= amount);
    }


    public boolean containsAtLeast(List<ItemStack> comparedTo) {
        List<ItemStack> networkItems = getAllItems();
        return networkItems.stream().filter(Objects::nonNull)
                .anyMatch((networkStack)->comparedTo.stream().filter(Objects::nonNull)
                        .anyMatch((comparedToStack) -> networkStack.isItemEqual(comparedToStack) && networkStack.stackSize >= comparedToStack.stackSize));
    }


    public boolean containsAtLeast(ItemStackList stacks) {
        return containsAtLeast(stacks.getStacks());
    }

    public ArrayList<ItemStack> returnMissing(ArrayList<ItemStack> stacks) {
        ArrayList<ItemStack> missing = new ArrayList<>();
        for (ItemStack stack : stacks) {
            long c = count(stack.itemID, stack.getMetadata());
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

    public int find(int id, int meta) {
        List<ItemStack> stacks = getAllItems();
        for (int i = 0; i < stacks.size(); i++) {
            if(stacks.get(i).itemID == id && stacks.get(i).getMetadata() == meta) {
                return i;
            }
        }
        return -1;
    }

    public ItemStack get(int index) {
        List<ItemStack> stacks = getAllItems();
        if (index < 0 || index >= stacks.size()) {
            return null;
        }
        return stacks.get(index);
    }

    public ItemStack get(int id, int meta) {
        return get(find(id, meta));
    }

    public ItemStack getLast() {
        List<ItemStack> stacks = getAllItems();
        return stacks.get(stacks.size() - 1);
    }

    public void inventoryChanged() {

    }

    public List<ItemStack> getStacks() {
        return getAllItems();
    }

    public boolean isEmpty() {
        List<ItemStack> stacks = getAllItems();
        return stacks.isEmpty();
    }*/


}
