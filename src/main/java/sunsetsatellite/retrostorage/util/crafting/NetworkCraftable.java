package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCrafting;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetworkCraftable {
    private RecipeEntryCrafting<?, ItemStack> recipe;
    private CraftingProcess process;

    public NetworkCraftable(@NotNull RecipeEntryCrafting<?, ItemStack> recipe) {
        this.recipe = recipe;
        this.process = null;
    }

    public NetworkCraftable(@NotNull CraftingProcess process) {
        this.process = process;
        this.recipe = null;
    }

    public NetworkCraftable(@NotNull NbtCompound tag) {
        readFromNBT(tag);
    }

    public RecipeEntryCrafting<?, ItemStack> getRecipe() {
        return recipe;
    }

    public CraftingProcess getProcess() {
        return process;
    }

    public CraftableType getType() {
        if (recipe != null) {
            return CraftableType.RECIPE;
        } else if (process != null) {
            return CraftableType.PROCESS;
        }
        return null;
    }

    public List<VariantStack> getOutput() {
        switch (getType()) {
            case RECIPE: {
                if (recipe != null) {
                    ArrayList<VariantStack> list = new ArrayList<>();
                    list.add(new VariantStack(recipe.getOutput().copy()));
                    return list;
                }
                return null;
            }
            case PROCESS:
                if (process != null) {
                    return new ArrayList<>(process.getAllOutputs());
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkCraftable craftable)) return false;

        return Objects.equals(getRecipe(), craftable.getRecipe()) && Objects.equals(getProcess(), craftable.getProcess());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getRecipe());
        result = 31 * result + Objects.hashCode(getProcess());
        return result;
    }

    public void readFromNBT(NbtCompound tag) {
        CraftableType type = CraftableType.valueOf(tag.getString("Type"));
        switch (type) {
            case RECIPE:
                NbtCompound nbt = tag.getCompound("Recipe");
                recipe = RetroStorage.findRecipeFromNBT(nbt);
                break;
            case PROCESS:
                process = new CraftingProcess(tag.getCompound("Process"));
                break;
        }
    }

    public void writeToNBT(NbtCompound tag) {
        tag.putString("Type", getType().name());
        switch (getType()) {
            case RECIPE:
                NbtCompound nbt = RetroStorage.itemsArrayToNBT(RetroStorage.getRecipeItems(this));
                tag.put("Recipe", nbt);
                break;
            case PROCESS:
                NbtCompound processTag = new NbtCompound();
                process.writeToNBT(processTag);
                tag.put("Process", processTag);
                break;
        }
    }

    @Override
    public String toString() {
        if (recipe != null) {
            return "Crafting: " + recipe;
        } else {
            return "Process: " + process.name;
        }
    }
}
