package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.FluidStackList;

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

    public NodeRequirements(CompoundTag tag) {
        readFromNbt(tag);
    }

    public NodeRequirements() {}

    public void writeToNbt(CompoundTag tag) {
        CompoundTag itemRequirementsTag = new CompoundTag();
        for (Map.Entry<Integer, ItemStackList> entry : itemRequirements.entrySet()) {
            CompoundTag listTag = new CompoundTag();
            entry.getValue().writeToNbt(listTag);
            itemRequirementsTag.put(String.valueOf(entry.getKey()), listTag);
        }
        tag.put("ItemRequirements", itemRequirementsTag);
        CompoundTag fluidRequirementsTag = new CompoundTag();
        for (Map.Entry<Integer, FluidStackList> entry : fluidRequirements.entrySet()) {
            CompoundTag listTag = new CompoundTag();
            entry.getValue().writeToNbt(listTag);
            fluidRequirementsTag.put(String.valueOf(entry.getKey()), listTag);
        }
        tag.put("FluidRequirements", fluidRequirementsTag);

        CompoundTag itemsNeededPerCraftTag = new CompoundTag();
        for (Map.Entry<Integer, Integer> entry : itemsNeededPerCraft.entrySet()) {
            tag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("ItemsNeededPerCraft", itemsNeededPerCraftTag);

        CompoundTag fluidsNeededPerCraftTag = new CompoundTag();
        for (Map.Entry<Integer, Integer> entry : fluidsNeededPerCraft.entrySet()) {
            tag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("FluidsNeededPerCraft", fluidsNeededPerCraftTag);
    }

    public void readFromNbt(CompoundTag tag) {
        CompoundTag itemRequirementsTag = tag.getCompound("ItemRequirements");
        CompoundTag fluidRequirementsTag = tag.getCompound("FluidRequirements");
        CompoundTag itemsNeededPerCraftTag = tag.getCompound("ItemsNeededPerCraft");
        CompoundTag fluidsNeededPerCraftTag = tag.getCompound("FluidsNeededPerCraft");

        for (Map.Entry<String, Tag<?>> entry : itemRequirementsTag.getValue().entrySet()) {
            CompoundTag listTag = (CompoundTag) entry.getValue();
            int ingredientNumber = Integer.parseInt(entry.getKey());
            ItemStackList list = new ItemStackList();
            list.readFromNbt(listTag);
            itemRequirements.put(ingredientNumber, list);
        }

        for (Map.Entry<String, Tag<?>> entry : fluidRequirementsTag.getValue().entrySet()) {
            CompoundTag listTag = (CompoundTag) entry.getValue();
            int ingredientNumber = Integer.parseInt(entry.getKey());
            FluidStackList list = new FluidStackList();
            list.readFromNbt(listTag);
            fluidRequirements.put(ingredientNumber, list);
        }

        for (Map.Entry<String, Tag<?>> entry : itemsNeededPerCraftTag.getValue().entrySet()) {
            int key = Integer.parseInt(entry.getKey());
            int value = ((IntTag) entry).getValue();
            itemsNeededPerCraft.put(key, value);
        }

        for (Map.Entry<String, Tag<?>> entry : fluidsNeededPerCraftTag.getValue().entrySet()) {
            int key = Integer.parseInt(entry.getKey());
            int value = ((IntTag) entry).getValue();
            fluidsNeededPerCraft.put(key, value);
        }
    }

    public void addItemRequirement(int ingredientNumber, ItemStack stack, int size, int perCraft) {
        stack = stack.copy();
        if (!itemsNeededPerCraft.containsKey(ingredientNumber)) {
            itemsNeededPerCraft.put(ingredientNumber, perCraft);
        }

        ItemStackList list = itemRequirements.computeIfAbsent(ingredientNumber, key -> new ItemStackList());

        stack.stackSize = size;
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

                    if (needed < toUse.stackSize) {
                        if (!simulate) {
                            itemRequirements.get(i).remove(toUse.itemID, toUse.getMetadata(), needed, toUse.getData(), false, true);
                        }

                        ItemStack copy = toUse.copy();
                        copy.stackSize = needed;
                        toReturn.add(copy);

                        needed = 0;
                    } else {
                        if (!simulate) {
                            it.remove();
                        }

                        toReturn.add(toUse);

                        needed -= toUse.stackSize;
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
                            fluidRequirements.get(i).removeById(toUse.fluid.getFirstId(), needed, false);
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
