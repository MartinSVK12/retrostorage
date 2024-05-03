package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.util.InventoryDigital;
import sunsetsatellite.retrostorage.util.ItemStackList;

import javax.annotation.Nullable;
import java.util.*;

public class NodeRequirements {

    private final Map<Integer, ItemStackList> itemRequirements = new LinkedHashMap<>();
    private final Map<Integer, Integer> itemsNeededPerCraft = new LinkedHashMap<>();

    @Nullable
    private List<ItemStack> cachedSimulatedItemRequirementSet = null;

    public void addItemRequirement(int ingredientNumber, ItemStack stack, int size, int perCraft) {
        stack = stack.copy();
        if (!itemsNeededPerCraft.containsKey(ingredientNumber)) {
            itemsNeededPerCraft.put(ingredientNumber, perCraft);
        }

        ItemStackList list = itemRequirements.computeIfAbsent(ingredientNumber, key -> new ItemStackList());

        stack.stackSize = size;
        list.add(stack);
    }

    @Override
    public String toString() {
        return "NodeRequirements{" +
                "itemRequirements=" + itemRequirements +
                ", itemsNeededPerCraft=" + itemsNeededPerCraft +
                '}';
    }

    public List<ItemStack> getSingleItemRequirements(boolean simulate) {
        List<ItemStack> cached = cachedSimulatedItemRequirementSet;
        if (simulate && cached != null) {
            return cached;
        }

        List<ItemStack> toReturn = new ArrayList<>();

        for (int i = 0; i < itemRequirements.size(); i++) {
            int needed = itemsNeededPerCraft.get(i);

            if (!itemRequirements.get(i).isEmpty()) {
                Iterator<ItemStack> it = itemRequirements.get(i).iterator();

                while (needed > 0 && it.hasNext()) {
                    ItemStack toUse = it.next();

                    if(needed < toUse.stackSize) {
                        if (!simulate) {
                            itemRequirements.get(i).remove(toUse.itemID, toUse.getMetadata(), needed, false, true);
                        }

                        ItemStack copy = toUse.copy();
                        copy.stackSize = needed;
                        toReturn.add(copy);

                        needed = 0;
                    } else {
                        if (!simulate){
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
}
