package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;

public class CalculationResult {
    private final CalculationResultType type;
    private final CraftingPreviewInfo craftingPreviewInfo;
    private final NetworkCraftable recursiveRecipe;
    private final CraftingTask task;

    public CalculationResult(CalculationResultType type, CraftingPreviewInfo craftingPreviewInfo, CraftingTask task) {
        this.type = type;
        this.task = task;
        this.craftingPreviewInfo = craftingPreviewInfo;
        this.recursiveRecipe = null;
    }

    public CalculationResult(CalculationResultType type, NetworkCraftable recursiveRecipe) {
        this.type = type;
        this.recursiveRecipe = recursiveRecipe;
        this.craftingPreviewInfo = null;
        this.task = null;
    }

    public CalculationResult(CalculationResultType type) {
        this.type = type;
        this.craftingPreviewInfo = null;
        this.recursiveRecipe = null;
        this.task = null;
    }

    public CalculationResultType getType() {
        return type;
    }

    public CraftingPreviewInfo getCraftingPreviewInfo() {
        return craftingPreviewInfo;
    }

    public NetworkCraftable getRecursiveRecipe() {
        return recursiveRecipe;
    }

    public CraftingTask getTask() {
        return task;
    }
}
