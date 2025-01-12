package sunsetsatellite.retrostorage.util;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;

public interface INetworkController {
    ArrayDeque<CraftingTask> getRequestQueue();

    ArrayList<CraftingTask> getCurrentTasks();

    void clearRequestQueue();

    void processCraftingTasks();

    @UnmodifiableView
    List<INetworkItemStorage> getAttachedStorage();

    @UnmodifiableView
    List<ICoprocessor> getCoprocessors();

    @UnmodifiableView
    List<IProcessor> getProcessors();

    @UnmodifiableView List<NetworkCraftable> getCraftables();

    @UnmodifiableView Map<IProcessor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap();

    void requestCrafting(CraftingTask task);

    IProcessor findProcessor(NetworkCraftable craftable);

    IProcessor findProcessorWithNode(ProcessNode node);

    @UnmodifiableView
    List<ItemStack> getAllItems();

    @UnmodifiableView
    List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction);

    @UnmodifiableView
    Map<INetworkItemStorage, @UnmodifiableView List<ItemStack>> getItemMap();

    long getItemCapacity();

    long getStackCapacity();

    long getStackAmount();

    long getAmount();

    ItemStack addItemToNetwork(ItemStack stack);

    @UnmodifiableView
    List<ItemStack> addItemsToNetwork(List<ItemStack> stacks);

    ItemStack removeItemFromNetwork(int id, int meta, CompoundTag data, long amount);

    //TODO: re-add fluid support!!
    FluidStack addFluidToNetwork(FluidStack stack);

    @UnmodifiableView
    List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks);

    FluidStack removeFluidFromNetwork(int id, long amount);

    @UnmodifiableView
    List<ItemStack> moveItems(ItemStackList what, ItemStackList where);

    @UnmodifiableView
    List<ItemStack> moveItems(List<ItemStack> what, ItemStackList where);

    @UnmodifiableView
    List<FluidStack> moveFluids(FluidStackList what, FluidStackList where);

    @UnmodifiableView
    List<FluidStack> moveFluids(List<FluidStack> what, FluidStackList where);

    long countItems(int id, int meta, CompoundTag data);

    long countFluids(int id);
}
