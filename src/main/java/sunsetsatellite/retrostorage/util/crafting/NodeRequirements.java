package sunsetsatellite.retrostorage.util.crafting;


import net.minecraft.item.ItemStack;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.ItemStackList;

import java.util.*;

public class NodeRequirements {

    private final Map<Integer, ItemStackList> itemRequirements = new LinkedHashMap<>();
    private final Map<Integer, Integer> itemsNeededPerCraft = new LinkedHashMap<>();

    private final Map<Integer, FluidStackList> fluidRequirements = new LinkedHashMap<>();
    private final Map<Integer, Integer> fluidsNeededPerCraft = new LinkedHashMap<>();

    @Nullable
    private List<ItemStack> cachedSimulatedItemRequirementSet = null;

    @Nullable
    private List<FluidStack> cachedSimulatedFluidRequirementSet = null;


    public void addItemRequirement(int ingredientNumber, ItemStack stack, int size, int perCraft) {
        stack = stack.copy();
        if (!itemsNeededPerCraft.containsKey(ingredientNumber)) {
            itemsNeededPerCraft.put(ingredientNumber, perCraft);
        }

        ItemStackList list = itemRequirements.computeIfAbsent(ingredientNumber, key -> new ItemStackList());

        stack.count = size;
        list.add(stack);
    }

    public void addFluidRequirement(int ingredientNumber, FluidStack stack, int size, int perCraft) {
        stack = stack.copy();
        if (!fluidsNeededPerCraft.containsKey(ingredientNumber)) {
            fluidsNeededPerCraft.put(ingredientNumber, perCraft);
        }

        FluidStackList list = fluidRequirements.computeIfAbsent(ingredientNumber, key -> new FluidStackList());

        stack.amount = size;
        list.add(stack);
    }

    @Override
    public String toString() {
        return "NodeRequirements{" +
                "fluidsNeededPerCraft=" + fluidsNeededPerCraft +
                ", fluidRequirements=" + fluidRequirements +
                ", itemsNeededPerCraft=" + itemsNeededPerCraft +
                ", itemRequirements=" + itemRequirements +
                '}';
    }

    public List<ItemStack> getSingleItemRequirements(boolean simulate) {
        List<ItemStack> cached = cachedSimulatedItemRequirementSet;
        if (simulate && cached != null && !(cached.isEmpty())) {
            return cached;
        }

        List<ItemStack> toReturn = new ArrayList<>();

        for (int i = 0; i < itemRequirements.size(); i++) {
            int needed = itemsNeededPerCraft.get(i);

            if (!itemRequirements.get(i).isEmpty()) {
                Iterator<ItemStack> it = itemRequirements.get(i).iterator();

                while (needed > 0 && it.hasNext()) {
                    ItemStack toUse = it.next();

                    if (needed < toUse.count) {
                        if (!simulate) {
                            itemRequirements.get(i).remove(toUse.itemId, toUse.getDamage(), needed, toUse.getStationNbt(), false, true);
                        }

                        ItemStack copy = toUse.copy();
                        copy.count = needed;
                        toReturn.add(copy);

                        needed = 0;
                    } else {
                        if (!simulate) {
                            it.remove();
                        }

                        toReturn.add(toUse);

                        needed -= toUse.count;
                    }
                }
            } else {
                return null;
            }
        }

        cachedSimulatedItemRequirementSet = simulate ? toReturn : null;

        return toReturn;
    }

    public List<FluidStack> getSingleFluidRequirements(boolean simulate) {
        List<FluidStack> cached = cachedSimulatedFluidRequirementSet;
        if (simulate && cached != null && !(cached.isEmpty())) {
            return cached;
        }

        List<FluidStack> toReturn = new ArrayList<>();

        for (int i = 0; i < fluidRequirements.size(); i++) {
            int needed = fluidsNeededPerCraft.get(i);

            if (!fluidRequirements.get(i).isEmpty()) {
                Iterator<FluidStack> it = fluidRequirements.get(i).iterator();

                while (needed > 0 && it.hasNext()) {
                    FluidStack toUse = it.next();

                    if (needed < toUse.amount) {
                        if (!simulate) {
                            fluidRequirements.get(i).removeById(toUse.fluid.blockId(), needed, false);
                        }

                        FluidStack copy = toUse.copy();
                        copy.amount = needed;
                        toReturn.add(copy);

                        needed = 0;
                    } else {
                        if (!simulate) {
                            it.remove();
                        }

                        toReturn.add(toUse);

                        needed -= toUse.amount;
                    }
                }
            } else {
                return null;
            }
        }

        cachedSimulatedFluidRequirementSet = simulate ? toReturn : null;

        return toReturn;
    }
}
