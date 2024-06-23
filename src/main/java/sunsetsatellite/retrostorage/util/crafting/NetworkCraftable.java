package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetworkCraftable {
    private final RecipeEntryCrafting<?, ItemStack> recipe;
    private final CraftingProcess process;

    public NetworkCraftable(RecipeEntryCrafting<?, ItemStack> recipe) {
        this.recipe = recipe;
        this.process = null;
    }

    public NetworkCraftable(CraftingProcess process) {
        this.process = process;
        this.recipe = null;
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
                    list.add(new VariantStack(recipe.getOutput()));
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
}
