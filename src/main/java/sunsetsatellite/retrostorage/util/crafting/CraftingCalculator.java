package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.ItemStackList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CraftingCalculator {
    private final DigitalNetwork network;
    private final int quantity;
    private final ItemStack requested;
    private final NetworkCraftable recipe;

    private final NodeList nodes = new NodeList();

    private final Set<NetworkCraftable> recipesUsed = new HashSet<>();

    private final CraftingPreviewInfo craftingPreviewInfo = new CraftingPreviewInfo();

    private final ItemStackList toExtractInitial = new ItemStackList();

    private long calculationStarted = -1;
    private final ArrayList<NetworkCraftable> knownRecipes;

    public CraftingCalculator(DigitalNetwork network, int quantity, ItemStack requested, NetworkCraftable recipe, ArrayList<NetworkCraftable> knownRecipes) {
        this.network = network;
        this.quantity = quantity;
        this.requested = requested;
        this.recipe = recipe;
        this.knownRecipes = knownRecipes;
    }

    public CalculationResult calculate() {
        this.calculationStarted = System.currentTimeMillis();

        if(recipe == null){
            return new CalculationResult(CalculationResultType.NO_RECIPE);
        }

        ItemStackList results = new ItemStackList();
        ItemStackList source = network.inventory.toList();

        int qtyPerCraft = qtyPerCraft(recipe);
        int qty = ((quantity - 1) / qtyPerCraft) + 1;

        try {
            calculateInternal(qty, source, results, recipe, true);
        } catch (CraftingCalculationException e){
            return new CalculationResult(e.getType(), e.getRecursiveRecipe());
        }

        if(requested != null){
            ItemStack stack = requested.copy();
            stack.stackSize = qty * qtyPerCraft;
            if(recipe.getType() == CraftableType.PROCESS){
                craftingPreviewInfo.getToProcess().add(stack);
            } else {
                craftingPreviewInfo.getToCraft().add(stack);
            }
        }

        if(craftingPreviewInfo.hasMissing()) {
            return new CalculationResult(CalculationResultType.MISSING_ITEMS,craftingPreviewInfo,null);
        }

        return new CalculationResult(CalculationResultType.OK,craftingPreviewInfo,new CraftingTask(network,quantity,nodes,recipe,toExtractInitial));
    }

    private void calculateInternal(int qty, ItemStackList source, ItemStackList results, NetworkCraftable recipe, boolean root) throws CraftingCalculationException {
        if (System.currentTimeMillis() - calculationStarted > 5000) {
            throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
        }

        if (!recipesUsed.add(recipe)) {
            throw new CraftingCalculationException(CalculationResultType.RECURSIVE,recipe);
        }

        ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));

        Node node = nodes.createOrAddToExistingNode(recipe, root, qty);

        calculateItems(qty, source, results, inputs, node);

        ItemStack output = recipe.getOutput().copy();
        output.stackSize *= qty;
        results.add(output);

        recipesUsed.remove(recipe);
    }

    private void calculateItems(int qty, ItemStackList source, ItemStackList results, ArrayList<ItemStack> inputs, Node node) throws CraftingCalculationException {
        int ingredientNumber = -1;

        for (ItemStack input : inputs) {
            ingredientNumber++;

            ItemStack fromSelf = results.get(input.itemID, input.getMetadata());
            ItemStack fromNetwork = source.get(input.itemID, input.getMetadata());

            int remaining = input.stackSize * qty;

            if (remaining < 0) { // int overflow
                throw new CraftingCalculationException(CalculationResultType.TOO_COMPLEX);
            }

            while(remaining > 0) {
                if(fromSelf != null){
                    int toTake = Math.min(remaining, fromSelf.stackSize);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.stackSize);

                    results.remove(fromSelf.itemID, fromSelf.getMetadata(), toTake, false, true);

                    remaining -= toTake;

                    fromSelf = results.get(input.itemID, input.getMetadata());
                }

                if (fromNetwork != null && remaining > 0) {
                    int toTake = Math.min(remaining, fromNetwork.stackSize);


                    ItemStack copy = input.copy();
                    copy.stackSize = toTake;
                    craftingPreviewInfo.getToTake().add(copy);

                    node.getRequirements().addItemRequirement(ingredientNumber, input, toTake, input.stackSize);

                    source.remove(fromNetwork.itemID, fromNetwork.getMetadata(), toTake, false, true);

                    remaining -= toTake;

                    fromNetwork = source.get(input.itemID,input.getMetadata());

                    copy = input.copy();
                    copy.stackSize = toTake;
                    toExtractInitial.add(copy);
                }

                if(remaining > 0){
                    NetworkCraftable subRecipe = RetroStorage.findRecipeByOutputUsingList(input, knownRecipes);
                    if(subRecipe != null){
                        int qtyPerCraft = qtyPerCraft(subRecipe);
                        int subQty = ((remaining - 1) / qtyPerCraft) + 1;

                        calculateInternal(subQty, source, results, subRecipe, false);

                        fromSelf = results.get(input.itemID, input.getMetadata());
                        if(fromSelf == null){
                            throw new CraftingCalculationException(CalculationResultType.ERROR,"Recursive calculation didn't yield anything!");
                        }

                        fromNetwork = source.get(input.itemID, input.getMetadata());

                        if(subRecipe.getType() == CraftableType.PROCESS){
                            craftingPreviewInfo.getToProcess().add(fromSelf.copy());
                        } else {
                            craftingPreviewInfo.getToCraft().add(fromSelf.copy());
                        }
                    } else {
                        ItemStack copy = input.copy();
                        copy.stackSize = remaining;
                        craftingPreviewInfo.getMissing().add(copy);
                        remaining = 0;
                    }
                }
            }
        }

    }

    private int qtyPerCraft(NetworkCraftable recipe) {
        return recipe.getOutput().stackSize;
    }

}
