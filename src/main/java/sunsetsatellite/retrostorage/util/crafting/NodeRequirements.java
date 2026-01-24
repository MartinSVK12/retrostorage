package sunsetsatellite.retrostorage.util.crafting;


import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;

import javax.annotation.Nullable;
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

    public NodeRequirements(NbtCompound tag) {
        readFromNbt(tag);
    }

    public NodeRequirements() {
    }

    public void writeToNbt(NbtCompound tag) {
        NbtCompound itemRequirementsTag = new NbtCompound();
        for (Map.Entry<Integer, ItemStackList> entry : itemRequirements.entrySet()) {
            NbtCompound listTag = new NbtCompound();
            entry.getValue().writeToNbt(listTag);
            itemRequirementsTag.put(String.valueOf(entry.getKey()), listTag);
        }
        tag.put("ItemRequirements", itemRequirementsTag);
        NbtCompound fluidRequirementsTag = new NbtCompound();
        for (Map.Entry<Integer, FluidStackList> entry : fluidRequirements.entrySet()) {
            NbtCompound listTag = new NbtCompound();
            entry.getValue().writeToNbt(listTag);
            fluidRequirementsTag.put(String.valueOf(entry.getKey()), listTag);
        }
        tag.put("FluidRequirements", fluidRequirementsTag);

        NbtCompound itemsNeededPerCraftTag = new NbtCompound();
        for (Map.Entry<Integer, Integer> entry : itemsNeededPerCraft.entrySet()) {
            itemsNeededPerCraftTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("ItemsNeededPerCraft", itemsNeededPerCraftTag);

        NbtCompound fluidsNeededPerCraftTag = new NbtCompound();
        for (Map.Entry<Integer, Integer> entry : fluidsNeededPerCraft.entrySet()) {
            itemsNeededPerCraftTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("FluidsNeededPerCraft", fluidsNeededPerCraftTag);
    }

    public void readFromNbt(NbtCompound tag) {
        NbtCompound itemRequirementsTag = tag.getCompound("ItemRequirements");
        NbtCompound fluidRequirementsTag = tag.getCompound("FluidRequirements");
        NbtCompound itemsNeededPerCraftTag = tag.getCompound("ItemsNeededPerCraft");
        NbtCompound fluidsNeededPerCraftTag = tag.getCompound("FluidsNeededPerCraft");

        for (Object o : itemRequirementsTag.entries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            NbtCompound listTag = (NbtCompound) entry.getValue();
            int ingredientNumber = Integer.parseInt((String) entry.getKey());
            ItemStackList list = new ItemStackList();
            list.readFromNbt(listTag);
            itemRequirements.put(ingredientNumber, list);
        }

        for (Object o : fluidRequirementsTag.entries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            NbtCompound listTag = (NbtCompound) entry.getValue();
            int ingredientNumber = Integer.parseInt((String) entry.getKey());
            FluidStackList list = new FluidStackList();
            list.readFromNbt(listTag);
            fluidRequirements.put(ingredientNumber, list);
        }

        for (Object o : itemsNeededPerCraftTag.entries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            int key = Integer.parseInt((String) entry.getKey());
            int value = ((NbtInt) entry.getValue()).value;
            itemsNeededPerCraft.put(key, value);
        }

        for (Object o : fluidsNeededPerCraftTag.entries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            int key = Integer.parseInt((String) entry.getKey());
            int value = ((NbtInt) entry.getValue()).value;
            fluidsNeededPerCraft.put(key, value);
        }
    }

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
                            fluidRequirements.get(i).removeById(toUse.fluid.getFlowingBlock().id, needed, false);
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
