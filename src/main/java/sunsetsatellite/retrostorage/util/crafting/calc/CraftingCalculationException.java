package sunsetsatellite.retrostorage.util.crafting.calc;


import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

public class CraftingCalculationException extends Exception {
    private final CalculationResultType type;
    private final NetworkCraftable recursiveRecipe;
    private final String message;


    public CraftingCalculationException(CalculationResultType type, NetworkCraftable recursiveRecipe) {
        this.type = type;
        this.recursiveRecipe = recursiveRecipe;
        this.message = "";
    }

    public CraftingCalculationException(CalculationResultType type) {
        this.type = type;
        this.recursiveRecipe = null;
        this.message = "";
    }

    public CraftingCalculationException(CalculationResultType type, String message) {
        this.type = type;
        this.recursiveRecipe = null;
        this.message = message;
    }

    public CalculationResultType getType() {
        return type;
    }

    public NetworkCraftable getRecursiveRecipe() {
        return recursiveRecipe;
    }
}
