package sunsetsatellite.retrostorage.util.crafting;


import net.minecraft.recipe.CraftingRecipe;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetworkCraftable {
    private final CraftingRecipe recipe;
    private final CraftingProcess process;

    public NetworkCraftable(CraftingRecipe recipe) {
        this.recipe = recipe;
        this.process = null;
    }

    public NetworkCraftable(CraftingProcess process) {
        this.process = process;
        this.recipe = null;
    }

    public CraftingRecipe getRecipe() {
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
        return switch (getType()) {
            case RECIPE -> {
                if (recipe != null) {
                    ArrayList<VariantStack> list = new ArrayList<>();
                    list.add(new VariantStack(recipe.getOutput().copy()));
                    yield list;
                }
                yield null;
            }
            case PROCESS -> {
                if (process != null) {
                    yield new ArrayList<>(process.getAllOutputs());
                }
                yield null;
            }
        };
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
}
