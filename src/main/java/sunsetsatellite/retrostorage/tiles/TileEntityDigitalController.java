package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.DoubleTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitTile;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.mp.PacketRequestControllerContentsUpdate;
import sunsetsatellite.retrostorage.mp.PacketRequestControllerCraftingQueue;
import sunsetsatellite.retrostorage.mp.PacketRequestControllerUpdate;
import sunsetsatellite.retrostorage.mp.PacketRequestCrafting;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityDigitalController extends TileEntityNetworkDevice implements INetworkController {

    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasks = new ArrayList<>();

    public TileEntityEnergyAcceptor externalEnergy;

    public double energy = 0;
    public boolean active = false;

    public TileEntityDigitalController() {
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        externalEnergy = getConnectedTileEntity(TileEntityEnergyAcceptor.class);
        if (externalEnergy != null) {
            long draw = getEnergyConsumption();
            if(externalEnergy.getEnergy() >= draw){
                externalEnergy.internalRemoveEnergy(draw);
                active = true;
            } else {
                active = false;
            }
        } else {
            long draw = getEnergyConsumption();
            if(energy >= draw){
                energy -= draw;
                active = true;
            } else {
                active = false;
            }
        }
        if(active){
            processCraftingTasks();
        }
    }

    public long energyConsumptionCache = 0;

    @Override
    public long getEnergyConsumption() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return energyConsumptionCache;
        }
        return getAttachedFluidStorage().size() + getAttachedStorage().size() + getProcessors().size() + getCoprocessors().size();
    }

    @Override
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }

    public ArrayDeque<CraftingTask> requestQueueCache = new ArrayDeque<>();
    public ArrayList<CraftingTask> currentTasksCache = new ArrayList<>();

    @Override
    public ArrayDeque<CraftingTask> getRequestQueue() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerCraftingQueue(x,y,z));
            return requestQueueCache;
        }
        return requestQueue;
    }

    @Override
    public ArrayList<CraftingTask> getCurrentTasks() {
        if (worldObj != null && worldObj.isClientSide) {

            return currentTasksCache;
        }
        return currentTasks;
    }

    @Override
    public void clearRequestQueue() {
        if (worldObj != null && worldObj.isClientSide) return;
        requestQueue.clear();
        for (CraftingTask task : currentTasks) {
            task.onCancelled();
        }
        for (IProcessor processor : getProcessors()) {
            processor.setFocus(null,null);
        }
        currentTasks.clear();
    }

    @Override
    public void processCraftingTasks(){
        if (worldObj != null && worldObj.isClientSide) return;
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
        if (worldObj != null && worldObj.isClientSide) return new HashSet<>();
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
        if (worldObj != null && worldObj.isClientSide) return new HashSet<>();
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
        if (worldObj != null && worldObj.isClientSide) return new HashSet<>();
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
        if (worldObj != null && worldObj.isClientSide) return new HashSet<>();
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
        if (worldObj != null && worldObj.isClientSide) return new ArrayList<>();
        if(network == null) return new ArrayList<>();
        ArrayList<NetworkCraftable> list = new ArrayList<>();
        for (IProcessor processor : getProcessors()) {
            list.addAll(processor.getCraftables());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public @UnmodifiableView Map<IProcessor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap() {
        if (worldObj != null && worldObj.isClientSide) return new HashMap<>();
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
            if (worldObj != null && worldObj.isClientSide) {
                NetworkHandler.sendToServer(new PacketRequestCrafting(x,y,z, task));
                return;
            }
            requestQueue.add(task);
        }
    }

    @Override
    public IProcessor findProcessor(NetworkCraftable craftable) {
        if (worldObj != null && worldObj.isClientSide) return null;
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
        if (worldObj != null && worldObj.isClientSide) return null;
        for (IProcessor processor : getProcessors()) {
            if(processor.getWorkingNode() == node){
                return processor;
            }
        }
        return null;
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerContentsUpdate(x,y,z));
            return itemCache;
        }
        return getAllItems(RetroStorage::sortById);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerContentsUpdate(x,y,z));
            return fluidCache;
        }
        return getAllFluids(RetroStorage::sortByIdFluid);
    }

    @Override
    public @UnmodifiableView List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction) {
        if (worldObj != null && worldObj.isClientSide) return new ArrayList<>();
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
        if (worldObj != null && worldObj.isClientSide) return new HashMap<>();
        if(network == null) return new HashMap<>();
        HashMap<INetworkItemStorage,@UnmodifiableView List<ItemStack>> map = new HashMap<>();
        for (INetworkItemStorage storage : getAttachedStorage()) {
            map.put(storage, storage.getStacks());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public @UnmodifiableView List<FluidStack> getAllFluids(Comparator<? super FluidStack> sortingFunction) {
        if (worldObj != null && worldObj.isClientSide) return new ArrayList<>();
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
        active = CompoundTag.getBoolean("Active");
    }

    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        DoubleTag nbt = new DoubleTag(energy);
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
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return itemCapacityCache;
        }
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getItemCapacity).sum();
    }


    @Override
    public long getStackCapacity() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return itemStackCapacityCache;
        }
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getStackCapacity).sum();
    }


    @Override
    public long getStackAmount() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return itemStackAmountCache;
        }
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getStackAmount).sum();
    }


    @Override
    public long getAmount() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return itemAmountCache;
        }
        return getAttachedStorage().stream().mapToLong(INetworkItemStorage::getAmount).sum();
    }

    @Override
    public long getFluidCapacity() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return fluidCapacityCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getMaxFluidAmount).sum();
    }


    @Override
    public long getFluidStackCapacity() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return fluidStackCapacityCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getMaxFluidStackSize).sum();
    }


    @Override
    public long getFluidStackAmount() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return fluidStackAmountCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getFluidStackAmount).sum();
    }


    @Override
    public long getFluidAmount() {
        if (worldObj != null && worldObj.isClientSide) {
            NetworkHandler.sendToServer(new PacketRequestControllerUpdate(x,y,z));
            return fluidAmountCache;
        }
        return getAttachedFluidStorage().stream().mapToLong(INetworkFluidStorage::getFluidAmount).sum();
    }

    @Override
    public ItemStack addItemToNetwork(ItemStack stack) {
        if (worldObj != null && worldObj.isClientSide) return stack;

        List<INetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(INetworkItemStorage::getPriority)));

        for (INetworkItemStorage nas : storages) {
            if(stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<ItemStack> addItemsToNetwork(List<ItemStack> stacks) {
        if (worldObj != null && worldObj.isClientSide) return stacks;
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack stack : stacks) {
            leftovers.add(addItemToNetwork(stack));
        }
        return Collections.unmodifiableList(Catalyst.condenseItemList(leftovers));
    }

    @Override
    public ItemStack removeItemFromNetwork(int id, int meta, CompoundTag data, long amount) {
        if(id == 0) return null;
        if (worldObj != null && worldObj.isClientSide) return null;

        ItemStack stack = new ItemStack(id,0,meta,data);

        long remaining = amount;

        List<INetworkItemStorage> storages = new ArrayList<>(getAttachedStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(INetworkItemStorage::getPriority)));

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
        if (worldObj != null && worldObj.isClientSide) return stack;
        List<INetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(INetworkFluidStorage::getPriority)));

        for (INetworkFluidStorage nas : storages) {
            if(stack == null) return null;
            stack = nas.add(stack);
        }

        return stack;
    }

    @Override
    public @UnmodifiableView List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks) {
        if (worldObj != null && worldObj.isClientSide) return stacks;
        ArrayList<FluidStack> leftovers = new ArrayList<>();
        for (FluidStack stack : stacks) {
            leftovers.add(addFluidToNetwork(stack));
        }
        return Collections.unmodifiableList(RetroStorage.condenseFluidList(leftovers));
    }

    @Override
    public FluidStack removeFluidFromNetwork(int id, long amount) {
        if(id == 0) return null;
        if (worldObj != null && worldObj.isClientSide) return null;

        FluidStack stack = new FluidStack(Fluids.getFluid(id),0);

        long remaining = amount;

        List<INetworkFluidStorage> storages = new ArrayList<>(getAttachedFluidStorage());
        storages.sort(Collections.reverseOrder(Comparator.comparing(INetworkFluidStorage::getPriority)));

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
        if (worldObj != null && worldObj.isClientSide) return what;
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
        if (worldObj != null && worldObj.isClientSide) return what;
        ArrayList<FluidStack> leftovers = new ArrayList<>();

        for (FluidStack stack : what) {
            FluidStack removed = removeFluidFromNetwork(stack.fluid.getFirstId(),stack.amount);
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
        if (worldObj != null && worldObj.isClientSide) return 0;
        List<ItemStack> stacks = getAllItems();
        return stacks.stream().filter((S)->S.itemID == id && (S.getMetadata() == meta || meta == -1) && (data == null || S.getData().equals(data))).mapToInt((S)->S.stackSize).sum();
    }

    @Override
    public long countFluids(int id) {
        if (worldObj != null && worldObj.isClientSide) return 0;
        List<FluidStack> fluids = getAllFluids();
        return fluids.stream().filter(S -> S.fluid.getFirstId() == id).mapToLong(S -> S.amount).sum();
    }
}
