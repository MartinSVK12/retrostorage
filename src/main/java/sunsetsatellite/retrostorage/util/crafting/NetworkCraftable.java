package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
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

    public NetworkCraftable(@NotNull CompoundTag tag) {
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
        if (!(o instanceof NetworkCraftable)) return false;

        NetworkCraftable craftable = (NetworkCraftable) o;
        return Objects.equals(getRecipe(), craftable.getRecipe()) && Objects.equals(getProcess(), craftable.getProcess());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getRecipe());
        result = 31 * result + Objects.hashCode(getProcess());
        return result;
    }
    
    public void readFromNBT(CompoundTag tag) {
        CraftableType type = CraftableType.valueOf(tag.getString("Type"));
        switch (type){
            case RECIPE:
                recipe = (RecipeEntryCrafting<?, ItemStack>) Registries.RECIPES.getRecipeFromKey(tag.getString("Recipe")).recipe;
                break;
            case PROCESS:
                process = new CraftingProcess(tag.getCompound("Process"));
                break;
        }
    }
    
    public void writeToNBT(CompoundTag tag) {
        tag.putString("Type", getType().name());
        switch (getType()){
            case RECIPE:
                tag.putString("Recipe",recipe.toString());
                break;
            case PROCESS:
                CompoundTag processTag = new CompoundTag();
                process.writeToNBT(processTag);
                tag.put("Process",processTag);
                break;
        }
    }

    @Override
    public String toString() {
        if(recipe != null){
            return "Crafting: " + recipe;
        } else {
            return "Process: " + process.name;
        }
    }
}
