package sunsetsatellite.retrostorage.util;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;

public interface NetworkController {
    boolean isActive();

    int getEnergyConsumption();

    ArrayDeque<CraftingTask> getRequestQueue();

    ArrayList<CraftingTask> getCurrentTasks();

    void clearRequestQueue();

    void processCraftingTasks();

    @UnmodifiableView
    Set<NetworkItemStorage> getAttachedStorage();

    @UnmodifiableView Set<NetworkFluidStorage> getAttachedFluidStorage();

    @UnmodifiableView
    Set<Coprocessor> getCoprocessors();

    @UnmodifiableView
    Set<Processor> getProcessors();

    @UnmodifiableView
    List<NetworkCraftable> getCraftables();

    @UnmodifiableView
    Map<Processor, @UnmodifiableView List<NetworkCraftable>> getCraftablesMap();

    void requestCrafting(CraftingTask task);

    Processor findProcessor(NetworkCraftable craftable);

    Processor findProcessorWithNode(ProcessNode node);

    @UnmodifiableView
    List<ItemStack> getAllItems();

    @UnmodifiableView List<FluidStack> getAllFluids();

    @UnmodifiableView
    List<ItemStack> getAllItems(Comparator<? super ItemStack> sortingFunction);

    @UnmodifiableView
    Map<NetworkItemStorage, @UnmodifiableView List<ItemStack>> getItemMap();

    @UnmodifiableView List<FluidStack> getAllFluids(Comparator<? super FluidStack> sortingFunction);

    @UnmodifiableView Map<NetworkFluidStorage,@UnmodifiableView List<FluidStack>> getFluidMap();

    long getItemCapacity();

    long getStackCapacity();

    long getStackAmount();

    long getAmount();

    long getFluidCapacity();

    long getFluidStackCapacity();

    long getFluidStackAmount();

    long getFluidAmount();

    ItemStack addItemToNetwork(ItemStack stack);

    @UnmodifiableView
    List<ItemStack> addItemsToNetwork(List<ItemStack> stacks);

    ItemStack removeItemFromNetwork(int id, int meta, NbtCompound data, long amount);

    FluidStack addFluidToNetwork(FluidStack stack);

    @UnmodifiableView
    List<FluidStack> addFluidsToNetwork(List<FluidStack> stacks);

    FluidStack removeFluidFromNetwork(FluidType id, long amount);

    @UnmodifiableView
    List<ItemStack> moveItems(ItemStackList what, ItemStackList where);

    @UnmodifiableView
    List<ItemStack> moveItems(List<ItemStack> what, ItemStackList where);

    @UnmodifiableView
    List<FluidStack> moveFluids(FluidStackList what, FluidStackList where);

    @UnmodifiableView
    List<FluidStack> moveFluids(List<FluidStack> what, FluidStackList where);

    long countItems(int id, int meta, NbtCompound data);

    long countFluids(int id);
}
