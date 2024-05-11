package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.retrostorage.util.ItemStackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingPreviewInfo {

    private final ItemStackList missing = new ItemStackList();
    private final ItemStackList toTake = new ItemStackList();

    private final List<ItemStack> toCraft = new ArrayList<>();
    private final List<ItemStack> toProcess = new ArrayList<>();

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

    private List<Pair<ItemStack,String>> listCache = new ArrayList<>();

    public int size(){
        return missing.sizeStacks()+ toTake.sizeStacks()+ toCraft.size() + toProcess.size();
    }

    public List<Pair<ItemStack,String>> toList(){
        if(!listCache.isEmpty()){
            return listCache;
        }
        List<Pair<ItemStack,String>> list = new ArrayList<>();
        for (int i = 0; i < missing.sizeStacks(); i++) {
            list.add(Pair.of(missing.get(i),"missing"));
        }
        for (ItemStack stack : toCraft) {
            list.add(Pair.of(stack, "toCraft"));
        }
        for (ItemStack stack : toProcess) {
            list.add(Pair.of(stack, "toProcess"));
        }
        for (int i = 0; i < toTake.sizeStacks(); i++) {
            list.add(Pair.of(toTake.get(i),"toTake"));
        }

        listCache = list;
        return list;
    }

    @Override
    public String toString() {
        return String.format("{Missing: %s, To Craft: %s | To Process: %s, To Take: %s}",missing,toCraft,toProcess,toTake);
    }
}
