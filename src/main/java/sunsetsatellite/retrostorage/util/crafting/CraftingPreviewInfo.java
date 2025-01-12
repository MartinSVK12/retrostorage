package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.ItemStackList;

import java.util.ArrayList;
import java.util.List;

public class CraftingPreviewInfo {

    private final ItemStackList missing = new ItemStackList();
    private final FluidStackList missingFluids = new FluidStackList();
    private final ItemStackList toTake = new ItemStackList();
    private final FluidStackList toTakeFluids = new FluidStackList();

    private final List<ItemStack> toCraft = new ArrayList<>();
    private final List<FluidStack> toCraftFluids = new ArrayList<>();
    private final List<ItemStack> toProcess = new ArrayList<>();
    private final List<FluidStack> toProcessFluids = new ArrayList<>();

    public ItemStackList getMissing() {
        return missing;
    }

    public boolean hasMissing() {
        return !missing.isEmpty();
    }

    public ItemStackList getToTake() {
        return toTake;
    }

    public List<ItemStack> getToCraft() {
        return toCraft;
    }

    public List<ItemStack> getToProcess() {
        return toProcess;
    }

    public FluidStackList getMissingFluids() {
        return missingFluids;
    }

    public FluidStackList getToTakeFluids() {
        return toTakeFluids;
    }

    public List<FluidStack> getToCraftFluids() {
        return toCraftFluids;
    }

    public List<FluidStack> getToProcessFluids() {
        return toProcessFluids;
    }

    private List<Pair<ItemStack, String>> listCache = new ArrayList<>();

    public int size() {
        return (int) (missing.getStackAmount() + missingFluids.sizeStacks() + toTake.getStackAmount() + toTakeFluids.sizeStacks() + toCraft.size() + toCraftFluids.size() + toProcess.size() + toProcessFluids.size());
    }

    public List<Pair<ItemStack, String>> toList() {
        if (!listCache.isEmpty()) {
            return listCache;
        }
        List<Pair<ItemStack, String>> list = new ArrayList<>();
        for (int i = 0; i < missing.getStackAmount(); i++) {
            list.add(Pair.of(missing.get(i), "missing"));
        }

        for (int i = 0; i < missingFluids.sizeStacks(); i++) {
            list.add(Pair.of(missingFluids.get(i).toItemStack(), "missingFluids"));
        }

        for (ItemStack stack : toCraft) {
            list.add(Pair.of(stack, "toCraft"));
        }
        for (ItemStack stack : toProcess) {
            list.add(Pair.of(stack, "toProcess"));
        }
        for (int i = 0; i < toTake.getStackAmount(); i++) {
            list.add(Pair.of(toTake.get(i), "toTake"));
        }

        for (FluidStack stack : toCraftFluids) {
            list.add(Pair.of(stack.toItemStack(), "toCraftFluids"));
        }
        for (FluidStack stack : toProcessFluids) {
            list.add(Pair.of(stack.toItemStack(), "toProcessFluids"));
        }
        for (int i = 0; i < toTakeFluids.sizeStacks(); i++) {
            list.add(Pair.of(toTakeFluids.get(i).toItemStack(), "toTakeFluids"));
        }

        listCache = list;
        return list;
    }

    @Override
    public String toString() {
        return String.format("{Missing: Items: %s Fluids: %s | To Craft: Items: %s Fluids: %s | To Process: Items: %s Fluids: %s | To Take: Items: %s Fluids: %s}", missing, missingFluids, toCraft, toCraftFluids, toProcess, toProcessFluids, toTake, toTakeFluids);
    }
}
